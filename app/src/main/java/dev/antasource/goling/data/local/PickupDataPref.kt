package dev.antasource.goling.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dev.antasource.goling.data.model.pickup.request.DestinationReceipt
import dev.antasource.goling.data.model.pickup.request.OriginSender
import dev.antasource.goling.data.model.pickup.request.PackageDetails

class PickupDataPref(context: Context) : PickupDataPrefImpl {
    private val savePickup = "PICKUP"
    private val sharedPreferences = context.getSharedPreferences(savePickup, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    override fun saveOriginOrder(
        context: Context,
        originSender: OriginSender
    ) {
        val json = gson.toJson(originSender)
        editor.putString("origin_sender", json)
        editor.apply()

    }

    override fun getLatestOriginOrder(): OriginSender? {
        val json = sharedPreferences.getString("origin_sender", "{}") ?: "{}"
        return try {
            val data = gson.fromJson(json, OriginSender::class.java)
            if (data != null && isOriginSenderValid(data)) data else null
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    override fun saveDestinationOrder(
        context: Context,
        destinationReceipt: DestinationReceipt
    ) {
        val json = gson.toJson(destinationReceipt)
        editor.putString("destination_receipt", json)
        editor.apply()
    }

    override fun getLatestDestinationReceipt(): DestinationReceipt? {
        val json = sharedPreferences.getString("destination_receipt", "{}") ?: "{}"
        return try {
            val data = gson.fromJson(json, DestinationReceipt::class.java)
            if (data != null && isDestinationReceiptValid(data)) data else null
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    override fun savePackageDetail(
        context: Context,
        detail: PackageDetails
    ) {
        val json = gson.toJson(detail)
        editor.putString("package_detail", json)
        editor.apply()
    }

    override fun clearData() {
        editor.remove("origin_sender")
        editor.remove("destination_receipt")
        editor.apply()

    }

    private fun isOriginSenderValid(originSender: OriginSender): Boolean {
        return !originSender.originName.isNullOrEmpty() &&
                !originSender.originPhone.isNullOrEmpty() &&
                !originSender.originAddress.isNullOrEmpty()
    }

    private fun isDestinationReceiptValid(destinationReceipt: DestinationReceipt): Boolean {
        return !destinationReceipt.destinationName.isNullOrEmpty() &&
                !destinationReceipt.destinationPhone.isNullOrEmpty() &&
                !destinationReceipt.destinationAddress.isNullOrEmpty()
    }
}