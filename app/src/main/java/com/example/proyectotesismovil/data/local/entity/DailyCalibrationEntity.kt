package com.example.proyectotesismovil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_calibration")
data class DailyCalibrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val dayNumber: Int,
    val date: Long,
    val avgHeartRate: Float? = null,
    val avgHrv: Float? = null,
    val sleepHours: Float? = null,
    val avgActivityLevel: Float? = null,
    val avgScreenUnlocks: Float? = null,
    val avgAppUsageMinutes: Float? = null
)
