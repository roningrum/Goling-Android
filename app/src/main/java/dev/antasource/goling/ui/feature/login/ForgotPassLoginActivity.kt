package dev.antasource.goling.ui.feature.login

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
import com.google.protobuf.Api
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.databinding.ActivityForgotPassLoginBinding
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.math.log

class ForgotPassLoginActivity : AppCompatActivity() {
    private lateinit var btnResetEmail: MaterialButton
    private lateinit var emailTextInput: TextInputEditText

    private var isLoading = false

    private lateinit var binding: ActivityForgotPassLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        val authDataSource = NetworkRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotPassLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailTextInput = binding.emailInputResetPass
        btnResetEmail = binding.layoutUiButtonRegister.registerButton

        btnResetEmail.text = "Reset Kata Sandi"
        btnResetEmail.isEnabled = false

        emailTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}
            override fun afterTextChanged(s: Editable?) {
                val emailInput = s?.toString()?.trim() ?: ""
                when {
                    emailInput.isEmpty() -> {
                        btnResetEmail.isEnabled = false
                    }

                    else -> {
                        if (isValidEmail(emailInput)) {
                            btnResetEmail.isEnabled = true
                            binding.emailInputLayoutResetPass.isErrorEnabled = false
                            btnResetEmail.setOnClickListener {
                                isLoading = true
                                showLoading(isLoading)
                                loginViewModel.resetPass(emailInput)
                            }
                        } else {
                            binding.emailInputLayoutResetPass.error =
                                "Email harus memiliki format @"
                            binding.emailInputLayoutResetPass.isErrorEnabled = true
                            btnResetEmail.isEnabled = false
                        }
                    }
                }
            }
        })
        observeResetPass()
    }

    private fun observeResetPass() {
        lifecycleScope.launch {
            loginViewModel.resetState.collectLatest { state ->
                when (state) {
                    is ApiResult.Loading -> {
                        if (isLoading) showLoading(true)
                    }
                    is ApiResult.Success -> {
                        val data = state.data
                        data?.let {
                            showLoading(false)
                            showSnackbarSuccess(data.message.toString())
                        }
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
                        showSnackbarError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutUiProgress.loadingOverlay.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    private fun showSnackbarSuccess(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(
            ContextCompat.getColor(
                applicationContext,
                R.color.greenColor
            )
        )
        snackbarMsg.show()
    }

    private fun showSnackbarError(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.redColor))
        snackbarMsg.show()
    }
}


