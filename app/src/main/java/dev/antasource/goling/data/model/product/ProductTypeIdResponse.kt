package dev.antasource.goling.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductTypeIdResponse(
    @SerializedName("productType")
    var productTypeName: ProductType
)