package dev.antasource.goling.ui.feature.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.antasource.goling.R
import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.HomeRepositoryRepository
import dev.antasource.goling.ui.factory.MainViewModelFactory
import dev.antasource.goling.ui.feature.home.fragment.HistoryFragment
import dev.antasource.goling.ui.feature.home.fragment.HomeFragment
import dev.antasource.goling.ui.feature.home.fragment.NotificationFragment
import dev.antasource.goling.ui.feature.home.fragment.ProfileFragment
import dev.antasource.goling.ui.feature.home.viewmodel.HomeViewModel
import dev.antasource.goling.ui.feature.scan.ScanBarcodeActivity
import dev.antasource.goling.util.SharedPrefUtil
import dev.antasource.goling.util.permission.PermissionUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var scannerButton: FloatingActionButton
    private lateinit var permissionUtil: PermissionUtil
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val homeViewModel by viewModels<HomeViewModel> {
        val dataSource = NetworkRemoteSource()
        val repo = HomeRepositoryRepository(dataSource)
        MainViewModelFactory(repo)
    }

    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Enable Edge-to-Edge dan atur insets agar tampilan tidak tertutup system bar
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup permission dan jika permission tersedia, kirim lokasi
        setupPermission()

        // Load fragment awal (HomeFragment)
        loadFragment(HomeFragment())

        // Inisialisasi Bottom Navigation
        bottomNav = findViewById(R.id.bottom_nav_menu)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> { loadFragment(HomeFragment()); true }
                R.id.history -> { loadFragment(HistoryFragment()); true }
                R.id.notification -> { loadFragment(NotificationFragment()); true }
                R.id.profile -> { loadFragment(ProfileFragment()); true }
                else -> false
            }
        }

        val token = SharedPrefUtil.getAccessToken(this).toString()
        homeViewModel.token = token

        // Setup tombol scanner
        scannerButton = findViewById(R.id.fab_scanner)
        scannerButton.setOnClickListener { launchScreen() }

        // Cek status login
        isLogin = SharedPrefUtil.getSessionLogin(this)

        // Setup back press handler sesuai versi Android
        if (isLogin) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT
                ) {
                    finish()
                }
            } else {
                onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish()
                    }
                })
            }
        }

        // Tampilkan pesan transaksi jika ada
        showTransactionMessage()
    }

    private fun setupPermission() {
        permissionUtil = PermissionUtil(
            caller = this,
            context = this,
            activity = this,
            shouldShowPermissionRationale = { permission ->
                shouldShowRequestPermissionRationale(permission)
            }
        )

        // Cek permission lokasi dan notifikasi menggunakan fungsi helper dari PermissionUtil
        permissionUtil.checkAllPermissions { locationGranted, notificationGranted ->
            if (locationGranted && notificationGranted) {
                sendLocation()
            } else {
                openAppSettings()
            }
        }
    }

    private fun sendLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission", "Location permissions are not granted. Aborting sendLocation().")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    homeViewModel.updateLocation(
                        location = LocationRequest(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                } ?: run {
                    Log.e("Location", "Location is not available.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Location", "Failed to retrieve location", exception)
            }
        lifecycleScope.launch{
            homeViewModel.locationUpdateResponse.collect { state ->
                when(state){
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        Log.d("Message Lokasi", "${state.data?.message}")
                    }
                    is ApiResult.Error ->{}
                }
            }
        }

    }
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
    private fun showTransactionMessage() {
        intent.getStringExtra("TRANSACTION_MESSAGE")?.let { message ->
            showSnackbarSuccess(message)
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_fragment, fragment)
            .commit()
    }
    private fun launchScreen() {
        val options = ScanOptions().apply {
            setOrientationLocked(false)
            setCaptureActivity(ScanBarcodeActivity::class.java)
            setCameraId(0)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
            setDesiredBarcodeFormats(listOf(ScanOptions.CODE_128, ScanOptions.EAN_13, ScanOptions.UPC_A))
        }
        barcodeLauncher.launch(options)
    }
    private fun showSnackbarSuccess(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.greenColor))
            .show()
    }
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        result.contents?.let { scannedResult ->
            Toast.makeText(this, "Scan Result: $scannedResult", Toast.LENGTH_SHORT).show()
        }
    }
}
