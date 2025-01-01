package dev.antasource.goling.ui.feature.estimate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.country.Districs
import dev.antasource.goling.data.model.country.LocationDeliver
import dev.antasource.goling.data.model.country.Regencies
import dev.antasource.goling.data.model.country.Region
import dev.antasource.goling.data.model.country.Villages
import dev.antasource.goling.data.repositoty.ShippingRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class EstimateViewModel(private val repository: ShippingRepository) : ViewModel() {

    private val _region = MutableLiveData<List<Region>>()
    val region: LiveData<List<Region>> = _region

    private val _regencies = MutableLiveData<List<Regencies>>()
    val regencies: LiveData<List<Regencies>> = _regencies

    private val _distric = MutableLiveData<List<Districs>>()
    val districs: LiveData<List<Districs>> = _distric

    private val _villages = MutableLiveData<List<Villages>>()
    val villages: LiveData<List<Villages>> = _villages

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    val _originLocation = MutableLiveData<LocationDeliver>()
    val originLocation : LiveData<LocationDeliver> = _originLocation

    val _destinateLocation = MutableLiveData<LocationDeliver>()
    val destinateLocation : LiveData<LocationDeliver> = _destinateLocation

    var provinceId = 0
    var regenciesId = 0
    var districtId = 0

    var originProvinceId = ""
    var originCityId = ""
    var originDistricId = ""
    var originVillagesId = ""

    var destinationProvinceId = ""
    var destinationCityId = ""
    var destinationDistricId = ""
    var destinationVillagesId = ""

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }

    fun getRegion() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
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
    }

    fun getRegenciesId() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
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
    }

    fun getDistric() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
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
    }

    fun getVillages() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
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
    }
}