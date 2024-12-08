package dev.antasource.goling.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

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

    fun getAccessToken(context: Context) = getSharedPref(context).getString("token", "tokenku")

    fun clear(context: Context){
        getSharedPref(context).edit().clear().commit()
    }
}