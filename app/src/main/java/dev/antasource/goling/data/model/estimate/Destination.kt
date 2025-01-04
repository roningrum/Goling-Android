package dev.antasource.goling.data.model.estimate

data class Destination(
    var address: String,
    var cityId: Int,
    var districtId: Int,
    var provinceId: Int,
    var villageId: Int
)