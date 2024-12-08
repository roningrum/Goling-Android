package dev.antasource.goling.data.networksource

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtil {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
//    private val responseInterceptor = object : Interceptor{
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val response = chain.proceed(chain.request())
//            val statusCode = response.code
//
//            when(statusCode){
//                201 -> "Register Sukses"
//                200 -> "Request Sukses"
//                400 -> "Invalid Credential"
//                else -> "Unknown Response"
//            }
//            return response
//        }
//
//    }
    private val client =OkHttpClient
        .Builder()
//        .addInterceptor(responseInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService : ApiService by lazy {
     Retrofit.Builder()
            .baseUrl("http://46.202.168.96:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)

    }
}