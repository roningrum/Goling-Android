package dev.antasource.goling.ui.feature.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepository
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.feature.home.fragment.HistoryFragment
import dev.antasource.goling.ui.feature.home.fragment.HomeFragment
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        bottomNav = findViewById(R.id.bottom_nav_menu)

        bottomNav.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.home ->{
                    HomeFragment()
                    true
                }
                R.id.history ->{
                    HistoryFragment()
                    true
                }
                R.id.profile->{
                    HomeFragment()
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
}