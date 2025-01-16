package dev.antasource.goling.data.model.pickup.request

import okhttp3.MultipartBody

data class OrderRequest(
    val originSender: OriginSender,
    val destinationReceipt: DestinationReceipt,
    val packageDetails: PackageDetails,
    val additionalDetails: AdditionalDetails,
    val multipartImage: MultipartBody.Part
)