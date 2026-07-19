package com.example.proyectotesismovil.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.crypto.KeyGenerator
import android.util.Base64
import android.util.Log

object KeystoreManager {

    private const val TAG = "KeystoreManager"
    private const val PREFS_NAME = "contigo_key_prefs"
    private const val DB_KEY_PREF = "db_passphrase"

    fun getOrCreateDatabaseKey(context: Context): ByteArray {
        try {
            val prefs = getEncryptedPrefs(context)
            val existingKey = prefs.getString(DB_KEY_PREF, null)

            if (existingKey != null) {
                return Base64.decode(existingKey, Base64.DEFAULT)
            }

            val newKey = generateRandomKey()
            val encodedKey = Base64.encodeToString(newKey, Base64.DEFAULT)
            prefs.edit().putString(DB_KEY_PREF, encodedKey).apply()
            return newKey
        } catch (e: Exception) {
            Log.e(TAG, "Fallo crítico en Keystore, intentando recuperación forzada", e)
            
            // Si hay un fallo de Keystore (frecuente en reinstalaciones), intentamos borrar y reintentar
            try {
                // Borrar las preferencias corruptas
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply()
                
                // Intentar de nuevo (esto creará un nuevo MasterKey si es necesario)
                val prefs = getEncryptedPrefs(context)
                val newKey = generateRandomKey()
                val encodedKey = Base64.encodeToString(newKey, Base64.DEFAULT)
                prefs.edit().putString(DB_KEY_PREF, encodedKey).apply()
                return newKey
            } catch (e2: Exception) {
                Log.e(TAG, "Recuperación de Keystore fallida, usando fallback inseguro", e2)
                
                // Fallback final a SharedPreferences normales para que la app NO se cierre
                val fallbackPrefs = context.getSharedPreferences("contigo_key_prefs_fallback", Context.MODE_PRIVATE)
                val existingKey = fallbackPrefs.getString(DB_KEY_PREF, null)
                
                if (existingKey != null) {
                    return Base64.decode(existingKey, Base64.DEFAULT)
                }
                
                val newKey = generateRandomKey()
                val encodedKey = Base64.encodeToString(newKey, Base64.DEFAULT)
                fallbackPrefs.edit().putString(DB_KEY_PREF, encodedKey).apply()
                return newKey
            }
        }
    }

    private fun generateRandomKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        return keyGenerator.generateKey().encoded
    }

    private fun getEncryptedPrefs(context: Context): android.content.SharedPreferences {
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
}
