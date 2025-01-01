package dev.antasource.goling.data.model.country

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationDeliver (
    val province: String,
    val city: String,
    val distric: String,
    val village: String
): Parcelable