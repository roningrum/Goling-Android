package dev.antasource.goling.data.model.topup

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("balance")
    val balance: Int
)
