package dev.antasource.goling.ui.feature.login

import android.os.Bundle
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
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import kotlin.getValue

class ForgotPassLoginActivity : AppCompatActivity() {
    private lateinit var btnResetEmail : MaterialButton
    private lateinit var emailTextInput: TextInputEditText

    private val loginViewModel: LoginViewModel by viewModels {
        val authDataSource = AuthenticationRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailTextInput = findViewById(R.id.email_input_reset_pass)
        btnResetEmail = findViewById(R.id.register_button)


        loginViewModel.message.observe(this){ message ->
            showSnackbarSuccess(message)
        }

        loginViewModel.errorMsg.observe(this){ errorMessage ->
            showSnackbarError(errorMessage)

        }



        btnResetEmail.text = "Reset Kata Sandi"

        btnResetEmail.setOnClickListener{
            loginViewModel.resetPass(emailTextInput.text.toString().trim())
        }

    }

    private fun showSnackbarSuccess(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.greenColor))
        snackbarMsg.show()
    }

    private fun showSnackbarError(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.redColor))
        snackbarMsg.show()
    }


}


