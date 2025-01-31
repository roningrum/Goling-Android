package dev.antasource.goling.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import dev.antasource.goling.data.model.country.LocationDeliver

object SharedPrefUtil {

    private const val PREF_FILE_NAME = "secure_prefs"

    private val mainAliasKey by lazy {
        val ketParameterSpec = MasterKeys.AES256_GCM_SPEC
        MasterKeys.getOrCreate(ketParameterSpec)

    }

    private fun getSharedPref(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            PREF_FILE_NAME,
            mainAliasKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
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

    fun isFormOpenedBefore(context: Context): Boolean {
        return getSharedPref(context).getBoolean("form_opened_before", false)
    }

    fun setFormOpenedBefore(context: Context, value: Boolean) {
        val editor = getSharedPref(context).edit()
        editor.putBoolean("form_opened_before", value)
        editor.apply()
    }


    fun clearLocation(context: Context) {
        val editor = getSharedPref(context).edit()
        editor.remove("destinate_loc")
        editor.remove("origin_loc")
        editor.apply()
    }

    fun clear(context: Context) {
        getSharedPref(context).edit().clear().commit()
    }
}