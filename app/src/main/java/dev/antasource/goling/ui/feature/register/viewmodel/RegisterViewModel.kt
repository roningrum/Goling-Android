package dev.antasource.goling.ui.feature.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.launch
import okio.IOException

class RegisterViewModel(private val regisRepo: AuthenticationRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    var phoneNumber: String = ""

    fun register(username: String, email: String, pass: String) {
        viewModelScope.launch {
            val response =
                regisRepo.registerAccount(RegisterRequest(username, email, pass, phoneNumber))
            if (response.isSuccessful) {
                _message.value = response.body()?.message
            } else {
                try {
                    val gson = Gson()
                    val error =
                        gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                    _errorMessage.value = error.message
                } catch (e: IOException) {
                    _errorMessage.value = e.message
                }
            }
        }
    }
}