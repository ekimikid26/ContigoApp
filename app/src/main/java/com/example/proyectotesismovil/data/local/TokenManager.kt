package com.example.proyectotesismovil.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = intPreferencesKey("user_id")
        private val UID = stringPreferencesKey("uid")
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }
    fun getUserId(): Flow<Int?> = context.dataStore.data.map { it[USER_ID] }
    fun getUid(): Flow<String?> = context.dataStore.data.map { it[UID] }

    suspend fun saveAuthData(accessToken: String, refreshToken: String, userId: Int, uid: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[USER_ID] = userId
            preferences[UID] = uid
        }
    }

    suspend fun updateAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(USER_ID)
            preferences.remove(UID)
        }
    }
}
