package dev.antasource.goling.ui.feature.scan

import android.Manifest
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dev.antasource.goling.databinding.ActivityScanBarcodeBinding
import dev.antasource.goling.util.permission.PermissionManager

class ScanBarcodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBarcodeBinding
    private lateinit var capture: CaptureManager
    private lateinit var scan: DecoratedBarcodeView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scan = binding.scannerCamera

        val permission = PermissionManager(this)
        permission.checkPermissions(
            Manifest.permission.CAMERA
        ){ isGranted ->
            if(isGranted){
                initializeQrScanner(savedInstanceState)
            }
            else{
                //
            }
        }


    }

    private fun initializeQrScanner(savedInstanceState: Bundle?) = with(binding) {
        capture = CaptureManager(this@ScanBarcodeActivity, scan)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()
    }

    override fun onResume() {
        super.onResume()
       scan.resume()
    }

    override fun onPause() {
        super.onPause()
        scan.pause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return scan.onKeyDown(keyCode, event)|| super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}