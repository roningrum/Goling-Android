package dev.antasource.goling.ui.feature.pickup.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.model.product.ProductType
import dev.antasource.goling.data.repositoty.ShippingRepository
import kotlinx.coroutines.launch

class PickupViewModel(private val repository: ShippingRepository): ViewModel() {
    private val _productType = MutableLiveData<List<ProductType>>()
    val productType : LiveData<List<ProductType>> = _productType


    private var _imageUri : Uri? = null

    fun setImageUri(uri: Uri){
        _imageUri = uri
    }

    fun getImageUri(): Uri? {
        return _imageUri
    }


    fun getProductType(){
        viewModelScope.launch{
            val response = repository.getProductType()
            if(response.isSuccessful){
                _productType.value = response.body()?.productTypes
            }
        }
    }

}