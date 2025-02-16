package dev.antasource.goling.ui.feature.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.home.fragment.HistoryFragment
import dev.antasource.goling.ui.feature.home.fragment.HomeFragment
import dev.antasource.goling.ui.feature.home.fragment.NotificationFragment
import dev.antasource.goling.ui.feature.home.fragment.ProfileFragment
import dev.antasource.goling.ui.feature.scan.ScanBarcodeActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var scannerButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_nav_menu)

        showTransactionMessage()
        scannerButton = findViewById(R.id.fab_scanner)
        scannerButton.setOnClickListener {
            launchScreen()
        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.history -> {
                    loadFragment(HistoryFragment())
                    true
                }

                R.id.notification -> {
                    loadFragment(NotificationFragment())
                    true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun showTransactionMessage() {
        val message = intent.getStringExtra("TRANSACTION_MESSAGE")
        message?.let {
            showSnackbarSuccess(message)
        }?:""
    }

    private fun loadFragment(fragment: Fragment) {
        if (true) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.view_fragment, fragment)
            transaction.commit()
        }
    }

    private fun launchScreen() {
        val option = ScanOptions()
            .setOrientationLocked(false)
            .setCaptureActivity(ScanBarcodeActivity::class.java)
            .setCameraId(0)
            .setBeepEnabled(false)
            .setBarcodeImageEnabled(true)
            .setDesiredBarcodeFormats( // Tentukan format barcode
                listOf(
                    ScanOptions.CODE_128, // Untuk barcode seperti yang Anda unggah
                    ScanOptions.EAN_13,  // Format barcode EAN-13
                    ScanOptions.UPC_A   // Format barcode UPC-A
                )
            )
        barcodeLauncher.launch(option)
    }

    private fun showSnackbarSuccess(message: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbarMsg = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbarMsg.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.greenColor))
        snackbarMsg.show()
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (!result.contents.isNullOrEmpty()) {
            Toast.makeText(this, "Scan Result: ${result.contents}", Toast.LENGTH_SHORT).show()
        } else {
            // CANCELED
        }
    }
}


