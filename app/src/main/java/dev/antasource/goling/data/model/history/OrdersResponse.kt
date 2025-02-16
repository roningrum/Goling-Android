package dev.antasource.goling.data.model.history

import dev.antasource.goling.data.model.pickup.response.Order

data class OrdersResponse (
    val orders : List<Order>
)
