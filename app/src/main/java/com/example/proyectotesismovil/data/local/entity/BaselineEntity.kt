package com.example.proyectotesismovil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baseline")
data class BaselineEntity(
    @PrimaryKey val userId: String,
    val avgHeartRate: Float? = null,
    val stdHeartRate: Float? = null,
    val avgHrv: Float? = null,
    val stdHrv: Float? = null,
    val avgSleepHours: Float? = null,
    val stdSleepHours: Float? = null,
    val avgActivityLevel: Float? = null,
    val stdActivityLevel: Float? = null,
    val avgScreenUnlocks: Float? = null,
    val stdScreenUnlocks: Float? = null,
    val avgAppUsageMinutes: Float? = null,
    val stdAppUsageMinutes: Float? = null,
    val calibrationCompletedAt: Long
)
