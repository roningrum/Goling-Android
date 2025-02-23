package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.model.location.LocationUpdateResponse
import dev.antasource.goling.data.networksource.ApiResult
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.home.HomeImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepository(private val networkRemoteSource: NetworkRemoteSource) : HomeImpl {
    suspend fun getUserProfile(token: String) = networkRemoteSource.getUser(token)
    suspend fun getBalance(token: String) = networkRemoteSource.getBalance(token)
    override fun postLocation(token: String, locationRequest: LocationRequest): Flow<ApiResult<LocationUpdateResponse>> = flow{
        emit(ApiResult.Loading)
        val result = networkRemoteSource.postLocationUpdate(token, locationRequest
        )
        emit(result)
    }
}