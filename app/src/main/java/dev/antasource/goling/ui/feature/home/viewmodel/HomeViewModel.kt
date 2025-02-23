package dev.antasource.goling.ui.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.UserResponse
import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.model.location.LocationUpdateResponse
import dev.antasource.goling.data.model.topup.Balance
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.repositoty.HomeRepositoryRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class HomeViewModel(private val homeRepository: HomeRepositoryRepository): ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _balance = MutableStateFlow<ApiResult<Balance>>(ApiResult.Loading)
    val balance : StateFlow<ApiResult<Balance>> = _balance

    private val _locationUpdateResponse = MutableStateFlow<ApiResult<LocationUpdateResponse>>(
        ApiResult.Loading)
    val locationUpdateResponse : StateFlow<ApiResult<LocationUpdateResponse>> = _locationUpdateResponse

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg


    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }

    var token: String = ""

    fun getUser(token: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = homeRepository.getUserProfile(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _userResponse.value = response.body()
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

    fun updateLocation(location: LocationRequest){
        viewModelScope.launch{
          homeRepository.postLocation(token, location)
              .onStart { _locationUpdateResponse.value = ApiResult.Loading }
              .catch { _locationUpdateResponse.value = ApiResult.Error(it.message ?: "") }
              .collect{ data -> _locationUpdateResponse.value = data }
        }
    }

    fun getBalance(token: String){
        viewModelScope.launch{
            homeRepository.getWalletBallance(token)
                .onStart{_balance.value = ApiResult.Loading }
                .catch { _balance.value = ApiResult.Error(it.message ?: "") }
                .collect{ data -> _balance.value = data}
        }
    }

}