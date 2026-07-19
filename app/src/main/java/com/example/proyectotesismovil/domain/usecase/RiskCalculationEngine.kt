package com.example.proyectotesismovil.domain.usecase

import com.example.proyectotesismovil.data.local.entity.BaselineEntity
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import kotlin.math.abs

enum class RiskLevel {
    NORMAL, MILD, MODERATE, SEVERE
}

class RiskCalculationEngine {
    fun calculateRisk(reading: BiometricReadingEntity, baseline: BaselineEntity): RiskLevel {
        val zScores = mutableListOf<Float>()

        reading.heartRate?.let { valZ(it, baseline.avgHeartRate, baseline.stdHeartRate)?.let { zScores.add(it) } }
        reading.hrv?.let { valZ(it, baseline.avgHrv, baseline.stdHrv)?.let { zScores.add(it) } }
        reading.sleepHours?.let { valZ(it, baseline.avgSleepHours, baseline.stdSleepHours)?.let { zScores.add(it) } }
        reading.activityLevel?.let { valZ(it, baseline.avgActivityLevel, baseline.stdActivityLevel)?.let { zScores.add(it) } }
        reading.screenUnlocks?.toFloat()?.let { valZ(it, baseline.avgScreenUnlocks, baseline.stdScreenUnlocks)?.let { zScores.add(it) } }
        reading.appUsageMinutes?.toFloat()?.let { valZ(it, baseline.avgAppUsageMinutes, baseline.stdAppUsageMinutes)?.let { zScores.add(it) } }

        if (zScores.isEmpty()) return RiskLevel.NORMAL

        val maxZ = zScores.maxOf { abs(it) }

        return when {
            maxZ < 1.5f -> RiskLevel.NORMAL
            maxZ < 2.0f -> RiskLevel.MILD
            maxZ < 2.5f -> RiskLevel.MODERATE
            else -> RiskLevel.SEVERE
        }
    }

    private fun valZ(current: Float, avg: Float?, std: Float?): Float? {
        if (avg == null || std == null || std == 0f) return null
        return (current - avg) / std
    }
}
