package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.AuthenticationRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.register.viewmodel.RegisterViewModel
import kotlin.getValue

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnNextRegister : MaterialButton
    private lateinit var emailInputEditText: TextInputEditText
    private lateinit var userNameInputEditText: TextInputEditText
    private lateinit var passwordInputEditText: TextInputEditText

    private val registrasiViewModel : RegisterViewModel by viewModels{
        val authDataSource = AuthenticationRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInputEditText = findViewById(R.id.email_input_register)
        userNameInputEditText = findViewById(R.id.username_input_register)
        passwordInputEditText = findViewById(R.id.password_input_register)

        btnNextRegister = findViewById(R.id.register_button)
        btnNextRegister.text = "Next"

        handleRegisterInput(userNameInputEditText, userNameInputEditText, passwordInputEditText)

        btnNextRegister.setOnClickListener{
            val username = userNameInputEditText.text.toString().trim()
            val password = passwordInputEditText.text.toString().trim()
            val email = emailInputEditText.text.toString().trim()
            if(isValidEmail(emailInputEditText.toString().trim())){
                registrasiViewModel.register(username, email, password, "")
                val intent = Intent(this, RegisterPhonectivity::class.java)
                startActivity(intent)
                finish()
            } else{
                emailInputEditText.error = "Email harus berformat @"
            }

        }
    }

    private fun handleRegisterInput(
        usernameEditText: TextInputEditText,
        emailEditText: TextInputEditText,
        passwordEditText: TextInputEditText

    ) {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()

        val allInputFilled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
        btnNextRegister.isEnabled = allInputFilled

    }

    private fun isValidEmail(email: String): Boolean {
       val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}