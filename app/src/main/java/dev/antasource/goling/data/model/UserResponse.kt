package dev.antasource.goling.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
   @SerializedName("user")
   var user: User
)