package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.networksource.model.ForgotPassRequest
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.RegisterRequest

class AuthenticationRepository (private val networkRemoteSource: NetworkRemoteSource) {

    suspend fun loginProcess(loginRequest: LoginRequest) =  networkRemoteSource.getLoginResponse(loginRequest)
    suspend fun registerAccount(registerRequest: RegisterRequest) = networkRemoteSource.getRegisterResponse(registerRequest)
    suspend fun resetPass(forgotPassRequest: ForgotPassRequest ) = networkRemoteSource.resetPassResponse(forgotPassRequest)
    suspend fun logout(token: String) = networkRemoteSource.logout(token)


  }