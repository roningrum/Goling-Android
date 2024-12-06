package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.AuthenticationRemoteSource
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.RegisterRequest

class AuthenticationRepository (private val authenticationRemoteSource: AuthenticationRemoteSource) {

    suspend fun loginProcess(loginRequest: LoginRequest) = authenticationRemoteSource.getLoginResponse(loginRequest)
    suspend fun regisProcess(registerRequest: RegisterRequest) = authenticationRemoteSource.getRegisterResponse(registerRequest)
}