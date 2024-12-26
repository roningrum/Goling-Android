package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.networksource.model.ForgotPassRequest
import dev.antasource.goling.data.networksource.model.ForgotPassResponse
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.LoginResponse
import dev.antasource.goling.data.networksource.model.LogoutResponse
import dev.antasource.goling.data.networksource.model.RegisterRequest
import dev.antasource.goling.data.networksource.model.RegisterResponse
import dev.antasource.goling.data.networksource.model.TopUpRequest
import dev.antasource.goling.data.networksource.model.TopUpResponse
import dev.antasource.goling.data.networksource.model.UserRequest
import dev.antasource.goling.data.networksource.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("auth/login")
    suspend fun loginService(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun registerService(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/forget-password")
    suspend fun resetPassword(@Body forgotPassRequest: ForgotPassRequest): Response<ForgotPassResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization")token: String): Response<LogoutResponse>

    @GET("user")
    suspend fun getUser(@Header("Authorization")token: String): Response<UserResponse>

    @PUT("user/update-user")
    suspend fun updateUser(@Header("Authorization")token: String, userRequest: UserRequest)

    @POST("wallet/topup")
    suspend fun topUpWallet(@Header("Authorization")token: String, @Body topUpRequest: TopUpRequest): Response<TopUpResponse>
}