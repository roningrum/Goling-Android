package dev.antasource.goling.ui.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.UserResponse
import dev.antasource.goling.data.model.topup.Balance
import dev.antasource.goling.data.repositoty.HomeRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _balance = MutableLiveData<Balance>()
    val balance : LiveData<Balance> = _balance

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

    fun getBalance(token: String){
        viewModelScope.launch{
            val response = homeRepository.getBalance(token)
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

}