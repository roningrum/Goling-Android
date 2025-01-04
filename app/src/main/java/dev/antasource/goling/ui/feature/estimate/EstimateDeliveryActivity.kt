package dev.antasource.goling.ui.feature.estimate

import android.app.ComponentCaller
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.transition.Visibility
import dev.antasource.goling.R
import dev.antasource.goling.data.model.country.LocationDeliver
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
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
import dev.antasource.goling.util.Util
import kotlin.getValue

class EstimateDeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstimateDeliveryBinding

    var weight = 0
    var width = 0
    var length = 0
    var height = 0

    var originProvinceId = 0
    var originCityId = 0
    var originDistricId = 0
    var originVillageId = 0

    var destinationProvinceId = 0
    var destinationCityId = 0
    var destinationDistricId = 0
    var destinationVillageId = 0


    private val estimateViewModel by viewModels<EstimateViewModel>(){
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onStart() {
        super.onStart()
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

        binding.inputOriginEditText.setHint("Origin")
        getDataLocation()

        binding.buttonCheckedInsurance.setOnCheckedChangeListener{ v, isChecked ->
            estimateViewModel.isGuarantee = isChecked
        }

        binding.buttonCheckEstimate.setOnClickListener{

            if (binding.inputWeightEdit.text.isNullOrEmpty() ||
                binding.inputLengthEdit.text.isNullOrEmpty() ||
                binding.inputHeightEdit.text.isNullOrEmpty() ||
                binding.inputWidthEdit.text.isNullOrEmpty()) {

                Toast.makeText(this, "Semua input harus diisi", Toast.LENGTH_SHORT).show()

            } else {
                weight = binding.inputWeightEdit.text.toString().toInt()
                length = binding.inputLengthEdit.text.toString().toInt()
                height= binding.inputHeightEdit.text.toString().toInt()
                width = binding.inputWidthEdit.text.toString().toInt()

                estimateViewModel.originProvinceId = originProvinceId
                estimateViewModel.originRegenciesId = originCityId
                estimateViewModel.originDistrictId = originDistricId
                estimateViewModel.originVillageId = originVillageId

                estimateViewModel.destinateProvinceId = destinationProvinceId
                estimateViewModel.destinateRegenciesId = destinationCityId
                estimateViewModel.destinationDistrictId = destinationDistricId
                estimateViewModel.destinationVillagesId = destinationVillageId

                estimateViewModel.weight = weight
                estimateViewModel.width = width
                estimateViewModel.height = height
                estimateViewModel.lenght = length

                estimateViewModel.getEstimateShipping()

                SharedPrefUtil.clearLocation(this)
            }

           Log.d("Berat Barang", "${binding.inputWeightEdit.text.toString().toInt()}")
        }

        estimateViewModel.data.observe(this){ data ->

            binding.cardEstimateLayout.visibility = View.VISIBLE
            if(data.details.isGuaranteed == true){
                binding.insuranceRateLayout.visibility = View.VISIBLE
                binding.insuranceCost.text = Util.formatCurrency(data.insuranceRate)
            }
            else{
                binding.insuranceRateLayout.visibility = View.GONE
            }
            binding.shipmentCost.text = Util.formatCurrency(data.itemPrice)
            binding.totalCost.text = Util.formatCurrency(data.totalCost)

        }
    }

    private fun getDataLocation() {
        val originData = SharedPrefUtil.getLastOriginLocation(this)
        originData?.let {

            binding.inputOriginEditText.setText("${it.province}, ${it.city}, ${it.distric}, ${it.village}")

            originProvinceId = it.provinceId
            originCityId = it.cityId
            originDistricId = it.districId
            originVillageId = it.villageId
        }

        val destinationData = SharedPrefUtil.getLastDestinateLocation(this)
        destinationData?.let {

            binding.inputDestinationEditText.setText("${it.province}, ${it.city}, ${it.distric}, ${it.village}")

            destinationProvinceId = it.provinceId
            destinationCityId = it.cityId
            destinationDistricId = it.districId
            destinationVillageId = it.villageId
        }

    }
}