package dev.antasource.goling.ui.feature.topup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.TopUpRepository
import dev.antasource.goling.databinding.ActivityTopUpBinding
import dev.antasource.goling.ui.factory.TopUpViewModelFactory
import dev.antasource.goling.ui.feature.topup.adapter.ChipAmountAdapter
import dev.antasource.goling.ui.feature.topup.viewmodel.TopupViewModel
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class TopUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopUpBinding
    private var isAmountSelected = false

    private val topupViewModel by viewModels<TopupViewModel>(){
        val networkRemoteSource = NetworkRemoteSource()
        val topUpRepo = TopUpRepository(networkRemoteSource)
        TopUpViewModelFactory(topUpRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTopUpConfirm.visibility = GONE

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        topupViewModel.token = SharedPrefUtil.getAccessToken(this).toString()
        topupViewModel.topUpResponse.observe(this){ response ->
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PaymentWebViewActivity::class.java)
            intent.putExtra("payment_url", response.invoiceUrl)
            startActivity(intent)
            finish()
        }
        topupViewModel.errorMessage.observe(this){response ->
            Toast.makeText(this, "Response $response", Toast.LENGTH_SHORT).show()

        }
        val chipAmount = listOf("100000", "200000","300000", "350000", "400000", "500000", "1000000")
        binding.gridAmountChip.layoutManager = GridLayoutManager(this, 2)
        val adapter = ChipAmountAdapter(this, chipAmount) { chipText ->
            isAmountSelected = true
            val nominal = Util.formatCurrency(chipText.toInt())
            binding.amountEditText.setText(nominal)
            binding.amountEditText.setSelection(nominal.length)
        }

        binding.gridAmountChip.adapter = adapter
        binding.amountEditText.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s.isNullOrEmpty()) binding.btnTopUpConfirm.visibility = GONE

                isEditing = true
                try {
                    val cleanText = s.toString().replace("[^\\d]".toRegex(), "") // Hanya ambil angka
                    val nominalValue = cleanText.toIntOrNull() ?: 0

                    val formattedText = NumberFormat.getInstance(
                        Locale.getDefault()).format(nominalValue)

                    if(nominalValue != 0 && nominalValue > 1000)
                        binding.btnTopUpConfirm.visibility = VISIBLE
                    else
                        binding.btnTopUpConfirm.visibility = GONE

                    if (s.toString() != formattedText) {
                        binding.amountEditText.setText(formattedText)
                        binding.amountEditText.setSelection(formattedText.length) // Set kursor di akhir teks
                    }

                    topupViewModel.amount = nominalValue
                } catch (e: NumberFormatException) {
                    Log.e("TopUpActivity", "Invalid number format", e)
                }

                isEditing = false
            }
        })

        binding.btnTopUpConfirm.setOnClickListener{
            topupViewModel.topUpAmountWallet()
        }
    }
}


