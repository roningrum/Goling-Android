package dev.antasource.goling.ui.feature.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.AuthenticationRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.register.viewmodel.RegisterViewModel
import kotlin.getValue

class RegisterPhonectivity : AppCompatActivity() {
    private lateinit var btnRegister : MaterialButton
    private lateinit var phoneNumberEditText : TextInputEditText
    private var username:String = ""
    private var password : String = ""
    private var email : String = ""

    private val registrasiViewModel : RegisterViewModel by viewModels{
        val authDataSource = AuthenticationRemoteSource()
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

        val bundle : Bundle? = intent.extras

        bundle?.let {
            bundle.apply {
                username = getString("username").toString()
                email = getString("email").toString()
                password = getString("password").toString()
            }
        }

        btnRegister = findViewById(R.id.register_button)
        btnRegister.setOnClickListener{
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            Log.d("Username", "username $phoneNumber")
            registrasiViewModel.register(username, email, password, phoneNumber)
        }
        registrasiViewModel.message.observe(this){message ->
//            Log.d("Pesan Register", "Register $message")
            val rootView = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.greenColor))
            snackbar.show()
        }

        registrasiViewModel.errorMessage.observe(this){ error ->
            val rootView = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(rootView, error, Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.redColor))
            snackbar.show()
        }



    }
}