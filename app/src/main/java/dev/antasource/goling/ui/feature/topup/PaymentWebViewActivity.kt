package dev.antasource.goling.ui.feature.topup
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.TopUpRepository
import dev.antasource.goling.databinding.ActivityWebViewBinding
import dev.antasource.goling.ui.factory.TopUpViewModelFactory
import dev.antasource.goling.ui.feature.home.HomeActivity
import dev.antasource.goling.ui.feature.topup.viewmodel.TopupViewModel
import dev.antasource.goling.util.SharedPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@SuppressLint("SetJavaScriptEnabled")
class PaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private var url_payment : String = ""

    private val topupViewModel by viewModels<TopupViewModel>(){
        val networkRemoteSource = NetworkRemoteSource()
        val topUpRepo = TopUpRepository(networkRemoteSource)
        TopUpViewModelFactory(topUpRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        url_payment = intent.getStringExtra("payment_url").toString()
        topupViewModel.token = SharedPrefUtil.getAccessToken(this).toString()

        binding.paymentWebUrl.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
               request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                Log.d("WebView", "Redirected to: $url")
                if(url.contains("/verify-payment")){
                    val transactionId = extractTransactionId(url)
                    Log.d("WebView", "Transaction Id $transactionId")
                    if(transactionId.isNotEmpty()){
                        topupViewModel.transactionId = transactionId
                        topupViewModel.verifyTopUp()
                    }

                    return true
                }
                return false
            }



            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "Page finished loading: $url")
            }
        }
        binding.paymentWebUrl.loadUrl(url_payment)
        binding.paymentWebUrl.settings.apply{
            javaScriptEnabled = true
            allowFileAccess = true
            domStorageEnabled = true
        }

        topupViewModel.paymentVerify.observe(this){ response ->
            if(response.status == 200){
                CoroutineScope(Dispatchers.Main).launch{
                    delay(3000)
                    response?.let {
                        val intent = Intent(this@PaymentWebViewActivity, SuccessTopUpActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        topupViewModel.errorMessage.observe(this){ errorMsg ->
            CoroutineScope(Dispatchers.Main).launch{
                delay(3000)
                errorMsg?.let {
                    val intent = Intent(this@PaymentWebViewActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    showSnackbar(errorMsg)
                }
            }

        }

    }

    private fun showSnackbar(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun extractTransactionId(url: String): String {
        val regex = "transactionId=([a-zA-Z0-9-]+)".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1) ?: ""
    }
}