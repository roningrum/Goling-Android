package dev.antasource.goling.data.model.pickup.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdditionalDetails(
    val glassware: Boolean,
    val isGuaranteed: Boolean
): Parcelable
