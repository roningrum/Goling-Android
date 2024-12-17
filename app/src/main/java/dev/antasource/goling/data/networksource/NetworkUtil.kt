package dev.antasource.goling.data.networksource

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtil {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun client(authToken: String? = null): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)
        authToken?.let {
            builder.addInterceptor{chain ->
                chain.proceed(chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken").build())
            }
        }
        return builder.build()

    }

    val apiService : ApiService by lazy {
     Retrofit.Builder()
            .baseUrl("https://antasource.online/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
            .create(ApiService::class.java)

    }
}