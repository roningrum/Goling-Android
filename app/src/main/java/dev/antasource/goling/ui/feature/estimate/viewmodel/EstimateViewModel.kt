package dev.antasource.goling.ui.feature.estimate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.country.Districs
import dev.antasource.goling.data.model.country.Regencies
import dev.antasource.goling.data.model.country.Region
import dev.antasource.goling.data.model.country.Villages
import dev.antasource.goling.data.model.estimate.Data
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
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

    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data> = _data

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    var provinceId = 0
    var regenciesId = 0
    var districtId = 0

    var oriProvinceId = 0
    var originRegenciesId = 0
    var originDistrictId =0
    var oriVillageId = 0

    var destinateProvinceId = 0
    var destinateRegenciesId = 0
    var destinationDistrictId = 0
    var destinationVillagesId = 0

    var weight = 0
    var lenght = 0
    var width = 0
    var height = 0

    var isGuarantee = false


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

    //    estimate
    fun getEstimateShipping() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
                val estimateShipRequest = EstimateShipRequest(
                    oriProvinceId,
                    originRegenciesId,
                    originDistrictId,
                    oriVillageId,
                    "",
                    destinateProvinceId,
                    destinateRegenciesId,
                    destinationDistrictId,
                    destinationVillagesId,
                    "",
                    height,
                    width,
                    lenght,
                    weight,
                    "Elektronik",
                    isGuarantee
                )
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
    }
}