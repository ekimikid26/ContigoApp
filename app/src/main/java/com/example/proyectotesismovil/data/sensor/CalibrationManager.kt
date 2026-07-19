package com.example.proyectotesismovil.data.sensor

import android.content.Context
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.entity.*
import com.example.proyectotesismovil.security.SecurePreferences
import kotlinx.coroutines.flow.first
import kotlin.math.pow
import kotlin.math.sqrt

class CalibrationManager(private val context: Context) {
    private val db = ContigoDatabase.getDatabase(context)
    private val dao = db.calibrationDao()
    private val biometricDao = db.biomarkerDao()

    suspend fun startCalibration(userId: String) {
        val calibration = CalibrationEntity(
            userId = userId,
            startDate = System.currentTimeMillis()
        )
        dao.insertCalibration(calibration)
    }

    suspend fun processDailySummary(userId: String, dayNumber: Int) {
        val readings = biometricDao.getReadingsSince(System.currentTimeMillis() - 24 * 60 * 60 * 1000).first()
        
        if (readings.isEmpty()) return

        val avgHeartRate = readings.mapNotNull { it.heartRate }.takeIf { it.isNotEmpty() }?.average()?.toFloat()
        val avgHrv = readings.mapNotNull { it.hrv }.takeIf { it.isNotEmpty() }?.average()?.toFloat()
        val sleepHours = readings.mapNotNull { it.sleepHours }.sum()
        val avgActivityLevel = readings.mapNotNull { it.activityLevel }.takeIf { it.isNotEmpty() }?.average()?.toFloat()
        val avgScreenUnlocks = readings.mapNotNull { it.screenUnlocks }.takeIf { it.isNotEmpty() }?.average()?.toFloat()
        val avgAppUsageMinutes = readings.mapNotNull { it.appUsageMinutes }.takeIf { it.isNotEmpty() }?.average()?.toFloat()

        val dailySummary = DailyCalibrationEntity(
            userId = userId,
            dayNumber = dayNumber,
            date = System.currentTimeMillis(),
            avgHeartRate = avgHeartRate,
            avgHrv = avgHrv,
            sleepHours = sleepHours,
            avgActivityLevel = avgActivityLevel,
            avgScreenUnlocks = avgScreenUnlocks,
            avgAppUsageMinutes = avgAppUsageMinutes
        )
        dao.insertDailySummary(dailySummary)
    }

    suspend fun calculateBaseline(userId: String) {
        val summaries = dao.getDailySummaries(userId)
        if (summaries.size < 7) return

        val baseline = BaselineEntity(
            userId = userId,
            avgHeartRate = summaries.mapNotNull { it.avgHeartRate }.average().toFloat(),
            stdHeartRate = calculateStdDev(summaries.mapNotNull { it.avgHeartRate }),
            avgHrv = summaries.mapNotNull { it.avgHrv }.average().toFloat(),
            stdHrv = calculateStdDev(summaries.mapNotNull { it.avgHrv }),
            avgSleepHours = summaries.mapNotNull { it.sleepHours }.average().toFloat(),
            stdSleepHours = calculateStdDev(summaries.mapNotNull { it.sleepHours }),
            avgActivityLevel = summaries.mapNotNull { it.avgActivityLevel }.average().toFloat(),
            stdActivityLevel = calculateStdDev(summaries.mapNotNull { it.avgActivityLevel }),
            avgScreenUnlocks = summaries.mapNotNull { it.avgScreenUnlocks }.average().toFloat(),
            stdScreenUnlocks = calculateStdDev(summaries.mapNotNull { it.avgScreenUnlocks }),
            avgAppUsageMinutes = summaries.mapNotNull { it.avgAppUsageMinutes }.average().toFloat(),
            stdAppUsageMinutes = calculateStdDev(summaries.mapNotNull { it.avgAppUsageMinutes }),
            calibrationCompletedAt = System.currentTimeMillis()
        )
        
        dao.insertBaseline(baseline)
        
        val calibration = dao.getCalibration(userId).first()
        calibration?.let {
            dao.insertCalibration(it.copy(isCompleted = true, completedAt = System.currentTimeMillis()))
            SecurePreferences.saveBoolean(context, "calibration_completed", true)
        }
    }

    private fun calculateStdDev(values: List<Float>): Float {
        if (values.isEmpty()) return 0f
        val avg = values.average()
        return sqrt(values.map { (it - avg).pow(2) }.average()).toFloat()
    }
}
