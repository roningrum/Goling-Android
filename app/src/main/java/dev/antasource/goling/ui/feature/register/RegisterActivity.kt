package dev.antasource.goling.ui.feature.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import dev.antasource.goling.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnNextRegister : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnNextRegister = findViewById(R.id.register_button)
        btnNextRegister.text = "Next"
        btnNextRegister.setOnClickListener{
            val intent = Intent(this, RegisterPhonectivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}