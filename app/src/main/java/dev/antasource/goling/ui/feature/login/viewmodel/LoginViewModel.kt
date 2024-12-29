package dev.antasource.goling.ui.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class LoginViewModel(private val loginRepo: AuthenticationRepository) : ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }

    fun login(username: String, pass: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response =
                loginRepo.loginProcess(LoginRequest(username = username, password = pass))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _accessToken.value = response.body()?.token
                    _message.value = response.body()?.message
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

    fun resetPass(email: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
                val response = loginRepo.resetPass(ForgotPassRequest(email))
                if (response.isSuccessful) {
                    _message.value = response.body()?.message
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


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}