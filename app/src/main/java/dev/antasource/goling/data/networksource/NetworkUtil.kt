package dev.antasource.goling.data.networksource

import com.google.gson.GsonBuilder
import dev.antasource.goling.BuildConfig
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
        val gson = GsonBuilder()
            .setLenient()
            .create()

     Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client())
            .build()
            .create(ApiService::class.java)

    }
}