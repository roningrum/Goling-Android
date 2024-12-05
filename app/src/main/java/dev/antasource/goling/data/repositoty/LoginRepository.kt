package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.AuthenticationRemoteSource
import dev.antasource.goling.data.networksource.model.LoginRequest

class LoginRepository (private val authenticationRemoteSource: AuthenticationRemoteSource) {

    suspend fun loginProcess(loginRequest: LoginRequest) = authenticationRemoteSource.getLoginResponse(loginRequest)
}