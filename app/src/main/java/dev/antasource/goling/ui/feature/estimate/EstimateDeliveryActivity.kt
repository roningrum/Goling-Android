package dev.antasource.goling.ui.feature.estimate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityEstimateDeliveryBinding
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.estimate.viewmodel.EstimateViewModel
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel
import dev.antasource.goling.ui.feature.login.LoginActivity
import kotlin.getValue

class EstimateDeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstimateDeliveryBinding

    private val estimateViewModel by viewModels<EstimateViewModel>(){
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEstimateDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.inputOriginEditText.setOnClickListener{
            val intent = Intent(this, ChoiceLocationActivity::class.java)
            startActivity(intent)
        }

    }
}