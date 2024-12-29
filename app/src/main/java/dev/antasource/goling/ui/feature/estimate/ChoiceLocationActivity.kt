package dev.antasource.goling.ui.feature.estimate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
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
        estimateViewModel.region.observe(this){ region->
            binding.listRegion.adapter = RegionAdapter(region){selectedRegion ->
                estimateViewModel.originProvinceId = selectedRegion.name
                estimateViewModel.provinceId = selectedRegion.id.toInt()
                binding.textSelectedProvince.text = selectedRegion.name.toString()
                binding.textSelectedCity.visibility = View.VISIBLE
                estimateViewModel.getRegenciesId()
            }
        }

        estimateViewModel.regencies.observe(this){ regencies->
            binding.listRegion.adapter = RegenciesAdapter(regencies){selectedRegencies->
                estimateViewModel.originCityId = selectedRegencies.name
                estimateViewModel.regenciesId = selectedRegencies.id.toInt()
                binding.textSelectedCity.text = selectedRegencies.name.toString()
                binding.textSelectedDistrict.visibility = View.VISIBLE

                estimateViewModel.getDistric()

            }
        }
        estimateViewModel.districs.observe(this){distric ->
            binding.listRegion.adapter = DistrictAdapter(distric){ selectedDistric->
                estimateViewModel.originDistricId = selectedDistric.name
                estimateViewModel.districtId = selectedDistric.id.toInt()
                binding.textSelectedDistrict.text = selectedDistric.name.toString()
                binding.textSelectedVillages.visibility = View.VISIBLE

                estimateViewModel.getVillages()
            }
        }
        estimateViewModel.villages.observe(this){ village ->
            binding.listRegion.adapter = VillagesAdapter(village){ selectedVillage ->
                estimateViewModel.originVillagesId = selectedVillage.name
                binding.textSelectedVillages.text = selectedVillage.name
            }
        }

        estimateViewModel.getRegion()
    }
}