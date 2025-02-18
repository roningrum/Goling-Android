package dev.antasource.goling.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import dev.antasource.goling.data.model.country.LocationDeliver

object SharedPrefUtil {

    private const val PREF_FILE_NAME = "secure_prefs"

    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private fun getSharedPref(context: Context): SharedPreferences {
        return try {
            EncryptedSharedPreferences.create(
                context,
                PREF_FILE_NAME,
                getMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit().clear().apply()
            EncryptedSharedPreferences.create(
                context,
                PREF_FILE_NAME,
                getMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun saveOriginDataPrefs(context: Context, locationDeliver: LocationDeliver) {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(locationDeliver)
        editor.putString("origin_loc", json)
        editor.apply()
    }

    fun saveDesDataPrefs(context: Context, locationDeliver: LocationDeliver) {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(locationDeliver)
        editor.putString("destinate_loc", json)
        editor.apply()
    }

    fun getAccessToken(context: Context) = getSharedPref(context).getString("token", "tokenku")
    fun getLastOriginLocation(context: Context): LocationDeliver? {
        val sharedPreferences = getSharedPref(context)
        val gson = Gson()
        val json = sharedPreferences.getString("origin_loc", null)
        return if (json != null) {
            gson.fromJson(json, LocationDeliver::class.java)
        } else {
            null
        }
    }

    fun getLastDestinateLocation(context: Context): LocationDeliver? {
        val sharedPreferences = getSharedPref(context)
        val gson = Gson()
        val json = sharedPreferences.getString("destinate_loc", null)
        return if (json != null) {
            gson.fromJson(json, LocationDeliver::class.java)
        } else {
            null
        }
    }

    fun clearLocation(context: Context) {
        val editor = getSharedPref(context).edit()
        editor.remove("destinate_loc")
        editor.remove("origin_loc")
        editor.apply()
    }

    fun clearAccessToken(context: Context) {
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
    }
}