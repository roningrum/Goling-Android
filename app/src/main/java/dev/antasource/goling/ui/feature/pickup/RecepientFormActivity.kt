package dev.antasource.goling.ui.feature.pickup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.model.country.LocationDeliver
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.databinding.ActivityRecepientFormBinding
import dev.antasource.goling.util.ParcelableUtils.parcelabe

class RecepientFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecepientFormBinding

    var recepientProvinceId = 0
    var recepientCityId = 0
    var recepientDistricId = 0
    var recepientVillageId = 0

    private var name = ""
    private var address = ""
    private var phoneNumber = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecepientFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTextWatchers()
        checkSenderField()
        binding.layoutRecepientForm.buttonUseRecepipDetail.setEnabled(false)

        binding.layoutRecepientForm.inputRecepientOriginEditText.setOnClickListener {
            openLocationChoice("recepient")
        }

        binding.layoutRecepientForm.buttonUseRecepipDetail.setOnClickListener {
            directToPickupForm()
        }
        getLocationDestination()

    }

    private fun getLocationDestination() {
        val destinationData: LocationDeliver? = intent.parcelabe("destinationLocation")
        destinationData?.let {
            binding.layoutRecepientForm.inputRecepientOriginEditText.setText(buildString {
                append(it.province)
                append(", ")
                append(it.city)
                append(", ")
                append(it.distric)
                append(", ")
                append(it.village)
            })
            recepientProvinceId = it.provinceId
            recepientCityId = it.cityId
            recepientDistricId = it.districId
            recepientVillageId = it.villageId
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkSenderField()
            }
        }

        binding.layoutRecepientForm.inputRecepientNameEditText.addTextChangedListener(textWatcher)
        binding.layoutRecepientForm.inputRecepientAddressEditText.addTextChangedListener(textWatcher)
        binding.layoutRecepientForm.inputRecepientOriginEditText.addTextChangedListener(textWatcher)
        binding.layoutRecepientForm.inputRecepientPhoneEditText.addTextChangedListener(textWatcher)

    }

    private fun checkSenderField() {
        val lokasi =
            binding.layoutRecepientForm.inputRecepientOriginEditText.editableText.toString()
        name = binding.layoutRecepientForm.inputRecepientNameEditText.editableText.toString()
        address = binding.layoutRecepientForm.inputRecepientAddressEditText.editableText.toString()
        phoneNumber =
            binding.layoutRecepientForm.inputRecepientPhoneEditText.editableText.toString()

        val isFilled =
            lokasi.isNotEmpty() && name.isNotEmpty() && address.isNotEmpty() && phoneNumber.isNotEmpty()
        binding.layoutRecepientForm.buttonUseRecepipDetail.apply {
            isEnabled = isFilled
            setBackgroundColor(
                ContextCompat.getColor(
                    this@RecepientFormActivity,
                    if (isFilled) R.color.darkBlue else R.color.grayBorder
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    this@RecepientFormActivity,
                    if (isFilled) R.color.backgroundColor else R.color.grayColor
                )
            )
        }
    }

    private fun openLocationChoice(fieldLocation: String) {
        val intent = Intent(this, PickupChoiceLocationActivity::class.java)
        intent.putExtra("fieldLocation", fieldLocation)
        startActivity(intent)
    }

    private fun directToPickupForm() {
        val recepientDestination = DestinationReceipt(
            destinationProvinceId = recepientProvinceId.toString(),
            destinationCityId = recepientCityId.toString(),
            destinationDistricId = recepientDistricId.toString(),
            destinationVillageId = recepientVillageId.toString(),
            destinationAddress = address,
            destinationName = name,
            destinationPhone = phoneNumber
        )
        val intent = Intent(this, PickupActivity::class.java)
        intent.putExtra("destination", recepientDestination)
        startActivity(intent)
    }
}