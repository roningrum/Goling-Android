package dev.antasource.goling.data.model.pickup.request

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object OrderRequestMapper {

    fun toPartMap(orderRequest: OrderRequest): Map<String, RequestBody> {
        val partMap = mutableMapOf<String, RequestBody>()

        // Helper function to convert field to RequestBody
        fun String.toPart(): RequestBody = this.toRequestBody("text/plain".toMediaTypeOrNull())
        fun Int.toPart(): RequestBody = this.toString().toPart()
        fun Float.toPart(): RequestBody = this.toString().toPart()
        fun Boolean.toPart(): RequestBody = this.toString().toPart()

        // Origin Sender
        partMap["originProvinceId"] = orderRequest.originSender.originProvinceId.toPart()
        partMap["originCityId"] = orderRequest.originSender.originCityId.toPart()
        partMap["originDistrictId"] = orderRequest.originSender.originDistrictId.toPart()
        partMap["originVillageId"] = orderRequest.originSender.originVillageId.toPart()
        partMap["originAddress"] = orderRequest.originSender.originAddress.toPart()
        partMap["originName"] = orderRequest.originSender.originName.toPart()
        partMap["originPhone"] = orderRequest.originSender.originPhone.toPart()

        // Destination Receipt
        partMap["destinationProvinceId"] = orderRequest.destinationReceipt.destinationProvinceId.toPart()
        partMap["destinationCityId"] = orderRequest.destinationReceipt.destinationCityId.toPart()
        partMap["destinationDistrictId"] = orderRequest.destinationReceipt.destinationDistrictId.toPart()
        partMap["destinationVillageId"] = orderRequest.destinationReceipt.destinationVillageId.toPart()
        partMap["destinationAddress"] = orderRequest.destinationReceipt.destinationAddress.toPart()
        partMap["destinationName"] = orderRequest.destinationReceipt.destinationName.toPart()
        partMap["destinationPhone"] = orderRequest.destinationReceipt.destinationPhone.toPart()

        // Package Details
        partMap["height"] = orderRequest.packageDetails.height.toPart()
        partMap["width"] = orderRequest.packageDetails.width.toPart()
        partMap["length"] = orderRequest.packageDetails.length.toPart()
        partMap["weight"] = orderRequest.packageDetails.weight.toPart()
        partMap["productType"] = orderRequest.packageDetails.productType.toPart()

        // Additional Details
        partMap["glassware"] = orderRequest.additionalDetails.glassware.toPart()
        partMap["isGuarantee"] = orderRequest.additionalDetails.isGuaranteed.toPart()

        return partMap
    }
}
