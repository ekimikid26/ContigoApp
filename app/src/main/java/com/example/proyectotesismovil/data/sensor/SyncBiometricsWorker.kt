package com.example.proyectotesismovil.data.sensor

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.remote.api.BiometricApi
import com.example.proyectotesismovil.data.remote.api.BiometricRequest
import com.example.proyectotesismovil.data.remote.ApiClient
import com.example.proyectotesismovil.data.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull

class SyncBiometricsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val tokenManager = TokenManager(applicationContext)
        val accessToken = tokenManager.getAccessToken().firstOrNull()
        if (accessToken == null) return Result.failure()

        val db = ContigoDatabase.getDatabase(applicationContext)
        val dao = db.biomarkerDao()
        val api = ApiClient.getRetrofit(tokenManager, applicationContext).create(BiometricApi::class.java)

        val unsynced = dao.getUnsyncedReadings()
        if (unsynced.isEmpty()) return Result.success()

        // Sync in batches of 50
        unsynced.chunked(50).forEach { batch ->
            try {
                batch.forEach { reading ->
                    val response = api.saveBiometric(BiometricRequest(
                        heart_rate = reading.heartRate,
                        hrv = reading.hrv,
                        spo2 = reading.spO2,
                        stress_level = reading.stressLevel,
                        sleep_hours = reading.sleepHours,
                        activity_level = reading.activityLevel,
                        screen_unlocks = reading.screenUnlocks,
                        app_usage_minutes = reading.appUsageMinutes
                    ))
                    if (response.isSuccessful) {
                        dao.markAsSynced(listOf(reading.id))
                    }
                }
            } catch (e: Exception) {
                return Result.retry()
            }
        }

        return Result.success()
    }
}
