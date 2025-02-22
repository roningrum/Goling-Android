package dev.antasource.goling.ui.feature.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.databinding.ActivityLoginBinding
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.home.HomeActivity
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import dev.antasource.goling.util.SharedPrefUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels {
        val authDataSource = NetworkRemoteSource()
        val repo = AuthenticationRepository(authDataSource)
        AuthViewModelFactory(repo)
    }

    private lateinit var binding: ActivityLoginBinding

    private var isLoggingIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        observeLoginState()
    }

    private fun setupUI() {
        with(binding) {
            val usernameAuthentication = formLogin.usernameInputLogin
            val passAuthentication = formLogin.passwordInputLogin
            val btnLogin = layoutUiButtonLogin.buttonLogin
            val btnResetPass = formLogin.resetPassButtonText

            btnResetPass.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPassLoginActivity::class.java))
            }

            btnLogin.isEnabled = false

            usernameAuthentication.addTextChangedListener(checkField(usernameAuthentication, passAuthentication))
            passAuthentication.addTextChangedListener(checkField(usernameAuthentication, passAuthentication))

            btnLogin.setOnClickListener {
                showLoading(true)
                loginViewModel.loginUser(
                    usernameAuthentication.text.toString().trim(),
                    passAuthentication.text.toString().trim()
                )
                hideKeyboard(passAuthentication)
            }
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch{
            loginViewModel.loginState.collectLatest { state ->
                when (state) {
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        showLoading(false)
                        state.data?.let { data ->
                            isLoggingIn = true
                            SharedPrefUtil.saveAccessToken(this@LoginActivity, data.token)
                            SharedPrefUtil.saveSessionLogin(this@LoginActivity, isLoggingIn)
                            showMessage(data.message)
                        }
                        delay(3000)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
                        showSnackbar(state.message, R.color.redColor)
                    }
                }
            }
        }
    }

    private fun checkField(
        usernameField: TextInputEditText,
        passwordField: TextInputEditText
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val usernameInput = usernameField.text.toString().trim()
                val passInput = passwordField.text.toString().trim()
                binding.layoutUiButtonLogin.buttonLogin.isEnabled = usernameInput.isNotEmpty() && passInput.isNotEmpty()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutUiProgress.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        showSnackbar(message, R.color.greenColor)
    }

    private fun showSnackbar(message: String, colorRes: Int) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(applicationContext, colorRes))
            .show()
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            ContextCompat.getSystemService(editText.context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}
