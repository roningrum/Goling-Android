package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.antasource.goling.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnNextRegister : MaterialButton
    private lateinit var emailInputEditText: TextInputEditText
    private lateinit var userNameInputEditText: TextInputEditText
    private lateinit var passwordInputEditText: TextInputEditText
    private lateinit var emailInputTextLayout : TextInputLayout


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
        emailInputTextLayout = findViewById(R.id.email_input_layout_register)
        userNameInputEditText = findViewById(R.id.username_input_register)
        passwordInputEditText = findViewById(R.id.password_input_register)

        btnNextRegister = findViewById(R.id.register_button)
        btnNextRegister.text = "Next"

        handleRegisterInput(userNameInputEditText,emailInputEditText, passwordInputEditText)
        emailInputEditText.addTextChangedListener(checkField(userNameInputEditText, emailInputEditText, passwordInputEditText))
        userNameInputEditText.addTextChangedListener(checkField(userNameInputEditText, emailInputEditText, passwordInputEditText))
        passwordInputEditText.addTextChangedListener(checkField(userNameInputEditText, emailInputEditText, passwordInputEditText))

        intent.data = null
    }


    override fun onResume() {
        super.onResume()
        val messageError = intent.getStringExtra("Pesan Error")?:""
        val successMessage = intent.getStringExtra("Pesan Sukses")?:""

        if(messageError!= ""){
            showErrorMessage(messageError)
        }

        if(successMessage !=""){
            showSuccessMessage(successMessage)
        }

    }

    override fun onPause() {
        super.onPause()
        intent.data = null
    }

    override fun onStop() {
        super.onStop()
        intent.data = null
    }

    private fun showSuccessMessage(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.greenColor))
        snackbar.show()
    }

    private fun showErrorMessage(message:String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.redColor))
        snackbar.show()
    }


    private fun checkField(usernameEditText: TextInputEditText, emailEditText: TextInputEditText, passwordEditText: TextInputEditText) : TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//                checkField(usernameEditText, emailEditText, passwordEditText)
//                btnNextRegister.isEnabled = false

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
                // Email valid, hilangkan pesan kesalahan jika ada
                emailInputTextLayout.isErrorEnabled = false

                // Lakukan proses registrasi, bisa dihubungkan dengan ViewModel atau logika lainnya
//                registrasiViewModel.register(username, email, password, "")

                // Lanjut ke halaman berikutnya (RegisterPhonectivity)

                val intent = Intent(this, RegisterPhonectivity::class.java)
                with(intent){
                    putExtra("username", username)
                    putExtra("email", email)
                    putExtra("password", password)
                }
                startActivity(intent)
            } else {
                // Email tidak valid, tampilkan pesan error
                emailInputTextLayout.isErrorEnabled = true
                emailInputTextLayout.error = "Email harus memiliki format @"
            }
            Log.d("Email", "Email Anda $email ")

        }

    }

    private fun isValidEmail(email: String): Boolean {
       val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


}