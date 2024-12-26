package dev.antasource.goling.ui.feature.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.home.fragment.HistoryFragment
import dev.antasource.goling.ui.feature.home.fragment.HomeFragment
import dev.antasource.goling.ui.feature.home.fragment.NotificationFragment
import dev.antasource.goling.ui.feature.home.fragment.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
        
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_nav_menu)
        bottomNav.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.home ->{
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_fragment)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                        insets
                    }
                    loadFragment(HomeFragment())
                    true
                }
                R.id.history ->{
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_fragment)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                        insets
                    }
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.notification ->{
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_fragment)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                        insets
                    }
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.profile->{
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_fragment)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, 0, systemBars.right, 0)
                        insets
                    }
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }


        }

    }

    private fun loadFragment(fragment: Fragment) {
        if(true){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.view_fragment, fragment)
            transaction.commit()
        }
    }
}