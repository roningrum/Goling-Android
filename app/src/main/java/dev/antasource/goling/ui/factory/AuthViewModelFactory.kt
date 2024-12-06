package dev.antasource.goling.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import dev.antasource.goling.ui.feature.register.viewmodel.RegisterViewModel

class AuthViewModelFactory(private val repository: AuthenticationRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}