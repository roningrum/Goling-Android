package dev.antasource.goling.data.model.estimate

data class EstimateShipRequest(
    var originProvinceId: Int,
    var originDistrictId: Int,
    var originCityId: Int,
    var originVillageId: Int,
    var originAddress: String,
    var destinationProvinceId: Int,
    var destinationCityId: Int,
    var destinationDistrictId: Int,
    var destinationVillageId: Int,
    var destinationAddress: String,
    var height: Int,
    var width: Int,
    var length: Int,
    var weight: Int,
    var productType: String,
    var isGuaranteed: Boolean,
)