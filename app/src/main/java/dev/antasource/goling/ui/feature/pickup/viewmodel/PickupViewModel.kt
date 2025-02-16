package dev.antasource.goling.ui.feature.pickup.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.local.PickupDataPref
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.country.Districs
import dev.antasource.goling.data.model.country.Regencies
import dev.antasource.goling.data.model.country.Region
import dev.antasource.goling.data.model.country.Villages
import dev.antasource.goling.data.model.estimate.Data
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import dev.antasource.goling.data.model.product.ProductType
import dev.antasource.goling.data.model.product.ProductTypeIdResponse
import dev.antasource.goling.data.model.topup.Balance
import dev.antasource.goling.data.repositoty.ShippingRepository
import kotlinx.coroutines.launch
import okio.IOException

class PickupViewModel(private val repository: ShippingRepository): ViewModel() {

    private val _productType = MutableLiveData<List<ProductType>>()
    val productType : LiveData<List<ProductType>> = _productType

    private val _region = MutableLiveData<List<Region>>()
    val region: LiveData<List<Region>> = _region

    private val _regencies = MutableLiveData<List<Regencies>>()
    val regencies: LiveData<List<Regencies>> = _regencies

    private val _distric = MutableLiveData<List<Districs>>()
    val districs: LiveData<List<Districs>> = _distric

    private val _villages = MutableLiveData<List<Villages>>()
    val villages: LiveData<List<Villages>> = _villages

    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data> = _data

    private val _errorMsg = MutableLiveData<String>()
    val errorMessage  : LiveData<String> = _errorMsg

    private val _sender = MutableLiveData<OriginSender>()
    val sender : LiveData<OriginSender> = _sender

    private val _receipt = MutableLiveData<DestinationReceipt>()
    val receipt : LiveData<DestinationReceipt> = _receipt

    private val _productName = MutableLiveData<ProductTypeIdResponse>()
    val productName : LiveData<ProductTypeIdResponse> = _productName


    private val _orderResponse = MutableLiveData<OrderResponse>()
    val orderResponse : LiveData<OrderResponse> = _orderResponse


    private val _balance = MutableLiveData<Balance>()
    val balance : LiveData<Balance> = _balance


    private var _imageUri : Uri? = null

    var provinceId = 0
    var regenciesId = 0
    var districtId = 0

    var token: String = ""


    fun setImageUri(uri: Uri){
        _imageUri = uri
    }

    fun getImageUri(): Uri? {
        return _imageUri
    }

    fun saveOriginDataToLocal(context: Context, originSender: OriginSender){
        PickupDataPref(context = context).saveOriginOrder(context, originSender)
    }

    fun saveDestinationToLocal(context: Context, destinationReceipt: DestinationReceipt){
        PickupDataPref(context = context).saveDestinationOrder(context, destinationReceipt)
    }

    fun getOriginData(context: Context){
        val data = PickupDataPref(context).getLatestOriginOrder()
        data.let {
            _sender.postValue(it)
        }

    }

    fun getReceiptData(context: Context){
        val data = PickupDataPref(context).getLatestDestinationReceipt()
        data.let {
            _receipt.postValue(it)
        }

    }

    fun clearOrderData(context: Context){
        PickupDataPref(context).clearData()
    }

    fun getProductType(){
        viewModelScope.launch{
            val response = repository.getProductType()
            if(response.isSuccessful){
                _productType.value = response.body()?.productTypes
            }
        }
    }


    fun getproductTypeById(id:Int){
        viewModelScope.launch{
            val response = repository.getProductTypeById(id)
            if (response.isSuccessful) {
                _productName.value = response.body()
                Log.d("View model", "Response ${response.body()}")
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

    fun getBallance(){
        viewModelScope.launch{
            val response = repository.getBalance(token)
            if(response.isSuccessful){
                _balance.value = response.body()
            }else{
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

    fun getEstimatePrice(estimateShipRequest: EstimateShipRequest){
        viewModelScope.launch{
            val response = repository.getEstimateShipping(estimateShipRequest)
            if (response.isSuccessful) {
                _data.value = response.body()?.data
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }

            }
        }

    }

    fun sendOrder(orderRequest: OrderRequest){
        viewModelScope.launch{
            val response = repository.createOrder(token, orderRequest)
            if(response.isSuccessful) {
                _orderResponse.value = response.body()
            } else{
                try {
                    val gson = Gson()
                    val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

    fun getRegion() {
        viewModelScope.launch{
            val response = repository.getRegion()
            if (response.isSuccessful) {
                _region.value = response.body()
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }

        }
    }

    fun getRegionName(id: String) = _region.value?.find { it.id == id }?.name


    fun getRegenciesId() {
        viewModelScope.launch{
            val response = repository.getCityRegencies(provinceId)
            if (response.isSuccessful) {
                _regencies.value = response.body()
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

    fun getRegenciesName(regenciesId: String)= _regencies.value?.find { it.id == regenciesId }?.name

    fun getDistric() {
        viewModelScope.launch{
            val response = repository.getDistrics(regenciesId)
            if (response.isSuccessful) {
                _distric.value = response.body()
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }
            }
        }
    }

    fun getDistricName() = _distric.value?.find { it.id == districtId.toString() }?.name

    fun getVillages() {
        viewModelScope.launch{
            val response = repository.getVillages(districtId)
            if (response.isSuccessful) {
                _villages.value = response.body()
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMsg.value = error.message
                } catch (e: IOException) {
                    _errorMsg.value = e.message
                }

            }
        }
    }

    fun getVillageName(villagesId: String) = _villages.value?.find { it.id == villagesId}?.name
}