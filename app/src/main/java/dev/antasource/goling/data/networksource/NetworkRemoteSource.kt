package dev.antasource.goling.data.networksource

import android.util.Log
import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.history.OrdersResponse
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OrderRequestMapper.toPartMap
import dev.antasource.goling.data.model.pickup.response.OrderDetailResponse
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import dev.antasource.goling.data.networksource.NetworkUtil.apiService
import retrofit2.Response

class NetworkRemoteSource() {
    suspend fun getLoginResponse(login: LoginRequest) = apiService.loginService(login)
    suspend fun getRegisterResponse(register: RegisterRequest) = apiService.registerService(register)
    suspend fun resetPassResponse(forgotPassRequest: ForgotPassRequest) = apiService.resetPassword(forgotPassRequest)
    suspend fun getUser(token: String)= apiService.getUser("Bearer $token")
    suspend fun topUpWallet(token: String, topUpRequest: TopUpRequest) = apiService.topUpWallet("Bearer $token", topUpRequest)
    suspend fun getBalance(token: String) = apiService.balance("Bearer $token")
    suspend fun verifyPayment(token:String, transactionId: String) = apiService.verifyPayment("Bearer $token",
       transactionId)
    suspend fun logout(token: String) = apiService.logout("Bearer $token ")
    suspend fun getRegion() = apiService.getRegion()
    suspend fun getRegencies(provinceId: Int) = apiService.getCity(provinceId)
    suspend fun getDistrics(cityId: Int) = apiService.getDistrict(cityId)
    suspend fun getVillages(districtId: Int) = apiService.getVillage(districtId)
    suspend fun getEstimateShipping(estimateShipRequest: EstimateShipRequest) = apiService.getEstimateShipping(estimateShipRequest)

    suspend fun getProductType() = apiService.getProductTypes()
    suspend fun getProductTypeId(id: Int) = apiService.getProductTypesbyId(id)

//    order
    suspend fun postOrder(token: String, orderRequest: OrderRequest) : Response<OrderResponse> {
        val partMap = toPartMap(orderRequest)
        Log.d("Order Request", "part $partMap")
        val photoPart = orderRequest.multipartImage
        return apiService.postOrders("Bearer $token", partMap, photoPart)
    }
    suspend fun getOrders(token: String) : Response<OrdersResponse> = apiService.getOrders("Bearer $token ")
    suspend fun getOrderDetail(token: String, id: Long) : Response<OrderDetailResponse> = apiService.getOrderId("Bearer $token", id)
}