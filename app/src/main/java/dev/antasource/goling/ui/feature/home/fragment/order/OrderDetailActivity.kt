package dev.antasource.goling.ui.feature.home.fragment.order

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowMetrics
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityOrderDetailBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.home.adapter.HistoryOrderAdapter
import dev.antasource.goling.ui.feature.home.viewmodel.TransactionViewModel
import dev.antasource.goling.util.SharedPrefUtil.getAccessToken
import dev.antasource.goling.util.Util
import dev.antasource.goling.util.Util.generateBarcode
import kotlin.getValue

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var orderDetailBinding: ActivityOrderDetailBinding

    private val transactionHistoryViewModel by viewModels<TransactionViewModel> {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        orderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(orderDetailBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(orderDetailBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        val orderId = intent.getStringExtra("orderId")?.toLong()

        val token = getAccessToken(this).toString()
        Log.d("Token Detail", "Token $token")

        val listHistory = orderDetailBinding.listHistoryOrder
        listHistory.layoutManager = LinearLayoutManager(this)

        transactionHistoryViewModel.token = token
        transactionHistoryViewModel.getHistoryDetail(orderId!!)

        transactionHistoryViewModel.orderHistory.observe(this) { orderDetail ->
            orderDetailBinding.orderIdTitle.text = buildString {
                append("Order ID : ")
                append(orderDetail.orderId)
            }
            orderDetailBinding.txtOrderDetailWeight.text = "${orderDetail.weight}"

            val barcodeBitmap = generateBarcode(orderDetail.orderId, 600, 200)
            orderDetailBinding.barcodeImage.setImageBitmap(barcodeBitmap)

            orderDetailBinding.txtOrderDetailSize.text = buildString {
                append(orderDetail.length)
                append("cm x ")
                append(orderDetail.width)
                append("cm x ")
                append(orderDetail.height)
                append("cm")
            }

            orderDetailBinding.orderDateTxt.text = Util.convertIsoToDate(orderDetail.createdAt)
            transactionHistoryViewModel.getproductTypeById(orderDetail.productType)
        }

        transactionHistoryViewModel.productTypeName.observe(this){productType ->
            orderDetailBinding.txtOrderProductyType.text = productType
        }


        transactionHistoryViewModel.history.observe(this) { history ->
            Log.d("History Data", "History $history")
            if (history != null) {
                val adapter = HistoryOrderAdapter(history)
                listHistory.adapter = adapter
            }
        }
    }

    private fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            windowMetrics.bounds.width()
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }
}