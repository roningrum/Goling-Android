package dev.antasource.goling.data.model.pickup.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OriginSender(
    val originProvinceId : String,
    val originCityId : String,
    val originDistricId : String,
    val originVillageId : String,
    val originAddress : String,
    val originName : String,
    val originPhone : String,
) : Parcelable
