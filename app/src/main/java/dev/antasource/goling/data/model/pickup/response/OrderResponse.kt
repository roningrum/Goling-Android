package dev.antasource.goling.data.model.pickup.response

data class OrderResponse(
    var estimasi: String,
    var order: Order,
    var orderDetail: OrderDetail,
    var orderHistory: OrderHistory
)