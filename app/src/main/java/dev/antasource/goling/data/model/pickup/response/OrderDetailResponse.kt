package dev.antasource.goling.data.model.pickup.response

data class OrderDetailResponse(
    var history: List<History>? = null,
    var order: Order? = null,
    var orderDetail: OrderDetail? = null
)