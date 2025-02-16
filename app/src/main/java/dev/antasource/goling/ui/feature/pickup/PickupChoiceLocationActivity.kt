package dev.antasource.goling.ui.feature.pickup

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
import dev.antasource.goling.databinding.ActivityPickupChoiceLocationBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.pickup.adapter.DistrictAdapter
import dev.antasource.goling.ui.feature.pickup.adapter.RegenciesAdapter
import dev.antasource.goling.ui.feature.pickup.adapter.RegionAdapter
import dev.antasource.goling.ui.feature.pickup.adapter.VillagesAdapter
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel


class PickupChoiceLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickupChoiceLocationBinding

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

    private val pickupViewModel by viewModels<PickupViewModel>() {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPickupChoiceLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        pickupViewModel.region.observe(this) { region ->
            binding.listRegionOrder.adapter = RegionAdapter(region) { selectedRegion ->
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originProvince = selectedRegion.name
                    originProvinceId = selectedRegion.id.toInt()
                } else {
                    destinateProvinceId = selectedRegion.id.toInt()
                    destinateProvince = selectedRegion.name
                }

                binding.textSelectedProvince.text = selectedRegion.name.toString()
                binding.textSelectedCity.visibility = View.VISIBLE
                pickupViewModel.provinceId = selectedRegion.id.toInt()
                pickupViewModel.getRegenciesId()
            }
        }

        pickupViewModel.regencies.observe(this) { regencies ->
            binding.listRegionOrder.adapter = RegenciesAdapter(regencies) { selectedRegencies ->
                pickupViewModel.regenciesId = selectedRegencies.id.toInt()
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originCity = selectedRegencies.name
                    originCityId = selectedRegencies.id.toInt()
                } else {
                    destinateCity = selectedRegencies.name
                    destinateCityId = selectedRegencies.id.toInt()
                }

                binding.textSelectedCity.text = selectedRegencies.name.toString()
                binding.textSelectedDistrict.visibility = View.VISIBLE
                pickupViewModel.getDistric()
            }
        }

        pickupViewModel.districs.observe(this) { distric ->
            binding.listRegionOrder.adapter = DistrictAdapter(distric) { selectedDistric ->

                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originDistric = selectedDistric.name
                    originDistrictId = selectedDistric.id.toInt()
                } else {
                    destinateDistric = selectedDistric.name
                    destinateDistricId = selectedDistric.id.toInt()
                }

                pickupViewModel.districtId = selectedDistric.id.toInt()
                binding.textSelectedDistrict.text = selectedDistric.name.toString()
                binding.textSelectedVillages.visibility = View.VISIBLE

                pickupViewModel.getVillages()
            }
        }

        pickupViewModel.villages.observe(this) { village ->
            binding.listRegionOrder.adapter = VillagesAdapter(village) { selectedVillage ->
                if (intent.getStringExtra("fieldLocation") == "origin") {
                    originVillage = selectedVillage.name
                    originVillageId = selectedVillage.id.toInt()
                } else {
                    destinateVillage = selectedVillage.name
                    destinateVillageId = selectedVillage.id.toInt()
                }

                directBackToForm()
            }
        }
        pickupViewModel.getRegion()
    }

    private fun directBackToForm() {
        if (intent.getStringExtra("fieldLocation") == "origin") {
            val intent = Intent(this, SenderFormActivity::class.java)
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
            intent.putExtra("originLocation", origin)
            startActivity(intent)
            finish()
        } else{
            val intent = Intent(this, RecepientFormActivity::class.java)
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

            intent.putExtra("destinationLocation", destination)
            startActivity(intent)
            finish()

        }
    }


}


