package dev.antasource.goling.ui.feature.estimate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.model.country.LocationDeliver
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityChoiceLocationBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.estimate.adapter.DistrictAdapter
import dev.antasource.goling.ui.feature.estimate.adapter.RegenciesAdapter
import dev.antasource.goling.ui.feature.estimate.adapter.RegionAdapter
import dev.antasource.goling.ui.feature.estimate.adapter.VillagesAdapter
import dev.antasource.goling.ui.feature.estimate.viewmodel.EstimateViewModel
import kotlin.getValue

class ChoiceLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoiceLocationBinding

    private var originProvince = ""
    private var originCity = ""
    private var originDistric = ""
    private var originVillage = ""

    private var destinateProvince = ""
    private var destinateCity = ""
    private var destinateDistric = ""
    private var destinateVillage = ""

    private val estimateViewModel by viewModels<EstimateViewModel>(){
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChoiceLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        estimateViewModel.region.observe(this){ region->
            binding.listRegion.adapter = RegionAdapter(region){selectedRegion ->
                if(intent.getStringExtra("fieldLocation") == "origin"){
                    originProvince= selectedRegion.name
                }else{
                    destinateProvince = selectedRegion.name
                }
                estimateViewModel.provinceId = selectedRegion.id.toInt()
                binding.textSelectedProvince.text = selectedRegion.name.toString()
                binding.textSelectedCity.visibility = View.VISIBLE
                estimateViewModel.getRegenciesId()
            }
        }

        estimateViewModel.regencies.observe(this){ regencies->
            binding.listRegion.adapter = RegenciesAdapter(regencies){selectedRegencies->
                originCity = selectedRegencies.name
                destinateCity = selectedRegencies.name
                estimateViewModel.regenciesId = selectedRegencies.id.toInt()
                binding.textSelectedCity.text = selectedRegencies.name.toString()
                binding.textSelectedDistrict.visibility = View.VISIBLE

                estimateViewModel.getDistric()

            }
        }
        estimateViewModel.districs.observe(this){distric ->
            binding.listRegion.adapter = DistrictAdapter(distric){ selectedDistric->
                originDistric = selectedDistric.name
                destinateDistric = selectedDistric.name
                estimateViewModel.districtId = selectedDistric.id.toInt()
                binding.textSelectedDistrict.text = selectedDistric.name.toString()
                binding.textSelectedVillages.visibility = View.VISIBLE

                estimateViewModel.getVillages()
            }
        }
        estimateViewModel.villages.observe(this){ village ->
            binding.listRegion.adapter = VillagesAdapter(village){ selectedVillage ->
                originVillage = selectedVillage.name
                destinateVillage = selectedVillage.name
                binding.textSelectedVillages.text = selectedVillage.name
                directBackToForm()
            }
        }

        estimateViewModel.getRegion()
    }

    private fun directBackToForm() {
        if(intent.getStringExtra("fieldLocation") == "origin"){
            val intent = Intent(this, EstimateDeliveryActivity::class.java)
            var bundle = Bundle()
            bundle.putParcelable("origin", LocationDeliver(originProvince,originCity, originDistric,originVillage))
            intent.putExtra("originLocation", bundle)
            startActivity(intent)
            finish()
        }else if (intent.getStringExtra("fieldLocation") == "destinate"){
            val intent = Intent(this, EstimateDeliveryActivity::class.java)
            var bundle = Bundle()
            bundle.putParcelable("destinate", LocationDeliver(destinateProvince,destinateCity, destinateDistric,destinateVillage))
            intent.putExtra("destinateLocation", bundle)
            startActivity(intent)
            finish()
        }

    }
}

