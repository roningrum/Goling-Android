package dev.antasource.goling.ui.feature.pickup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityItemDetailPackageBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel

class ItemDetailPackageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailPackageBinding
    private lateinit var cameraActivityLauncher: ActivityResultLauncher<Intent>
    private val pickupViewModel: PickupViewModel by viewModels(){
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Register the ActivityResultLauncher for the camera activity
        cameraActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Get the image URI from the result data
                val imageUri: Uri? = result.data?.getStringExtra("IMAGE_URI")?.let { Uri.parse(it) }
                imageUri?.let {
                    pickupViewModel.setImageUri(it) // Save the URI in the ViewModel
                    displayImage(it) // Display the image
                }
            }
        }

        // Set up the click listener to launch the camera activity
        binding.layoutItemDetailPackage.imgPhotoCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraActivityLauncher.launch(intent)
        }
    }

    private fun displayImage(uri: Uri) {
        Log.d("Uri Paths", "Paths Foto ${uri.path}")
        binding.layoutItemDetailPackage.imgPhotoCamera.visibility = View.GONE // Hide the camera icon
        binding.layoutItemDetailPackage.imgPreview.setImageURI(uri) // Set the image URI to the ImageView
        binding.layoutItemDetailPackage.imgPreview.visibility = View.VISIBLE // Make sure the preview is visible
    }
}
