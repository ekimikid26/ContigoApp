package com.example.proyectotesismovil.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePreferences {

    private const val PREFS_NAME = "contigo_secure_prefs"

    fun get(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        get(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        return get(context).getBoolean(key, default)
    }

    fun saveString(context: Context, key: String, value: String) {
        get(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, default: String? = null): String? {
        return get(context).getString(key, default)
    }
}
