package com.example.proyectotesismovil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calibration")
data class CalibrationEntity(
    @PrimaryKey val userId: String,
    val startDate: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
