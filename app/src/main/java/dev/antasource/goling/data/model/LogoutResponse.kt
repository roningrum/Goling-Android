package dev.antasource.goling.data.model

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    var message: String

)
