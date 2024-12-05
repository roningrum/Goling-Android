package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.networksource.model.LoginRequest

class AuthenticationRemoteSource () {
    suspend fun getLoginResponse(login: LoginRequest) = NetworkUtil.apiService.loginService(login)
}