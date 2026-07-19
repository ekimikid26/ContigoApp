package com.example.proyectotesismovil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gad7_results")
data class Gad7ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val q1: Int, val q2: Int, val q3: Int, val q4: Int,
    val q5: Int, val q6: Int, val q7: Int,
    val totalScore: Int,
    val severityLevel: String,
    val appliedAt: Long = System.currentTimeMillis(),
    val syncedToServer: Boolean = false
)
