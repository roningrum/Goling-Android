package dev.antasource.goling.ui.feature.walkthrough

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.login.LoginActivity
import dev.antasource.goling.ui.feature.register.RegisterActivity
import dev.antasource.goling.ui.feature.register.RegisterPhonectivity

class WalkthroughActivity : AppCompatActivity() {
    private lateinit var btnRegisterPage: MaterialButton
    private lateinit var btnLoginPage: MaterialButton

    private lateinit var onboardingViewPager : ViewPager2
    private lateinit var onboardingTabLayout : TabLayout

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

        onboardingTabLayout = findViewById(R.id.tab_page_onboarding)
        onboardingViewPager = findViewById(R.id.onboarding_page_viewpager)

        val adapter = OnboardingAdapter(this)
        onboardingViewPager.adapter = adapter

        TabLayoutMediator(onboardingTabLayout, onboardingViewPager){tab, position ->
            Log.d("Tab", "$tab, $position")
        }.attach()

        btnLoginPage.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
//            finish()
        }
        btnRegisterPage.setOnClickListener{
            val intent = Intent(this, RegisterPhonectivity::class.java)
            startActivity(intent)
//            finish()
        }


    }
}