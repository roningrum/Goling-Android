package dev.antasource.goling.ui.feature.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.RegisterResponse
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.repositoty.auth.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterViewModel(private val regisRepo: AuthenticationRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<ApiResult<RegisterResponse>>(ApiResult.Loading)
    val registerState : StateFlow<ApiResult<RegisterResponse>> = _registerState

    var phoneNumber: String = ""

    fun register(username: String, email: String, pass: String) {
        viewModelScope.launch {
            regisRepo.register(RegisterRequest(
                username = username,
                email = email,
                password = pass,
                phone = phoneNumber
            )).onStart {
                _registerState.value = ApiResult.Loading
            }.catch { msg ->
                _registerState.value = ApiResult.Error(msg.message ?: "Unknown Error" )
            }
                .collect{ registerResponse ->
                _registerState.value = registerResponse
            }
        }
    }
}