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
import dev.antasource.goling.util.SharedPrefUtil
import kotlin.getValue

class ChoiceLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoiceLocationBinding

    private var originProvinceId = 0
    private var originCityId = 0
    private var originDistrictId = 0
    private var originVillageId = 0

    private var destinateProvinceId = 0
    private var destinateCityId = 0
    private var destinateDistricId = 0
    private var destinateVillageId = 0


    private var originProvince = ""
    private var originCity = ""
    private var originDistric = ""
    private var originVillage = ""

    private var destinateProvince = ""
    private var destinateCity = ""
    private var destinateDistric = ""
    private var destinateVillage = ""

    private val estimateViewModel by viewModels<EstimateViewModel>() {
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

        estimateViewModel.region.observe(this) { region ->
            binding.listRegion.adapter = RegionAdapter(region) { selectedRegion ->
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originProvince = selectedRegion.name
                    originProvinceId = selectedRegion.id.toInt()
                } else {
                    destinateProvinceId = selectedRegion.id.toInt()
                    destinateProvince = selectedRegion.name
                }
                binding.textSelectedProvince.text = selectedRegion.name.toString()
                binding.textSelectedCity.visibility = View.VISIBLE
                estimateViewModel.provinceId = selectedRegion.id.toInt()
                estimateViewModel.getRegenciesId()
            }
        }

        estimateViewModel.regencies.observe(this) { regencies ->
            binding.listRegion.adapter = RegenciesAdapter(regencies) { selectedRegencies ->
                estimateViewModel.regenciesId = selectedRegencies.id.toInt()
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originCity = selectedRegencies.name
                    originCityId = selectedRegencies.id.toInt()
                } else {
                    destinateCity = selectedRegencies.name
                    destinateCityId = selectedRegencies.id.toInt()
                }

                binding.textSelectedCity.text = selectedRegencies.name.toString()
                binding.textSelectedDistrict.visibility = View.VISIBLE

                estimateViewModel.getDistric()

            }
        }
        estimateViewModel.districs.observe(this) { distric ->
            binding.listRegion.adapter = DistrictAdapter(distric) { selectedDistric ->

                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originDistric = selectedDistric.name
                    originDistrictId = selectedDistric.id.toInt()
                } else {
                    destinateDistric = selectedDistric.name
                    destinateDistricId = selectedDistric.id.toInt()
                }

                estimateViewModel.districtId = selectedDistric.id.toInt()
                binding.textSelectedDistrict.text = selectedDistric.name.toString()
                binding.textSelectedVillages.visibility = View.VISIBLE

                estimateViewModel.getVillages()
            }
        }
        estimateViewModel.villages.observe(this) { village ->
            binding.listRegion.adapter = VillagesAdapter(village) { selectedVillage ->
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originVillage = selectedVillage.name
                    originVillageId = selectedVillage.id.toInt()
                } else {
                    destinateVillage = selectedVillage.name
                    destinateVillageId = selectedVillage.id.toInt()
                }

                binding.textSelectedVillages.text = selectedVillage.name
                directBackToForm()
            }
        }

        estimateViewModel.getRegion()
    }

    private fun directBackToForm() {
        if (intent.getStringExtra("fieldLocation") == "origin") {
            val intent = Intent(this, EstimateDeliveryActivity::class.java)
            val origin = LocationDeliver(
                originProvinceId,
                originCityId,
                originDistrictId,
                originVillageId,
                originProvince,
                originCity,
                originDistric,
                originVillage
            )
            SharedPrefUtil.saveOriginDataPrefs(this, origin)
            startActivity(intent)
            finish()
        } else if (intent.getStringExtra("fieldLocation") == "destinate") {
            val intent = Intent(this, EstimateDeliveryActivity::class.java)
            val destination = LocationDeliver(
                destinateProvinceId,
                destinateCityId,
                destinateDistricId,
                destinateVillageId,
                destinateProvince,
                destinateCity,
                destinateDistric,
                destinateVillage
            )
            SharedPrefUtil.saveDesDataPrefs(this, destination)
            startActivity(intent)
            finish()
        }

    }
}

