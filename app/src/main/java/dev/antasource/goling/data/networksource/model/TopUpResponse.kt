package dev.antasource.goling.data.networksource.model

data class TopUpResponse(
    var invoiceUrl: String,
    var message: String,
    var status: Int
)