package dev.antasource.goling.ui.feature.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.networksource.model.ErrorMessage
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.LoginResponse
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(private val loginRepo: AuthenticationRepository) : ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->   }

    fun login(username: String, pass: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            val response = loginRepo.loginProcess(LoginRequest(username = username, password = pass))
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    _accessToken.value = response.body()?.token
                    _message.value = response.body()?.message
                } else {
                   try {
                       val gson = Gson()
                       val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java )
                       _errorMsg.value = error.message
                   } catch (e: IOException){
                       _errorMsg.value = e.message
                   }
                }

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}