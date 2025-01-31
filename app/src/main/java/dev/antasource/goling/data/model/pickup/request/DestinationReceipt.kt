package dev.antasource.goling.data.model.pickup.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DestinationReceipt(
    val destinationProvinceId : String,
    val destinationCityId : String,
    val destinationDistricId : String,
    val destinationVillageId : String,
    val destinationAddress : String,
    val destinationName : String,
    val destinationPhone : String,
): Parcelable
