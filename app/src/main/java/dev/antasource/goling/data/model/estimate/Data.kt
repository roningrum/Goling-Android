package dev.antasource.goling.data.model.estimate

data class Data(
    var deliveryTime: String,
    var details: Details,
    var insuranceRate: Int,
    var itemPrice: Int,
    var totalCost: Int
)