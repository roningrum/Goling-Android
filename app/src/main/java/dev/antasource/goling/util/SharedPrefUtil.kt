package dev.antasource.goling.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dev.antasource.goling.data.model.country.LocationDeliver

object SharedPrefUtil {

    private const val PREF_FILE_NAME = "secure_prefs"

    private val mainAliasKey by lazy {
        val ketParameterSpec = MasterKeys.AES256_GCM_SPEC
        MasterKeys.getOrCreate(ketParameterSpec)

    }

    private fun getSharedPref(context: Context) : SharedPreferences{
       return  EncryptedSharedPreferences.create(
          PREF_FILE_NAME,
           mainAliasKey,
           context,
           EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
           EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
       )
    }

    fun saveAccessToken(context:Context, token:String){
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun saveLastOriginLocation(context: Context, lastLocation: String){
        val sharedPreferences = getSharedPref(context)
        val editor = sharedPreferences.edit()
        editor.putString("lastLocation", lastLocation)
        editor.apply()
    }

    fun getAccessToken(context: Context) = getSharedPref(context).getString("token", "tokenku")
    fun getLastOriginLocation(context: Context) = getSharedPref(context).getString("lastLocation", "")

    fun clearLocation(context: Context){
        val editor = getSharedPref(context).edit()
        editor.remove("lastLocation")
        editor.apply()
    }

    fun clear(context: Context){
        getSharedPref(context).edit().clear().commit()
    }
}