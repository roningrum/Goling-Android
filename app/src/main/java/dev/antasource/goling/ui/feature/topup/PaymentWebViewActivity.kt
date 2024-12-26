package dev.antasource.goling.ui.feature.topup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.databinding.ActivityWebViewBinding

@SuppressLint("SetJavaScriptEnabled")
class PaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private var url_payment : String = ""

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
        binding.paymentWebUrl.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
               request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                Log.d("WebView", "Redirected to: $url")
                return true
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



    }
}