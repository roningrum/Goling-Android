package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.register.viewmodel.RegisterViewModel
import kotlin.getValue

class RegisterPhonectivity : AppCompatActivity() {
    private lateinit var btnRegister : MaterialButton
    private lateinit var phoneNumberEditText : TextInputEditText

    private val registrasiViewModel : RegisterViewModel by viewModels{
        val authDataSource = NetworkRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_phonectivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        phoneNumberEditText = findViewById(R.id.phone_input_login)

        btnRegister = findViewById(R.id.register_button)
        btnRegister.text = getString(R.string.next_register_button_text)
        btnRegister.setOnClickListener{
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            Log.d("Phone Number", "Number $phoneNumber")
//            registrasiViewModel.phoneNumber = phoneNumber

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}