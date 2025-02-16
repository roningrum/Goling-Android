package dev.antasource.goling.data.model.pickup.response

data class Order(
    var clientIp: String? = null,
    var createdAt: String? = null,
    var destinationAddress: String? = null,
    var destinationCityId: String? = null,
    var destinationDistrictId: String? = null,
    var destinationProvinceId: String? = null,
    var destinationVillageId: String? = null,
    var id: Int? = 0,
    var insuranceRate: Int? = 0,
    var itemPrice: Int? = 0,
    var orderId: String? = null,
    var originAddress: String? = null,
    var originCityId: String? = null,
    var originDistrictId: String? = null,
    var originProvinceId: String? = null,
    var originVillageId: String? = null,
    var totalCost: Int? = 0,
    var updatedAt: String ?= null,
    var username: String ? = null
)