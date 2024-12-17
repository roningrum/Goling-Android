package dev.antasource.goling.ui.feature.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dev.antasource.goling.data.networksource.model.ErrorMessage
import dev.antasource.goling.data.networksource.model.RegisterRequest
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class RegisterViewModel(private val regisRepo: AuthenticationRepository): ViewModel() {
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    var phone_number : String = ""


    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->   }


    fun register(username:String, email:String, pass:String, phone:String){
      job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch{
          val response = regisRepo.registerAccount(RegisterRequest(username, email, pass, phone))
          withContext(Dispatchers.Main){
              if(response.isSuccessful){
                  _message.value = response.body()?.message
              }else{
                  try {
                      val gson = Gson()
                      val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java )
                      _errorMessage.value = error.message
                  } catch (e: IOException){
                      _errorMessage.value = e.message
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