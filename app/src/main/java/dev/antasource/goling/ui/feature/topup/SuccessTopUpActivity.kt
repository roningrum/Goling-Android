package dev.antasource.goling.ui.feature.topup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.databinding.ActivitySuccessTopUpBinding
import dev.antasource.goling.ui.feature.home.HomeActivity
import dev.antasource.goling.ui.feature.topup.PaymentWebViewActivity

class SuccessTopUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessTopUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySuccessTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonSuccessTopUp.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}