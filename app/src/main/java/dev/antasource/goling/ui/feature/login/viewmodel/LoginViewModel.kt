package dev.antasource.goling.ui.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.repositoty.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo : LoginRepository) : ViewModel()  {
    private val _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String> = _accessToken

    fun login(username: String, pass:String){
        viewModelScope.launch{
            val response = loginRepo.loginProcess(loginRequest = LoginRequest(username = username, password = pass))
            _accessToken.value = response.token
        }
    }

}