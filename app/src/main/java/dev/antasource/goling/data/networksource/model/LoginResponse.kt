package dev.antasource.goling.data.networksource.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message:String,
    @SerializedName("token")
    val token: String


)