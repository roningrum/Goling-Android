package dev.antasource.goling.data.repositoty.home

import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.model.location.LocationUpdateResponse
import dev.antasource.goling.data.model.topup.Balance
import dev.antasource.goling.data.networksource.ApiResult
import kotlinx.coroutines.flow.Flow

interface HomeRepositoryImpl {
    fun getWalletBallance(token: String): Flow<ApiResult<Balance>>
    fun postLocation(token: String, locationRequest: LocationRequest): Flow<ApiResult<LocationUpdateResponse>>
}