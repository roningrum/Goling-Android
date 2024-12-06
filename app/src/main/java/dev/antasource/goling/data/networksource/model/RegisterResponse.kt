package dev.antasource.goling.data.networksource.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    val message: String
)
