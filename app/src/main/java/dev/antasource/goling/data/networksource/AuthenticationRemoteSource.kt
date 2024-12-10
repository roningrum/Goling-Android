package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.networksource.model.ForgotPassRequest
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.RegisterRequest

class AuthenticationRemoteSource() {
    suspend fun getLoginResponse(login: LoginRequest) = NetworkUtil.apiService.loginService(login)
    suspend fun getRegisterResponse(register: RegisterRequest) = NetworkUtil.apiService.registerService(register)
    suspend fun resetPassResponse(forgotPassRequest: ForgotPassRequest) = NetworkUtil.apiService.resetPassword(forgotPassRequest)
}