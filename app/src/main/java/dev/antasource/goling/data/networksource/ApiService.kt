package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.networksource.model.ForgotPassRequest
import dev.antasource.goling.data.networksource.model.ForgotPassResponse
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.LoginResponse
import dev.antasource.goling.data.networksource.model.RegisterRequest
import dev.antasource.goling.data.networksource.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun loginService(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun registerService(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/forget-password")
    suspend fun resetPassword(@Body forgotPassRequest: ForgotPassRequest): Response<ForgotPassResponse>
}