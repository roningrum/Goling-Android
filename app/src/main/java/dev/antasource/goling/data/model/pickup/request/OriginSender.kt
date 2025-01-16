package dev.antasource.goling.data.model.pickup.request

data class OriginSender(
    val originProvinceId : String,
    val originCityId : String,
    val originDistricId : String,
    val originVillageId : String,
    val originAddress : String,
    val originName : String,
    val originPhone : String,
)
