package dev.antasource.goling.data.networksource

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtil {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client =OkHttpClient
        .Builder()
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