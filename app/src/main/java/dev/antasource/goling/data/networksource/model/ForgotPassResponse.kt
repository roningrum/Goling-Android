package dev.antasource.goling.data.networksource.model

import com.google.gson.annotations.SerializedName

data class ForgotPassResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
