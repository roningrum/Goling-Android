package dev.antasource.goling.ui.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: AuthenticationRepository): ViewModel()  {
    private val _message  = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }

    var token: String = ""

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