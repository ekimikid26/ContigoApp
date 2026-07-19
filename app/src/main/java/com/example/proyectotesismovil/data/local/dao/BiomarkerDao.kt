package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BiomarkerDao {
    @Insert
    suspend fun insertBiometricReading(reading: BiometricReadingEntity)

    @Query("SELECT * FROM biometric_readings ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<BiometricReadingEntity>>

    @Query("SELECT * FROM biometric_readings WHERE timestamp >= :startTime ORDER BY timestamp ASC")
    fun getReadingsSince(startTime: Long): Flow<List<BiometricReadingEntity>>

    @Query("SELECT * FROM biometric_readings WHERE syncedToServer = 0")
    suspend fun getUnsyncedReadings(): List<BiometricReadingEntity>

    @Query("UPDATE biometric_readings SET syncedToServer = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Int>)
}
