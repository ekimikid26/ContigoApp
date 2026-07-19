package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface EmotionalActivityRepository {
    suspend fun saveActivityLog(log: ActivityLogEntity)
    fun getAllActivityLogs(): Flow<List<ActivityLogEntity>>

    suspend fun saveReflection(reflection: ReflectionEntity)
    fun getLatestReflections(): Flow<List<ReflectionEntity>>
    suspend fun clearReflections()

    suspend fun saveEmotionalState(state: EmotionalStateEntity)
    fun getLastEmotionalState(): Flow<EmotionalStateEntity?>

    suspend fun saveGratitude(gratitude: GratitudeEntity)
    fun getAllGratitudeEntries(): Flow<List<GratitudeEntity>>
}
