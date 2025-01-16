package dev.antasource.goling.data.model.pickup.request

data class PackageDetails(
    val height: Float,
    val width: Float,
    val length: Float,
    val weight: Float,
    val productType: Int
)