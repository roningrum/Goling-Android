package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.estimate.EstimateShipRequest

class NetworkRemoteSource() {
    suspend fun getLoginResponse(login: LoginRequest) = NetworkUtil.apiService.loginService(login)
    suspend fun getRegisterResponse(register: RegisterRequest) = NetworkUtil.apiService.registerService(register)
    suspend fun resetPassResponse(forgotPassRequest: ForgotPassRequest) = NetworkUtil.apiService.resetPassword(forgotPassRequest)
    suspend fun getUser(token: String)= NetworkUtil.apiService.getUser("Bearer $token")
    suspend fun topUpWallet(token: String, topUpRequest: TopUpRequest) = NetworkUtil.apiService.topUpWallet("Bearer $token", topUpRequest)
    suspend fun getBalance(token: String) = NetworkUtil.apiService.balance("Bearer $token")
    suspend fun verifyPayment(token:String, transactionId: String) = NetworkUtil.apiService.verifyPayment("Bearer $token",
       transactionId)
    suspend fun logout(token: String) = NetworkUtil.apiService.logout("Bearer $token ")
    suspend fun getRegion() = NetworkUtil.apiService.getRegion()
    suspend fun getRegencies(provinceId: Int) = NetworkUtil.apiService.getCity(provinceId)
    suspend fun getDistrics(cityId: Int) = NetworkUtil.apiService.getDistrict(cityId)
    suspend fun getVillages(districtId: Int) = NetworkUtil.apiService.getVillage(districtId)
    suspend fun getEstimateShipping(estimateShipRequest: EstimateShipRequest) = NetworkUtil.apiService.getEstimateShipping(estimateShipRequest)

    suspend fun getProductType() = NetworkUtil.apiService.getProductTypes()
}