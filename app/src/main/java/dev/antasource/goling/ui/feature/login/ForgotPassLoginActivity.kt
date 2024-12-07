package dev.antasource.goling.ui.feature.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dev.antasource.goling.R

class ForgotPassLoginActivity : AppCompatActivity() {
    private lateinit var btnResetEmail : MaterialButton
    private lateinit var emailTextInput: TextInputEditText


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

        btnResetEmail.text = "Reset Kata Sandi"



    }
}