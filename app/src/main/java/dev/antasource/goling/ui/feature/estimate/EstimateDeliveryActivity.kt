package dev.antasource.goling.ui.feature.estimate

import android.app.ComponentCaller
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import dev.antasource.goling.R
import dev.antasource.goling.data.model.country.LocationDeliver
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityEstimateDeliveryBinding
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.estimate.viewmodel.EstimateViewModel
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.util.SharedPrefUtil
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

        setSupportActionBar(binding.toolbarEstimateMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.inputOriginEditText.setOnClickListener{
            val intent = Intent(this, ChoiceLocationActivity::class.java)
            intent.putExtra("fieldLocation", "origin")
            startActivity(intent)
        }
        binding.inputDestinationEditText.setOnClickListener{
            val intent = Intent(this, ChoiceLocationActivity::class.java)
            intent.putExtra("fieldLocation", "destinate")
            startActivity(intent)
        }

        if(SharedPrefUtil.getLastOriginLocation(this)!= null){
            binding.inputOriginEditText.setText(SharedPrefUtil.getLastOriginLocation(this))
        }
        else{
            binding.inputOriginEditText.setHint("Origin")
        }
        getDataLocation()
    }

    private fun getDataLocation() {
                val bundle = intent.getBundleExtra("originLocation") ?: intent.getBundleExtra("destinateLocation")

        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable("origin", LocationDeliver::class.java)
                ?: bundle?.getParcelable("destinate", LocationDeliver::class.java)
        } else {
            bundle?.getParcelable<LocationDeliver>("origin")
                ?: bundle?.getParcelable<LocationDeliver>("destinate")
        }

        location?.let {
            if (intent.getBundleExtra("originLocation") != null) {
                val originLocation =
                binding.inputOriginEditText.setText("${it.province}, ${it.city}, ${it.distric}, ${it.village}")
                SharedPrefUtil.saveLastOriginLocation(this,"${it.province}, ${it.city}, ${it.distric}, ${it.village}")
                Log.d("Location Info", "Origin - City: ${it.city}, Province: ${it.province}")
            } else {
                binding.inputDestinationEditText.setText("${it.province}, ${it.city}, ${it.distric}, ${it.village}")
                Log.d("Location Info", "Destination - City: ${it.city}, Province: ${it.province}")
            }
        }

    }
}