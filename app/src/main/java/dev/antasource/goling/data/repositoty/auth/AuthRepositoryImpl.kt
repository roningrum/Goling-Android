package dev.antasource.goling.data.repositoty.auth

import dev.antasource.goling.data.model.ForgotPassResponse
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.LoginResponse
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.RegisterResponse
import dev.antasource.goling.data.networksource.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepositoryImpl {
    fun login(loginRequest: LoginRequest): Flow<ApiResult<LoginResponse>>
    fun register(registerRequest: RegisterRequest): Flow<ApiResult<RegisterResponse>>
    fun resetPassword(email: String) : Flow<ApiResult<ForgotPassResponse>>
}