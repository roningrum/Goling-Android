package dev.antasource.goling.data.model.country

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LocationDeliver (
    val provinceId: Int,
    val cityId: Int,
    val districId: Int,
    val villageId: Int,
    val province: String,
    val city: String,
    val distric: String,
    val village: String
): Parcelable
