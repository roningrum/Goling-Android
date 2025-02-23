package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.auth.AuthenticationRepository
import dev.antasource.goling.databinding.ActivityRegisterBinding
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.ui.feature.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnNextRegister: MaterialButton
    private lateinit var emailInputEditText: TextInputEditText
    private lateinit var userNameInputEditText: TextInputEditText
    private lateinit var passwordInputEditText: TextInputEditText
    private lateinit var emailInputTextLayout: TextInputLayout

    private lateinit var binding: ActivityRegisterBinding

    private var isLoading = false

    private val registrasiViewModel: RegisterViewModel by viewModels {
        val authDataSource = NetworkRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBinding()
        observerRegisterState()
    }

    private fun setupBinding() {
        with(binding){
            emailInputEditText = layoutUiRegisterForm.emailInputRegister
            emailInputTextLayout = layoutUiRegisterForm.emailInputLayoutRegister
            userNameInputEditText = layoutUiRegisterForm.usernameInputRegister
            passwordInputEditText = layoutUiRegisterForm.passwordInputRegister

            btnNextRegister = binding.layoutUiButtonRegister.registerButton
            btnNextRegister.text = "Selesai"


            registrasiViewModel.phoneNumber = intent.getStringExtra("PHONE_NUMBER")!!

            handleRegisterInput(userNameInputEditText, emailInputEditText, passwordInputEditText)
            emailInputEditText.addTextChangedListener(
                checkField(
                    userNameInputEditText,
                    emailInputEditText,
                    passwordInputEditText
                )
            )
            userNameInputEditText.addTextChangedListener(
                checkField(
                    userNameInputEditText,
                    emailInputEditText,
                    passwordInputEditText
                )
            )
            passwordInputEditText.addTextChangedListener(
                checkField(
                    userNameInputEditText,
                    emailInputEditText,
                    passwordInputEditText
                )
            )

        }
    }

    private fun observerRegisterState() {
        lifecycleScope.launch {
            registrasiViewModel.registerState.collectLatest { state ->
                when (state) {
                    is ApiResult.Loading -> {
                        if (isLoading){
                            showLoading(true)
                        }
                    }
                    is ApiResult.Success -> {
                        showLoading(false)
                        val data = state.data
                        data?.let { it ->
                            showSuccessMessage(it.message)
                            delay(2000)
                            redirectToLogin()
                        }
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
                        showErrorMessage(state.message)
                    }
                }
            }
        }
    }

    private fun redirectToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun showLoading(isLoading: Boolean) {
       val loading = binding.layoutUiProgress.loadingOverlay
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSuccessMessage(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.greenColor))
        snackbar.show()
    }
    private fun showErrorMessage(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.redColor))
        snackbar.show()
    }


    private fun checkField(
        usernameEditText: TextInputEditText,
        emailEditText: TextInputEditText,
        passwordEditText: TextInputEditText
    ): TextWatcher {
        return object : TextWatcher {
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
                handleRegisterInput(usernameEditText, emailEditText, passwordEditText)
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

        btnNextRegister.setOnClickListener {
            if (isValidEmail(email)) {
                emailInputTextLayout.isErrorEnabled = false
            } else {
                emailInputTextLayout.isErrorEnabled = true
                emailInputTextLayout.error = "Email harus memiliki format @"
            }

            isLoading = true
            showLoading(isLoading)
            registrasiViewModel.register(username, email, password)

        }

    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


}