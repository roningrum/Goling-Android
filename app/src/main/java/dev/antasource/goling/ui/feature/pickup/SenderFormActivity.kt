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
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.databinding.ActivitySenderFormBinding
import dev.antasource.goling.util.ParcelableUtils.parcelabe

class SenderFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySenderFormBinding

    var originProvinceId = 0
    var originCityId = 0
    var originDistrictId = 0
    var originVillageId = 0

    var name = ""
    var address = ""
    var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySenderFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkSenderField()
        setupTextWatchers()
        binding.layoutSenderForm.buttonUseSenderDetail.setOnClickListener {
            directToPickupForm()
        }
        binding.layoutSenderForm.inputSenderOriginEditText.setOnClickListener {
            openLocationChoice("origin")
        }
        handleDataLocation()
    }

    private fun handleDataLocation() {
        val dataLocation: LocationDeliver? = intent.parcelabe("originLocation")
        dataLocation?.let { originData ->
            binding.layoutSenderForm.inputSenderOriginEditText.setText(
                buildString {
                    append(originData.province)
                    append(", ")
                    append(originData.city)
                    append(", ")
                    append(originData.distric)
                    append(", ")
                    append(originData.village)
                }
            )
            originProvinceId = originData.provinceId
            originCityId = originData.cityId
            originDistrictId = originData.districId
            originVillageId = originData.villageId
        }
    }


    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkSenderField()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkSenderField()
            }
        }

        binding.layoutSenderForm.inputSenderOriginEditText.addTextChangedListener(textWatcher)
        binding.layoutSenderForm.inputSenderAddressEditText.addTextChangedListener(textWatcher)
        binding.layoutSenderForm.inputSenderNameEditText.addTextChangedListener(textWatcher)
        binding.layoutSenderForm.inputSenderPhoneEditText.addTextChangedListener(textWatcher)
    }

    private fun checkSenderField() {
        val lokasi = binding.layoutSenderForm.inputSenderOriginEditText.editableText.toString()
        address = binding.layoutSenderForm.inputSenderAddressEditText.editableText.toString()
        name = binding.layoutSenderForm.inputSenderNameEditText.editableText.toString()
        phoneNumber =
            binding.layoutSenderForm.inputSenderPhoneEditText.editableText.toString().trim()

        val isFilled =
            lokasi.isNotEmpty() && address.isNotEmpty() && name.isNotEmpty() && phoneNumber.isNotEmpty()
        binding.layoutSenderForm.buttonUseSenderDetail.apply {
            isEnabled = isFilled
            setBackgroundColor(
                ContextCompat.getColor(
                    this@SenderFormActivity,
                    if (isFilled) R.color.darkBlue else R.color.grayBorder
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    this@SenderFormActivity,
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
        val originSender = OriginSender(
            originName = name,
            originCityId = originCityId.toString(),
            originPhone = phoneNumber.trim(),
            originAddress = address,
            originDistrictId = originDistrictId.toString(),
            originVillageId = originVillageId.toString(),
            originProvinceId = originProvinceId.toString()
        )
        val intent = Intent(this, PickupActivity::class.java)
        intent.putExtra("originSender", originSender)
        startActivity(intent)
    }
}




