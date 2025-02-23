package dev.antasource.goling.data.networksource

import android.util.Log
import com.google.gson.Gson
import dev.antasource.goling.data.model.ErrorMessage
import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.ForgotPassResponse
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.LoginResponse
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.RegisterResponse
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.history.OrdersResponse
import dev.antasource.goling.data.model.location.LocationRequest
import dev.antasource.goling.data.model.location.LocationUpdateResponse
import dev.antasource.goling.data.model.pickup.request.OrderRequest
import dev.antasource.goling.data.model.pickup.request.OrderRequestMapper.toPartMap
import dev.antasource.goling.data.model.pickup.response.OrderDetailResponse
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import dev.antasource.goling.data.networksource.NetworkUtil.apiService
import okio.IOException
import retrofit2.Response

class NetworkRemoteSource{
    suspend fun postLogin(login: LoginRequest): ApiResult<LoginResponse> {
       val response = apiService.loginService(login)
        if(response.isSuccessful){
            val data = response.body()
            return ApiResult.Success(data)
        } else{
            try {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                return ApiResult.Error(error.message)
            } catch (e: IOException) {
                return ApiResult.Error("${e.message}")
            }
            return ApiResult.Error("Unknown Error")
        }
    }

    suspend fun postRegisterUser(register: RegisterRequest) : ApiResult<RegisterResponse>{
        val response = apiService.registerService(register)
        if(response.isSuccessful){
            val data = response.body()
            return ApiResult.Success(data)
        } else{
            try {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                return ApiResult.Error(error.message)
            } catch (e: IOException) {
                return ApiResult.Error("${e.message}")
            }
            return ApiResult.Error("Unknown Error")
        }
    }
    suspend fun postResetPassword(forgotPassRequest: ForgotPassRequest) : ApiResult<ForgotPassResponse>{
        val response = apiService.resetPassword(forgotPassRequest)
        if(response.isSuccessful){
            val data = response.body()
            return ApiResult.Success(data)
        } else{
            try {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                return ApiResult.Error(error.message)
            } catch (e: IOException) {
                return ApiResult.Error("${e.message}")
            }
            return ApiResult.Error("Unknown Error")
        }
    }
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

    suspend fun postLocationUpdate(token: String, locationRequest: LocationRequest): ApiResult<LocationUpdateResponse>{
        val response = apiService.postLocationUpdate(
            token = "Bearer $token",
            locationRequest = locationRequest
        )
        if(response.isSuccessful){
            val data = response.body()
            return ApiResult.Success(data)
        }else{
            try {
                val gson = Gson()
                val error = gson.fromJson(response.errorBody()?.string(), ErrorMessage::class.java)
                return ApiResult.Error(error.message)
            } catch (e: IOException) {
                return ApiResult.Error("${e.message}")
            }
            return ApiResult.Error("Unknown Error")
        }
    }

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