package dev.antasource.goling.data.networksource

import dev.antasource.goling.data.model.ForgotPassRequest
import dev.antasource.goling.data.model.ForgotPassResponse
import dev.antasource.goling.data.model.LoginRequest
import dev.antasource.goling.data.model.LoginResponse
import dev.antasource.goling.data.model.LogoutResponse
import dev.antasource.goling.data.model.PaymentResponse
import dev.antasource.goling.data.model.country.Region
import dev.antasource.goling.data.model.RegisterRequest
import dev.antasource.goling.data.model.RegisterResponse
import dev.antasource.goling.data.model.TopUpRequest
import dev.antasource.goling.data.model.TopUpResponse
import dev.antasource.goling.data.model.UserRequest
import dev.antasource.goling.data.model.UserResponse
import dev.antasource.goling.data.model.country.Districs
import dev.antasource.goling.data.model.country.Regencies
import dev.antasource.goling.data.model.country.Villages
import dev.antasource.goling.data.model.estimate.EstimateShipRequest
import dev.antasource.goling.data.model.estimate.EstimateShipResponse
import dev.antasource.goling.data.model.pickup.response.OrderResponse
import dev.antasource.goling.data.model.product.ProductType
import dev.antasource.goling.data.model.product.ProductTypeIdResponse
import dev.antasource.goling.data.model.product.ProductTypeResponse
import dev.antasource.goling.data.model.topup.Balance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun loginService(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun registerService(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/forget-password")
    suspend fun resetPassword(@Body forgotPassRequest: ForgotPassRequest): Response<ForgotPassResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization")token: String): Response<LogoutResponse>

    @GET("user")
    suspend fun getUser(@Header("Authorization")token: String): Response<UserResponse>

    @PUT("user/update-user")
    suspend fun updateUser(@Header("Authorization")token: String, userRequest: UserRequest)

    @POST("wallet/topup")
    suspend fun topUpWallet(@Header("Authorization")token: String, @Body topUpRequest: TopUpRequest): Response<TopUpResponse>

    @GET("wallet/verify-payment")
    suspend fun verifyPayment(@Header("Authorization")token: String, @Query("transactionId") transactionId: String): Response<PaymentResponse>

    @GET("wallet/balance")
    suspend fun balance(@Header("Authorization")token: String): Response<Balance>

    //    Get Location
    @GET("wilayah")
    suspend fun getRegion(): Response<List<Region>>

    @GET("wilayah/{provinceId}/regencies")
    suspend fun getCity(@Path("provinceId") provinceId: Int): Response<List<Regencies>>

    @GET("wilayah/{regencyId}/districts")
    suspend fun getDistrict(@Path("regencyId") regencyId: Int): Response<List<Districs>>

    @GET("wilayah/{districtId}/villages")
    suspend fun getVillage(@Path("districtId") districtId: Int): Response<List<Villages>>

    //estimate
    @POST("shipping/estimate-shipping")
    suspend fun getEstimateShipping(@Body estimateRequest: EstimateShipRequest): Response<EstimateShipResponse>

    //Ship
    @GET("admin/product-types")
    suspend fun getProductTypes(): Response<ProductTypeResponse>

    @GET("admin/product-types/{id}")
    suspend fun getProductTypesbyId(@Path("id") id: Int): Response<ProductTypeIdResponse>

    @Multipart
    @POST("orders")
    suspend fun postOrders(
        @Header("Authorization") token: String,
        @PartMap orderRequest: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part
    ): Response<OrderResponse>
}