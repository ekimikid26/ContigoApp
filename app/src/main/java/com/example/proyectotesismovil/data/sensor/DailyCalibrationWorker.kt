package com.example.proyectotesismovil.data.sensor

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull

class DailyCalibrationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val tokenManager = TokenManager(applicationContext)
        val uid = tokenManager.getUid().firstOrNull() ?: return androidx.work.ListenableWorker.Result.failure()

        val db = ContigoDatabase.getDatabase(applicationContext)
        val manager = CalibrationManager(applicationContext)
        val calibrationDao = db.calibrationDao()
        
        val calibration = calibrationDao.getCalibration(uid).firstOrNull()

        if (calibration != null && !calibration.isCompleted) {
            val summaries = calibrationDao.getDailySummaries(uid)
            val currentDay = summaries.size + 1
            
            manager.processDailySummary(uid, currentDay)
            
            if (currentDay >= 7) {
                manager.calculateBaseline(uid)
            }
        }

        return androidx.work.ListenableWorker.Result.success()
    }
}
