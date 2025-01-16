package dev.antasource.goling.data.model.pickup.response

data class OrderDetail(
    var createdAt: String,
    var glassware: Boolean,
    var height: Int,
    var id: Int,
    var isGuaranteed: Boolean,
    var length: Int,
    var orderId: String,
    var productType: Int,
    var updatedAt: String,
    var weight: Int,
    var width: Int
)