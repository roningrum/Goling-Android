package dev.antasource.goling.ui.feature.pickup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.antasource.goling.R
import dev.antasource.goling.data.model.pickup.request.AdditionalDetails
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.data.model.pickup.request.PackageDetails
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityPickupBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel
import dev.antasource.goling.util.ParcelableUtils.parcelabe
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util.bitmapToFile
import dev.antasource.goling.util.Util.getResizedBitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.getValue

class PickupActivity : AppCompatActivity() {

    private lateinit var pickupBinding: ActivityPickupBinding

    private lateinit var dataOriginSender: OriginSender
    private lateinit var dataDestinationReceipt: DestinationReceipt
    private var path = ""
    private lateinit var packageDetails: PackageDetails

    private val pickupViewModel by viewModels<PickupViewModel>() {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pickupBinding = ActivityPickupBinding.inflate(layoutInflater)
        setContentView(pickupBinding.root)

        setupToolbar()
        setupFormNavigation()
        handleData()

        pickupViewModel.token = SharedPrefUtil.getAccessToken(this).toString()
        pickupBinding.buttonPay.setOnClickListener{
            sendData()
        }

        pickupViewModel.orderResponse.observe(this){ order ->
            showEstimatePriceOrder(order)
        }

    }

    private fun handleData() {
        val dataOrigin: OriginSender? = intent.parcelabe("originSender")
        val dataPenerima: DestinationReceipt? = intent.parcelabe("destination")
        val pathImage = intent.getStringExtra("path")
        val packageInfo: PackageDetails? = intent.parcelabe("packageInfo")

        pathImage?.let {
            path = it
        }

        dataOrigin?.let {
            pickupViewModel.saveOriginDataToLocal(this, it) }
        dataPenerima?.let {
            pickupViewModel.saveDestinationToLocal(this, it)
        }

        packageInfo?.let {
            packageDetails = packageInfo
            pickupBinding.layoutPickupForm.cardDetailPacket.visibility = View.VISIBLE
            pickupBinding.layoutPickupForm.weightItem.text = buildString {
                append(it.weight)
                append("kg")
            }
            pickupBinding.layoutPickupForm.dimensionItem.text = buildString {
                append(it.length)
                append("x")
                append(it.width)
                append("x")
                append(it.height)
            }

            pickupViewModel.productName.observe(this){
                pickupBinding.layoutPickupForm.jenisItem.text = it.productTypeName.name
            }

            pickupViewModel.getproductTypeById(it.productType)

        }

        pickupViewModel.getOriginData(this)
        pickupViewModel.getReceiptData(this)


        pickupViewModel.sender.observe(this) {
            if (it != null) {
                dataOriginSender = it
                bindOriginData(it)
            }
        }
        pickupViewModel.receipt.observe(this) {
            if (it != null) {
                dataDestinationReceipt = it
                bindDestinationData(it)
            }

        }
    }

    private fun sendData() {
        Log.d("Path", "Path Image $path")
        Log.d("origin provinde Id", "Origin Distric Sender ${dataOriginSender.originDistrictId} ")
        val file = File(path)

        val compressed = getResizedBitmap(file.path, 1024)
        val afterResized = bitmapToFile(this, compressed)

        val fileSizeKB = afterResized.length() / 1024
        val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())

        if(fileSizeKB <= 1024){
            pickupViewModel.sendOrder(orderRequest = OrderRequest(
            originSender = dataOriginSender,
            destinationReceipt = dataDestinationReceipt,
            packageDetails = packageDetails,
            additionalDetails = AdditionalDetails(
                glassware = false,
                isGuaranteed = false
            ),
            multipartImage = MultipartBody.Part.createFormData("photo", afterResized.name, requestFile)
        ))
        } else{
            Log.d("Compressed File", "File Besar")
        }

    }



    private fun setupToolbar() {
        setSupportActionBar(pickupBinding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        pickupBinding.materialToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupFormNavigation() {
        pickupBinding.layoutPickupForm.senderDetail.setOnClickListener {
            startActivity(Intent(this, SenderFormActivity::class.java))
        }
        pickupBinding.layoutPickupForm.recepientDetail.setOnClickListener {
            startActivity(Intent(this, RecepientFormActivity::class.java))
        }
        pickupBinding.layoutPickupForm.itemPackageDetail.setOnClickListener {
            startActivity(Intent(this, ItemDetailPackageActivity::class.java))
        }
    }

    private fun bindOriginData(data: OriginSender) {
        Log.d("Origin Data", "data $data")
        pickupBinding.layoutPickupForm.apply {
            senderNamelayout.visibility = View.VISIBLE
            phoneSender.visibility = View.VISIBLE
            cardAddressSender.visibility = View.VISIBLE
            nameSender.text = data.originName
            phoneSender.text = data.originPhone
            senderAddress.text = data.originAddress
        }
    }

    private fun bindDestinationData(data: DestinationReceipt) {
        pickupBinding.layoutPickupForm.apply {
            recepientNamelayout.visibility = View.VISIBLE
            phoneRecepientSender.visibility = View.VISIBLE
            cardAddressRecepient.visibility = View.VISIBLE
            nameRecepientSender.text = data.destinationName
            phoneRecepientSender.text = data.destinationPhone
            textRecepientAddress.text = data.destinationAddress
        }
    }

    private fun showEstimatePriceOrder(response: OrderResponse) {
        pickupBinding.layoutPickupForm.summaryAmount.visibility = View.VISIBLE
        pickupBinding.layoutPickupForm.txtEstimationSend.text = response.estimasi
        pickupBinding.layoutPickupForm.txtIncludeAssuranceSend.text = "${response.order.insuranceRate}"
        pickupBinding.layoutPickupForm.txtTotalAmountSend.text = "${response.order.totalCost}"
    }
}


