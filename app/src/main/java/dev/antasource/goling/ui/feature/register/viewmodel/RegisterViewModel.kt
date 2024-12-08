package dev.antasource.goling.ui.feature.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.networksource.model.RegisterRequest
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val regisRepo: AuthenticationRepository): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    fun register(username:String, email:String, pass:String, phone:String){
       viewModelScope.launch{
           val registrasiRequest = RegisterRequest(username, email, pass, phone)
           val response = regisRepo.regisProcess(registrasiRequest)


           if (response.status == 200){
               _message.value = response.message
           }
           else if(response.status == 400){
               _errorMessage.value = response.message
           }

       }
    }
}