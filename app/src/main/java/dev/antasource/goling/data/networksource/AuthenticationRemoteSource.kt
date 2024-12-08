package dev.antasource.goling.data.networksource

import android.util.Log
import dev.antasource.goling.data.networksource.model.LoginRequest
import dev.antasource.goling.data.networksource.model.LoginResponse
import dev.antasource.goling.data.networksource.model.RegisterRequest

class AuthenticationRemoteSource () {
    suspend fun getLoginResponse(login: LoginRequest) : LoginResponse {
       return try {

            val response = NetworkUtil.apiService.loginService(login)
            Log.d("Response API", "Response $response")
            response
        }
        catch (e: Exception){
            throw Exception("Error : ${e.message}")
        }
    }
    suspend fun getRegisterResponse(register: RegisterRequest) = NetworkUtil.apiService.registerService(register)
}