package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.RegisterRequest

class AuthenticationRepository (private val networkRemoteSource: NetworkRemoteSource) {

    suspend fun loginProcess(loginRequest: LoginRequest) =  networkRemoteSource.getLoginResponse(loginRequest)
    suspend fun registerAccount(registerRequest: RegisterRequest) = networkRemoteSource.getRegisterResponse(registerRequest)
    suspend fun resetPass(forgotPassRequest: ForgotPassRequest ) = networkRemoteSource.resetPassResponse(forgotPassRequest)
    suspend fun logout(token: String) = networkRemoteSource.logout(token)
    suspend fun getUserProfile(token: String) = networkRemoteSource.getUser(token)
  }