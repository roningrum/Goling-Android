package dev.antasource.goling.data.model

data class EstimateRequest(
    var destinationAddress: String,
    var destinationCityId: String,
    var destinationDistrictId: String,
    var destinationProvinceId: String,
    var destinationVillageId: String,
    var height: Int,
    var isGuaranteed: Boolean,
    var length: Int,
    var originAddress: String,
    var originCityId: String,
    var originDistrictId: String,
    var originProvinceId: String,
    var originVillageId: String,
    var productType: String,
    var weight: Int,
    var width: Int
)