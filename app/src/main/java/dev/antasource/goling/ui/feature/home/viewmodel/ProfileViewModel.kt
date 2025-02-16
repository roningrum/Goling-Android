package dev.antasource.goling.ui.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.UserResponse
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class ProfileViewModel(private val repository: AuthenticationRepository): ViewModel()  {
    private val _message  = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse : LiveData<UserResponse> = _userResponse

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }

    var token: String = ""

    fun getUserProfile(){
        viewModelScope.launch{
            val response = repository.getUserProfile(token)
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

    fun logout(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
            val response = repository.logout(token)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    _message.value = response.body()?.message
                }else{
                    _message.value = response.body()?.message
                }
            }
        }
    }

}