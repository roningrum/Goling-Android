package dev.antasource.goling.data.local

import android.content.Context
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.data.model.pickup.request.PackageDetails

interface PickupDataPrefImpl {
    fun saveOriginOrder(context: Context, originSender: OriginSender)
    fun getLatestOriginOrder() : OriginSender?
    fun saveDestinationOrder(context: Context, destinationReceipt: DestinationReceipt)
    fun getLatestDestinationReceipt(): DestinationReceipt?
    fun savePackageDetail(context: Context, detail: PackageDetails)
    fun clearData()
}