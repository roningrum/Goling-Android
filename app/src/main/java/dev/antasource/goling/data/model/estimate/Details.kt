package dev.antasource.goling.data.model.estimate

data class Details(
    var destination: Destination,
    var dimensions: String,
    var isGuaranteed: Boolean,
    var origin: Origin,
    var productType: String,
    var weight: String
)