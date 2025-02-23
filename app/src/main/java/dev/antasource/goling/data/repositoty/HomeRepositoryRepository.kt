package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.model.location.LocationUpdateResponse
import dev.antasource.goling.data.model.topup.Balance
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.home.HomeRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryRepository(private val networkRemoteSource: NetworkRemoteSource) : HomeRepositoryImpl {
    suspend fun getUserProfile(token: String) = networkRemoteSource.getUser(token)
    suspend fun getBalance(token: String) = networkRemoteSource.getBalance(token)
    override fun getWalletBallance(token: String): Flow<ApiResult<Balance>> = flow {
        emit(ApiResult.Loading)
        val result = networkRemoteSource.getBalance(token)
        emit(result)
    }


    override fun postLocation(token: String, locationRequest: LocationRequest): Flow<ApiResult<LocationUpdateResponse>> = flow{
        emit(ApiResult.Loading)
        val result = networkRemoteSource.postLocationUpdate(token, locationRequest)
        emit(result)
    }
}