package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.networksource.AuthenticationRemoteSource
import dev.antasource.goling.data.networksource.model.ForgotPassRequest
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.LoginResponse
import dev.antasource.goling.data.networksource.model.RegisterRequest
import dev.antasource.goling.data.networksource.model.RegisterResponse

class AuthenticationRepository (private val authenticationRemoteSource: AuthenticationRemoteSource) {

    suspend fun loginProcess(loginRequest: LoginRequest) =  authenticationRemoteSource.getLoginResponse(loginRequest)

    suspend fun registerAccount(registerRequest: RegisterRequest) = authenticationRemoteSource.getRegisterResponse(registerRequest)
    suspend fun resetPass(forgotPassRequest: ForgotPassRequest ) = authenticationRemoteSource.resetPassResponse(forgotPassRequest)

  }