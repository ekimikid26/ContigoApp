package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.local.dao.BiomarkerDao
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import com.example.proyectotesismovil.data.remote.api.BiometricApi
import com.example.proyectotesismovil.data.remote.api.BiometricRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class BiometricRepositoryImpl(
    private val api: BiometricApi,
    private val dao: BiomarkerDao
) {
    suspend fun saveBiometric(reading: BiometricReadingEntity) {
        // Offline-first: Save to Room
        dao.insertBiometricReading(reading)
        
        // Remote sync
        try {
            api.saveBiometric(BiometricRequest(
                heart_rate = reading.heartRate,
                hrv = reading.hrv,
                spo2 = reading.spO2,
                stress_level = reading.stressLevel,
                sleep_hours = reading.sleepHours,
                activity_level = reading.activityLevel,
                screen_unlocks = reading.screenUnlocks,
                app_usage_minutes = reading.appUsageMinutes
            ))
        } catch (e: Exception) {
            // Log sync failure
        }
    }

    fun getMyBiometrics(days: Int): Flow<List<BiometricReadingEntity>> = flow {
        val local = dao.getAllReadings().first()
        if (local.isNotEmpty()) {
            emit(local)
        } else {
            try {
                val response = api.getMyBiometrics(days)
                if (response.isSuccessful && response.body() != null) {
                    // Sync to local and emit
                    emit(emptyList()) // Placeholder mapping
                }
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }
}
