package dev.antasource.goling.data.networksource.model

import com.google.gson.annotations.SerializedName

data class ErrorMessage (
    @SerializedName("message")
    val message : String,
    @SerializedName("status")
    val status: Int
)