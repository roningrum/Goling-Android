package dev.antasource.goling.data.networksource

import android.util.Log
import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OrderRequestMapper
import dev.antasource.goling.data.model.pickup.request.OrderRequestMapper.toPartMap
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

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
    suspend fun getProductTypeId(id: Int) = NetworkUtil.apiService.getProductTypesbyId(id)
    suspend fun postOrder(token: String, orderRequest: OrderRequest) : Response<OrderResponse> {
        val partMap = toPartMap(orderRequest)
        Log.d("Order Request", "part $partMap")
        val photoPart = orderRequest.multipartImage
        return NetworkUtil.apiService.postOrders("Bearer $token", partMap, photoPart)
    }
}