package dev.antasource.goling.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
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
import dev.antasource.goling.ui.SharedPrefUtil
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels {
        val authDataSource = AuthenticationRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }
    private lateinit var usernameAuthentication : TextInputEditText
    private lateinit var passAuthentication : TextInputEditText
    private lateinit var btnLogin: MaterialButton

    private lateinit var btnResetPass : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameAuthentication = findViewById(R.id.username_input_login)
        passAuthentication = findViewById(R.id.password_input_login)
        btnLogin = findViewById(R.id.button_login)
        btnResetPass = findViewById(R.id.reset_pass_button_text)

        btnResetPass.setOnClickListener{
            val intent = Intent(this, ForgotPassLoginActivity::class.java)
            startActivity(intent)
        }

        usernameAuthentication.addTextChangedListener(checkField(usernameAuthentication,passAuthentication))
        passAuthentication.addTextChangedListener(checkField(usernameAuthentication, passAuthentication))

        handleLoginButton(usernameAuthentication, passAuthentication)

        loginViewModel.accessToken.observe(this){ response ->
            response?.let {
                val rootView = findViewById<View>(android.R.id.content)
                val snackbarMsg = Snackbar.make(rootView, "Sukses Login", Snackbar.LENGTH_SHORT)
                snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.greenColor))
                snackbarMsg.show()
                SharedPrefUtil.saveAccessToken(this, response)
            }
        }
        loginViewModel.errorMsg.observe(this){errorMessage ->
            errorMessage.let {
                val rootView = findViewById<View>(android.R.id.content)
                val snackbarMsg = Snackbar.make(rootView, "$errorMessage", Snackbar.LENGTH_SHORT)
                snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.redColor))
                snackbarMsg.show()
            }
        }
        Log.d("Token Gue", "Token ${SharedPrefUtil.getAccessToken(this)}")

    }

    private fun checkField(
        usernameField: TextInputEditText,
        passwordField: TextInputEditText
    ): TextWatcher {
        return object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {
                handleLoginButton(usernameField, passwordField)
            }

        }
    }

    private fun handleLoginButton(
        username: TextInputEditText,
        password: TextInputEditText
    ) {

        val usernameInput = username.text.toString().trim()
        val passInput = password.text.toString().trim()

        btnLogin.isEnabled = usernameInput.isNotEmpty() && passInput.isNotEmpty()

        btnLogin.setOnClickListener{
            loginViewModel.login(usernameInput, passInput)
        }
    }

}