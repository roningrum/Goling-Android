package dev.antasource.goling.data.repositoty.auth

import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.ForgotPassResponse
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.LoginResponse
import dev.antasource.goling.data.model.LogoutResponse
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthenticationRepository(private val networkRemoteSource: NetworkRemoteSource) :
    AuthRepositoryImpl {

    suspend fun getUserProfile(token: String) = networkRemoteSource.getUser(token)
    override fun login(loginRequest: LoginRequest): Flow<ApiResult<LoginResponse>> = flow {
        emit(ApiResult.Loading) // Emit loading sebelum API call
        val result = networkRemoteSource.postLogin(login = loginRequest) // Panggil API
        emit(result) // Emit hasil dari API call
    }

    override fun register(registerRequest: RegisterRequest) = flow{
        emit(ApiResult.Loading)
        val result = networkRemoteSource.postRegisterUser(register = registerRequest)
        emit(result)
    }

    override fun resetPassword(email: String): Flow<ApiResult<ForgotPassResponse>> = flow {
        emit(ApiResult.Loading)
        val result = networkRemoteSource
            .postResetPassword(forgotPassRequest = ForgotPassRequest(email))
        emit(result)
    }

    override fun logout(token: String): Flow<ApiResult<LogoutResponse>> = flow {
        emit(ApiResult.Loading)
        val result = networkRemoteSource.logout(token)
        emit(result)
    }
}