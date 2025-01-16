package dev.antasource.goling.data.repositoty

import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.networksource.NetworkRemoteSource

class ShippingRepository(private val networkRemoteSource: NetworkRemoteSource) {
    suspend fun getRegion() = networkRemoteSource.getRegion()
    suspend fun getCityRegencies(provinceId: Int) = networkRemoteSource.getRegencies(provinceId)
    suspend fun getDistrics(cityId: Int) = networkRemoteSource.getDistrics(cityId)
    suspend fun getVillages(districtId: Int) = networkRemoteSource.getVillages(districtId)
    suspend fun getEstimateShipping(estimateShipRequest: EstimateShipRequest) = networkRemoteSource.getEstimateShipping(estimateShipRequest)

    //get all product
    suspend fun getProductType()= networkRemoteSource.getProductType()
}