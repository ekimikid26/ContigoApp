package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.local.dao.EmotionalStateDao
import com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity
import com.example.proyectotesismovil.data.remote.api.EmotionalStateApi
import com.example.proyectotesismovil.data.remote.api.EmotionalStateRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class EmotionalStateRepositoryImpl(
    private val api: EmotionalStateApi,
    private val dao: EmotionalStateDao
) {
    suspend fun saveEmotionalState(state: EmotionalStateEntity) {
        dao.insertEmotionalState(state)
        try {
            api.saveEmotionalState(EmotionalStateRequest(state.emotionalState))
        } catch (e: Exception) {
            // Log sync failure
        }
    }

    fun getMyEmotionalStates(days: Int): Flow<List<EmotionalStateEntity>> = flow {
        // Implement offline-first logic similar to BiometricRepository
        emit(emptyList())
    }
}
