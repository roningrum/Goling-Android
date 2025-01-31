package dev.antasource.goling.data.model.pickup.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PackageDetails(
    val height: Float,
    val width: Float,
    val length: Float,
    val weight: Float,
    val productType: Int
): Parcelable