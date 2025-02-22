package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dev.antasource.goling.R
import dev.antasource.goling.databinding.ActivityRegisterPhonectivityBinding

class RegisterPhonectivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterPhonectivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterPhonectivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
 // Mengatur padding sesuai system insets (status bar, navigation bar, dsb)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneNumberEditText: TextInputEditText = binding.phoneInputLogin
        val btnRegister: MaterialButton = binding.layoutUiRegisterButtonPhone.registerButton

        btnRegister.text = getString(R.string.next_register_button_text)
        btnRegister.isEnabled = false

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val phoneText = s?.toString()?.trim() ?: ""
                when {
                    phoneText.isEmpty() -> {
                        btnRegister.isEnabled = false
                    }
                    phoneText.length < 12 -> {
                        binding.phoneInputLayoutLogin.error = "Nomor Telepon maksimal 11 atau 12 karakter"
                        btnRegister.isEnabled = false
                    }
                    phoneText.length > 12 ->{
                        binding.phoneInputLayoutLogin.error = "Nomor Telepon maksimal 11 atau 12 karakter"
                        btnRegister.isEnabled = false
                    }
                    else -> {
                        binding.phoneInputLayoutLogin.isErrorEnabled = false
                        btnRegister.isEnabled = true
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRegister.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text?.toString()?.trim() ?: ""
            Log.d("Phone Number", "Number $phoneNumber")
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("PHONE_NUMBER", phoneNumber)
            startActivity(intent)
            finish()
        }
    }
}
