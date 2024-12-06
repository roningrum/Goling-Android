package dev.antasource.goling.ui.feature.walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.ui.feature.register.RegisterActivity

class WalkthroughActivity : AppCompatActivity() {
    private lateinit var btnRegisterPage: MaterialButton
    private lateinit var btnLoginPage: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_walkthrough)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        btnLoginPage = findViewById(R.id.button_login_page)
        btnRegisterPage = findViewById(R.id.button_sign_up_page)

        btnLoginPage.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
//            finish()
        }
        btnRegisterPage.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
//            finish()
        }


    }
}