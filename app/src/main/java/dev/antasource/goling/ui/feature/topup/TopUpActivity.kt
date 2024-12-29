package dev.antasource.goling.ui.feature.topup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.TopUpRepository
import dev.antasource.goling.databinding.ActivityTopUpBinding
import dev.antasource.goling.ui.factory.TopUpViewModelFactory
import dev.antasource.goling.ui.feature.topup.adapter.ChipAmountAdapter
import dev.antasource.goling.ui.feature.topup.viewmodel.TopupViewModel
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.Util

class TopUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopUpBinding
    private var isUserTyping = true
    private var isAmountSelected = false

    private val topupViewModel by viewModels<TopupViewModel>(){
        val networkRemoteSource = NetworkRemoteSource()
        val topUpRepo = TopUpRepository(networkRemoteSource)
        TopUpViewModelFactory(topUpRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top+24, systemBars.right, 0)
            insets
        }

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_back)
        }

//        onBackPrevPage()

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


        val chipAmount = listOf("5000", "10000", "20000", "25000", "30000", "35000", "40000", "45000")
        binding.gridAmountChip.adapter = ChipAmountAdapter(this, chipAmount) { chipText ->
            // Ketika chip dipilih, tampilkan nominal dan aktifkan tombol
            isAmountSelected = true
            val nominal = Util.formatCurrency(chipText.toInt())
            binding.amountEditText.setText(nominal)
            binding.amountEditText.setSelection(nominal.length)
//            checkEnableButton()  // Periksa status tombol
        }


        binding.amountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Memberitahu bahwa pengguna mengetik
                isUserTyping = true
//                checkEnableButton()  // Periksa status tombol ketika teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Tidak perlu melakukan apa-apa di sini
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val nominalText = s.toString().replace(",", "").toInt()
                    if(true){
                        binding.amountEditText.removeTextChangedListener(this)  // Hentikan TextWatcher untuk mencegah loop
                        binding.amountEditText.setText(Util.formatCurrency(nominalText))  // Format ulang
                        binding.amountEditText.setSelection(binding.amountEditText.text?.length ?: 0)
                        binding.amountEditText.addTextChangedListener(this)  // Aktifkan kembali TextWatcher
                    }
                    topupViewModel.amount = nominalText

                } catch (e: NumberFormatException) {
                    // Tangani jika input tidak valid
                    Log.e("TopUpActivity", "Invalid number format", e)
                }
//                checkEnableButton()  // Periksa status tombol ketika input manual
            }
        })

        binding.btnTopUpConfirm.setOnClickListener{
            topupViewModel.topUpAmountWallet()
        }


    }
}


