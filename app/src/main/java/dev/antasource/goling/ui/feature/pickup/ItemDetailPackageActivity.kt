package dev.antasource.goling.ui.feature.pickup

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dev.antasource.goling.R
import dev.antasource.goling.data.model.pickup.request.AdditionalDetails
import dev.antasource.goling.data.model.pickup.request.PackageDetails
import dev.antasource.goling.data.model.product.ProductType
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.ShippingRepository
import dev.antasource.goling.databinding.ActivityItemDetailPackageBinding
import dev.antasource.goling.databinding.FilterOtherBottomBinding
import dev.antasource.goling.ui.factory.ShippingViewModelFactory
import dev.antasource.goling.ui.feature.pickup.viewmodel.PickupViewModel
import java.io.File
import java.io.FileOutputStream

class ItemDetailPackageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailPackageBinding
    private lateinit var cameraActivityLauncher: ActivityResultLauncher<Intent>
    private val pickupViewModel: PickupViewModel by viewModels() {
        val data = NetworkRemoteSource()
        val repo = ShippingRepository(data)
        ShippingViewModelFactory(repo)
    }

    private var productTypeId = 0
    private var productTypeName = ""

    private var weight = 0
    private var length = 0
    private var height = 0
    private var width = 0
    private var path = ""

    private var isGlassWare = false
    private var isAssurance = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showProductType()
        checkFilledForm()
        setupTextWatchers()

        cameraActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri: Uri? = result.data?.getStringExtra("IMAGE_URI")?.let { Uri.parse(it) }
                imageUri?.let {
                    saveImageToExternalStorage(it)
                    displayImage(it) // Display the image
                }
            }
        }

        binding.layoutItemDetailPackage.buttonCheckedInsurance.setOnCheckedChangeListener { v, isChecked ->
            isAssurance = isChecked
        }

        binding.layoutItemDetailPackage.buttonCheckedGlassware.setOnCheckedChangeListener { v, isChecked ->
            isGlassWare = isChecked
        }

        binding.layoutItemDetailPackage.imgPhotoCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraActivityLauncher.launch(intent)
        }

        binding.layoutItemDetailPackage.buttonSavePackageInfo.setOnClickListener {
            directToPickupForm()
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkFilledForm()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkFilledForm()
            }
        }
        binding.layoutItemDetailPackage.inputItemWeightEdit.addTextChangedListener(textWatcher)
        binding.layoutItemDetailPackage.inputWidthEdit.addTextChangedListener(textWatcher)
        binding.layoutItemDetailPackage.inputHeightEdit.addTextChangedListener(textWatcher)
        binding.layoutItemDetailPackage.inputLengthEdit.addTextChangedListener(textWatcher)
    }

    private fun checkFilledForm() {
        val weightText = binding.layoutItemDetailPackage.inputItemWeightEdit.editableText.toString()
        val widthText = binding.layoutItemDetailPackage.inputWidthEdit.editableText.toString()
        val lengthText = binding.layoutItemDetailPackage.inputLengthEdit.editableText.toString()
        val heightText = binding.layoutItemDetailPackage.inputHeightEdit.editableText.toString()

        weight = if (weightText.isBlank()) 0 else weightText.toInt()
        width = if (widthText.isBlank()) 0 else widthText.toInt()
        length = if (lengthText.isBlank()) 0 else lengthText.toInt()
        height = if (heightText.isBlank()) 0 else heightText.toInt()

        val isFilled = weight != 0 && width != 0 && length != 0 && height != 0

        binding.layoutItemDetailPackage.buttonSavePackageInfo.apply {
            isEnabled = isFilled
            setBackgroundColor(
                ContextCompat.getColor(
                    this@ItemDetailPackageActivity,
                    if (isFilled) R.color.darkBlue else R.color.grayBorder
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    this@ItemDetailPackageActivity,
                    if (isFilled) R.color.backgroundColor else R.color.grayColor
                )
            )
        }
    }

    private fun showProductType() {
        pickupViewModel.productType.observe(this) { productType ->
            binding.layoutItemDetailPackage.chipGroup.removeAllViews()
            val visibleChips = productType.take(5)
            val remainingChips = productType.drop(5)

            // Menampilkan chip untuk produk utama
            visibleChips.forEach { product ->
                val chip = Chip(this).apply {
                    text = product.name
                    isCheckable = true
                    tag = product.id
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.backgroundColor
                        )
                    )
                    chipStrokeColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grayColor))
                    chipStrokeWidth = 1f

                    setOnClickListener {
                        if (isChecked) {
                            chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.darkBlue
                                )
                            )
                            setTextColor(ContextCompat.getColor(context, R.color.white))
                        } else {
                            chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.backgroundColor
                                )
                            )
                            chipStrokeColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.grayColor
                                )
                            )
                            chipStrokeWidth = 1f
                        }

                        productTypeId = tag as Int
                        productTypeName = text as String

                        binding.layoutItemDetailPackage.chipGroup.children.forEach { child ->
                            if (child is Chip && child != this) {
                                child.isChecked = false
                                child.setChipBackgroundColorResource(R.color.backgroundColor)
                                child.setTextColor(ContextCompat.getColor(context, R.color.black))
                            }
                        }
                    }
                }
                binding.layoutItemDetailPackage.chipGroup.addView(chip)
            }

            if (remainingChips.isNotEmpty()) {
                val othersChip = Chip(this).apply {
                    text = "Lainnya"
                    isCheckable = true
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.backgroundColor
                        )
                    )
                    chipStrokeColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grayColor))
                    chipStrokeWidth = 1f
                }

                othersChip.setOnClickListener {
                    showOtherFilterDialog(remainingChips, othersChip){ isOtherSelected ->
                        if(isOtherSelected){
                            othersChip.chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this,
                                    R.color.darkBlue
                                )
                            )
                            othersChip.setTextColor(ContextCompat.getColor( this, R.color.white))
                        } else {
                            othersChip.chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this,
                                    R.color.backgroundColor
                                )
                            )
                            othersChip.chipStrokeColor = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                   this,
                                    R.color.grayColor
                                )
                            )
                            othersChip.chipStrokeWidth = 1f
                        }
                    }
                }

                binding.layoutItemDetailPackage.chipGroup.addView(othersChip)
            }
        }
        pickupViewModel.getProductType()
    }

    private fun showOtherFilterDialog(types: List<ProductType>, othersChips: Chip,  onOtherSelected: (Boolean) -> Unit ) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = FilterOtherBottomBinding.inflate(layoutInflater)

        types.forEach { product ->
            val chip = Chip(this).apply {
                text = product.name
                isCheckable = true
                chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.backgroundColor))
                chipStrokeColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grayColor))
                chipStrokeWidth = 1f

                setOnClickListener {
                    othersChips.text = product.name
                    othersChips.isChecked = true
                    othersChips.tag = product.id
                    chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.darkBlue))
                    setTextColor(ContextCompat.getColor(context, R.color.white))

                    binding.layoutItemDetailPackage.chipGroup.children.forEach { child ->
                        if (child is Chip && child != this) {
                            child.isChecked = false
                            child.setChipBackgroundColorResource(R.color.backgroundColor)
                            child.setTextColor(ContextCompat.getColor(context, R.color.black))
                        }
                    }
                    productTypeId = othersChips.tag as Int
                    productTypeName = othersChips.text.toString()
                    onOtherSelected(true)
                    bottomSheetDialog.dismiss()
                }
            }
            view.otherChipGroup.addView(
                chip
            )
        }
        bottomSheetDialog.setContentView(view.root)
        bottomSheetDialog.show()
    }

    private fun displayImage(uri: Uri) {
        path = uri.path.toString()
        binding.layoutItemDetailPackage.imgPhotoCamera.visibility =
            View.GONE // Hide the camera icon
        binding.layoutItemDetailPackage.imgPreview.setImageURI(uri) // Set the image URI to the ImageView
        binding.layoutItemDetailPackage.imgPreview.visibility =
            View.VISIBLE // Make sure the preview is visible
    }

    private fun directToPickupForm() {
        val packageInfo = PackageDetails(
            height = height.toFloat(),
            width = width.toFloat(),
            length = length.toFloat(),
            weight = weight.toFloat(),
            productType = productTypeId
        )
        val additionalDetails = AdditionalDetails(
            glassware = isGlassWare,
            isGuaranteed = isAssurance
        )

        val uriPath = path
        Log.d("GlassWare Value","Nilai $isGlassWare")
        Log.d("isGuarantee Value","Nilai $isAssurance")
        val intent = Intent(this, PickupActivity::class.java)
        intent.putExtra("path", uriPath)
        intent.putExtra("productTypeName", productTypeName)
        intent.putExtra("packageInfo", packageInfo)
        intent.putExtra("additionalInfo", additionalDetails)
        startActivity(intent)
    }

    private fun saveImageToExternalStorage(imageUri: Uri): String? {
        val context = this
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            return file.absolutePath // Return path gambar yang disimpan
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}






