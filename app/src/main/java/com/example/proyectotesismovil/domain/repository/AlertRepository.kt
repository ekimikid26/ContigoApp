package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.data.remote.api.AlertResponse
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    suspend fun saveAlert(riskLevel: String): Result<AlertResponse>
    fun getMyAlerts(days: Int): Flow<List<AlertResponse>>
    fun getPatientAlerts(patientId: Int): Flow<List<AlertResponse>>
}
