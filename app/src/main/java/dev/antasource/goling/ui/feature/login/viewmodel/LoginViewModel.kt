package dev.antasource.goling.ui.feature.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo : AuthenticationRepository) : ViewModel()  {
    private val _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String> = _accessToken

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg : LiveData<String> = _errorMsg

    fun login(username: String, pass:String){
        viewModelScope.launch{
            try {
                val response = loginRepo.loginProcess(loginRequest = LoginRequest(username = username, password = pass))
                if(response.token != null){
                    _accessToken.value = response.token
                }
                else {
                  _errorMsg.value = response.message ?:"Invalid"
                }

            }catch (e : Exception){
                _errorMsg.value = "${e.message}"
            }

        }
    }

}