package dev.antasource.goling.data.model.pickup.response

data class Order(
    var clientIp: String,
    var createdAt: String,
    var destinationAddress: String,
    var destinationCityId: String,
    var destinationDistrictId: String,
    var destinationProvinceId: String,
    var destinationVillageId: String,
    var id: Int,
    var insuranceRate: Int,
    var itemPrice: Int,
    var orderId: String,
    var originAddress: String,
    var originCityId: String,
    var originDistrictId: String,
    var originProvinceId: String,
    var originVillageId: String,
    var totalCost: Int,
    var updatedAt: String,
    var username: String
)