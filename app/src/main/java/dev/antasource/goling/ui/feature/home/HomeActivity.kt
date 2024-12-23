package dev.antasource.goling.ui.feature.home

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.feature.home.fragment.HistoryFragment
import dev.antasource.goling.ui.feature.home.fragment.HomeFragment
import dev.antasource.goling.ui.feature.home.fragment.NotificationFragment
import dev.antasource.goling.ui.feature.home.fragment.ProfileFragment
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private val homeViewModel : HomeViewModel by viewModels{
        val data = NetworkRemoteSource()
        val repo = HomeRepository(data)
        MainViewModelFactory(repo)
    }
    private lateinit var token : String
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
                    loadFragment(HomeFragment())
                    true
                }
                R.id.history ->{
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.notification ->{
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.profile->{
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }


        }
        token = SharedPrefUtil.getAccessToken(this).toString()

        homeViewModel.userResponse.observe(this){ data ->
            Log.d("Data User", "Data $data")
        }
        homeViewModel.message.observe(this){ nama ->
            nama?.let {
                Toast.makeText(this, "Welcome $nama", Toast.LENGTH_SHORT).show()
            }

        }
        homeViewModel.errorMsg.observe(this){ error ->
            Log.e("Error Message", "Error $error")
        }
        Log.d("Token User", "Token $token")
        homeViewModel.getUser(token)
    }

    private fun loadFragment(fragment: Fragment) {
        if(true){
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_fragment)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                insets
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.view_fragment, fragment)
            transaction.commit()
        }
    }
}