package dev.antasource.goling.ui.feature.pickup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.model.estimate.Data
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.pickup.request.AdditionalDetails
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.data.model.pickup.request.PackageDetails
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityPickupBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.home.HomeActivity
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel
import dev.antasource.goling.util.ParcelableUtils.parcelabe
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util
import dev.antasource.goling.util.Util.bitmapToFile
import dev.antasource.goling.util.Util.getResizedBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var itemPrice = 0

    private lateinit var packageDetails: PackageDetails
    private lateinit var additionalDetail: AdditionalDetails

    private val pickupViewModel by viewModels<PickupViewModel>() {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickupBinding = ActivityPickupBinding.inflate(layoutInflater)
        setContentView(pickupBinding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(pickupBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupFormNavigation()
        handleData()
        pickupViewModel.token = SharedPrefUtil.getAccessToken(this).toString()
        pickupViewModel.orderResponse.observe(this){ order ->
            Toast.makeText(this, order.orderHistory.status, Toast.LENGTH_SHORT).show()
            pickupViewModel.clearOrderData(this)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@PickupActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        })

    }

    private fun handleData() {
        val dataOrigin: OriginSender? = intent.parcelabe("originSender")
        val dataPenerima: DestinationReceipt? = intent.parcelabe("destination")
        val pathImage = intent.getStringExtra("path")
        val packageInfo: PackageDetails? = intent.parcelabe("packageInfo")
        val additionalDetails: AdditionalDetails? = intent.parcelabe("additionalInfo")

        pathImage?.let {
            path = it
        }
        dataOrigin?.let {
            pickupViewModel.saveOriginDataToLocal(this, it) }
        dataPenerima?.let {
            pickupViewModel.saveDestinationToLocal(this, it)
        }
        pickupViewModel.getOriginData(this)
        pickupViewModel.getReceiptData(this)

        packageInfo?.let {
            packageDetails = packageInfo
            pickupBinding.layoutPickupForm.cardDetailPacket.visibility = VISIBLE
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

           checkData()

        }

        additionalDetails?.let {
            additionalDetail = additionalDetails
        }

        pickupViewModel.sender.observe(this) {
            if (it != null) {
                dataOriginSender = it
                bindOriginData(it)
                checkData()
            }
        }
        pickupViewModel.receipt.observe(this) {
            if (it != null) {
                dataDestinationReceipt = it
                bindDestinationData(it)
                checkData()
            }
        }
    }

    private fun checkData() {
        if (this::dataOriginSender.isInitialized && this::dataDestinationReceipt.isInitialized && this::packageDetails.isInitialized) {
            getEstimateData()
        }
    }


    private fun getEstimateData(){
        val producTypeName = intent.getStringExtra("productTypeName")
        val estimateShipRequest = EstimateShipRequest(
            originProvinceId = dataOriginSender.originProvinceId.toInt(),
            originDistrictId = dataOriginSender.originDistrictId.toInt(),
            originCityId = dataOriginSender.originCityId.toInt(),
            originVillageId = dataOriginSender.originVillageId.toInt(),
            originAddress = dataOriginSender.originAddress,
            destinationProvinceId = dataDestinationReceipt.destinationProvinceId.toInt(),
            destinationCityId = dataDestinationReceipt.destinationCityId.toInt(),
            destinationDistrictId = dataDestinationReceipt.destinationDistrictId.toInt(),
            destinationVillageId = dataDestinationReceipt.destinationVillageId.toInt(),
            destinationAddress = dataDestinationReceipt.destinationAddress,
            height = packageDetails.height.toInt(),
            width = packageDetails.width.toInt(),
            length = packageDetails.length.toInt(),
            weight = packageDetails.weight.toInt(),
            productType = producTypeName.toString(),
            isGuaranteed = additionalDetail.isGuaranteed
        )
        pickupViewModel.getEstimatePrice(estimateShipRequest)

        pickupViewModel.data.observe(this){ data ->
            showEstimatePriceOrder(data)
        }

    }

    private fun showEstimatePriceOrder(response: Data) {
        //show Progress bar
        pickupBinding.layoutPickupForm.progressLoadingSummary.visibility = VISIBLE
        pickupBinding.layoutPickupForm.summaryAmount.visibility = GONE

        CoroutineScope(Dispatchers.Main).launch{
            delay(2000)
            pickupBinding.layoutPickupForm.progressLoadingSummary.visibility = GONE
            pickupBinding.layoutPickupForm.summaryAmount.visibility = VISIBLE
            pickupBinding.layoutPickupForm.txtEstimationSend.text = response.deliveryTime
            pickupBinding.layoutPickupForm.txtIncludeAssuranceSend.text = "${response.insuranceRate}"
            pickupBinding.layoutPickupForm.txtTotalAmountSend.text = "${response.totalCost}"

            itemPrice = response.itemPrice

            showBalanceOrder(itemPrice)
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun showBalanceOrder(price : Int){
        pickupBinding.payCard.visibility = VISIBLE
        pickupViewModel.getBallance()
        pickupViewModel.balance.observe(this){ it->
            if(it.balance != 0){
                pickupBinding.pickupBalanceWallet.text = "${it.balance}"
            } else{
                pickupBinding.pickupBalanceWallet.text = "0"
            }

            if(it.balance < price){
                pickupBinding.buttonPay.isEnabled = false
                pickupBinding.pickupBalanceWallet.setTextColor(R.color.redColor)
                pickupBinding.pickupBalanceWallet.text = "Saldo kurang: ${Util.formatCurrency(it.balance)}"
            } else{
                pickupBinding.buttonPay.isEnabled = true
                pickupBinding.pickupBalanceWallet.text = "${it.balance}"
            }
        }
        pickupBinding.buttonPay.setOnClickListener {
            if(pickupBinding.buttonPay.isEnabled){
                sendData()
            }
        }
    }

    private fun sendData() {
        val file = File(path)
        val compressed = getResizedBitmap(file.path, 1024)
        val afterResized = bitmapToFile(this, compressed)
        val fileSizeKB = afterResized.length() / 1024
        val requestFile = afterResized.asRequestBody("image/jpg".toMediaTypeOrNull())

        if(fileSizeKB <= 1024){
            pickupViewModel.sendOrder(orderRequest = OrderRequest(
                originSender = dataOriginSender,
                destinationReceipt = dataDestinationReceipt,
                packageDetails = packageDetails,
                additionalDetails = additionalDetail,
                multipartImage = MultipartBody.Part.createFormData("photo", afterResized.name, requestFile)
            ))
        } else{
            Toast.makeText(this, "File Terlalu Besar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(pickupBinding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        pickupBinding.materialToolbar.setNavigationOnClickListener {
            val intent = Intent(this@PickupActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
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
            senderNamelayout.visibility = VISIBLE
            phoneSender.visibility = VISIBLE
            cardAddressSender.visibility = VISIBLE
            nameSender.text = data.originName
            phoneSender.text = data.originPhone
            senderAddress.text = data.originAddress
        }
    }

    private fun bindDestinationData(data: DestinationReceipt) {
        pickupBinding.layoutPickupForm.apply {
            recepientNamelayout.visibility = VISIBLE
            phoneRecepientSender.visibility = VISIBLE
            cardAddressRecepient.visibility = VISIBLE
            nameRecepientSender.text = data.destinationName
            phoneRecepientSender.text = data.destinationPhone
            textRecepientAddress.text = data.destinationAddress
        }
    }


}


