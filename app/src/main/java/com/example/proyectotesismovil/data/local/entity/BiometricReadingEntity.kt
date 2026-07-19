package com.example.proyectotesismovil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "biometric_readings")
data class BiometricReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val heartRate: Float? = null,
    val hrv: Float? = null,
    val spO2: Float? = null,
    val stressLevel: Float? = null,
    val sleepHours: Float? = null,
    val activityLevel: Float? = null,
    val screenUnlocks: Int? = null,
    val appUsageMinutes: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val syncedToServer: Boolean = false
)
