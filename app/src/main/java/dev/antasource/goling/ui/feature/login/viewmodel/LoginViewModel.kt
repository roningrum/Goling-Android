package dev.antasource.goling.ui.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.antasource.goling.data.model.ForgotPassResponse
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.LoginResponse
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo: AuthenticationRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<ApiResult<LoginResponse>>(ApiResult.Loading)
    val loginState: StateFlow<ApiResult<LoginResponse>> = _loginState

    private val _resetState = MutableStateFlow<ApiResult<ForgotPassResponse>>(ApiResult.Loading)
    val resetState: StateFlow<ApiResult<ForgotPassResponse>> = _resetState

    fun loginUser(username: String, pass: String){
        viewModelScope.launch{
            loginRepo.login(LoginRequest(
                username = username,
                password = pass
            )).onStart { _loginState.value = ApiResult.Loading }
                .catch { e -> _loginState.value = ApiResult.Error(e.message ?: "Unexpected Error")  }
                .collect{ result -> _loginState.value = result}
        }
    }

    fun resetPass(email: String) {
        viewModelScope.launch{
            loginRepo.resetPassword(email = email)
                .onStart { _resetState.value = ApiResult.Loading }
                .catch { e -> _resetState.value = ApiResult.Error(e.message ?: "Unexpected Error") }
                .collect{ result -> _resetState.value = result}
        }
    }
}