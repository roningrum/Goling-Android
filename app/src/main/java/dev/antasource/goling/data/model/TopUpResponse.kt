package dev.antasource.goling.data.model

data class TopUpResponse(
    var invoiceUrl: String,
    var message: String,
    var status: Int
)