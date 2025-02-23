package dev.antasource.goling.data.model.location

import com.google.gson.annotations.SerializedName

data class LocationUpdateResponse(
    @SerializedName("message")
    val message: String
)