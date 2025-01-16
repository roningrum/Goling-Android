package dev.antasource.goling.data.model.pickup.response

data class OrderHistory(
    var createdAt: String,
    var id: Int,
    var orderId: String,
    var photo: String,
    var status: String,
    var updatedAt: String,
    var updatedBy: String
)