package dev.antasource.goling.ui.feature.estimate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityEstimateDeliveryBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.estimate.viewmodel.EstimateViewModel
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util
import kotlin.getValue

class EstimateDeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstimateDeliveryBinding

    var weightText = 0
    var widthText = 0
    var lengthText = 0
    var heightText = 0

    var originProvinceId = 0
    var originCityId = 0
    var originDistricId = 0
    var originVillageId = 0

    var destinationProvinceId = 0
    var destinationCityId = 0
    var destinationDistricId = 0
    var destinationVillageId = 0


    private val estimateViewModel by viewModels<EstimateViewModel>() {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstimateDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        binding.inputOriginEditText.setOnClickListener {
            val intent = Intent(this, ChoiceLocationActivity::class.java)
            intent.putExtra("fieldLocation", "origin")
            startActivity(intent)
        }

        binding.inputDestinationEditText.setOnClickListener {
            val intent = Intent(this, ChoiceLocationActivity::class.java)
            intent.putExtra("fieldLocation", "destinate")
            startActivity(intent)
        }
        getDataLocation()
        binding.buttonCheckedInsurance.setOnCheckedChangeListener { v, isChecked ->
            estimateViewModel.isGuarantee = isChecked
        }

        setupButtonState()

        binding.buttonCheckEstimate.setOnClickListener {
                weightText = binding.inputWeightEdit.text.toString().toInt()
                lengthText = binding.inputLengthEdit.text.toString().toInt()
                heightText = binding.inputHeightEdit.text.toString().toInt()
                widthText = binding.inputWidthEdit.text.toString().toInt()

                estimateViewModel.apply {
                    oriProvinceId = originProvinceId
                    originRegenciesId = originCityId
                    originDistrictId = originDistricId
                    oriVillageId = originVillageId

                    destinateProvinceId = destinationProvinceId
                    destinateRegenciesId = destinationCityId
                    destinationDistrictId = destinationDistricId
                    destinationVillagesId = destinationVillageId

                    weight = weightText
                    width = widthText
                    height = heightText
                    lenght = lengthText
                }

                estimateViewModel.getEstimateShipping()
                SharedPrefUtil.clearLocation(this)
            }
        estimateViewModel.data.observe(this) { data ->

            binding.cardEstimateLayout.visibility = View.VISIBLE
            if (data.details.isGuaranteed == true) {
                binding.insuranceRateLayout.visibility = View.VISIBLE
                binding.insuranceCost.text = Util.formatCurrency(data.insuranceRate)
            } else {
                binding.insuranceRateLayout.visibility = View.GONE
            }
            binding.shipmentCost.text = Util.formatCurrency(data.itemPrice)
            binding.totalCost.text = Util.formatCurrency(data.totalCost)

        }

        estimateViewModel.errorMsg.observe(this){ error ->
            showSnackbarError(error)
        }
    }

    private fun showSnackbarError(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.redColor))
        snackbarMsg.show()
    }

    private fun setupButtonState() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonCheckEstimate.isEnabled = isFormFilled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.inputWeightEdit.addTextChangedListener(textWatcher)
        binding.inputLengthEdit.addTextChangedListener(textWatcher)
        binding.inputHeightEdit.addTextChangedListener(textWatcher)
        binding.inputWidthEdit.addTextChangedListener(textWatcher)


        binding.inputOriginEditText.addTextChangedListener(textWatcher)
        binding.inputDestinationEditText.addTextChangedListener(textWatcher)

        // Set initial state
        binding.buttonCheckEstimate.isEnabled = isFormFilled()
    }

    private fun isFormFilled(): Boolean {
        return binding.inputWeightEdit.text?.isNotEmpty() == true &&
                binding.inputLengthEdit.text?.isNotEmpty() == true &&
                binding.inputHeightEdit.text?.isNotEmpty() == true &&
                binding.inputWidthEdit.text?.isNotEmpty() == true &&
                binding.inputOriginEditText.text?.isNotEmpty() == true &&
                binding.inputDestinationEditText.text?.isNotEmpty() == true
    }

    private fun getDataLocation() {
        val originData = SharedPrefUtil.getLastOriginLocation(this)
        originData?.let {

            binding.inputOriginEditText.setText(buildString {
                append(it.province)
                append(", ")
                append(it.city)
                append(", ")
                append(it.distric)
                append(", ")
                append(it.village)
            })

            originProvinceId = it.provinceId
            originCityId = it.cityId
            originDistricId = it.districId
            originVillageId = it.villageId
        }

        val destinationData = SharedPrefUtil.getLastDestinateLocation(this)
        destinationData?.let {

            binding.inputDestinationEditText.setText(
                buildString {
                    append(it.province)
                    append(", ")
                    append(it.city)
                    append(", ")
                    append(it.distric)
                    append(", ")
                    append(it.village)
                })

            destinationProvinceId = it.provinceId
            destinationCityId = it.cityId
            destinationDistricId = it.districId
            destinationVillageId = it.villageId
        }
        binding.buttonCheckEstimate.isEnabled = isFormFilled()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarEstimateMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        binding.toolbarEstimateMenu.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}