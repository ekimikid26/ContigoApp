package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AlertRepositoryImpl(private val api: AlertApi) : AlertRepository {

    override suspend fun saveAlert(riskLevel: String): Result<AlertResponse> {
        return try {
            val response = api.saveAlert(AlertRequest(riskLevel))
            if (response.isSuccessful && response.body() != null) Result.success(response.body()!!)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMyAlerts(days: Int): Flow<List<AlertResponse>> = flow {
        try {
            val response = api.getMyAlerts(days)
            if (response.isSuccessful) emit(response.body() ?: emptyList())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getPatientAlerts(patientId: Int): Flow<List<AlertResponse>> = flow {
        try {
            val response = api.getPatientAlerts(patientId)
            if (response.isSuccessful) emit(response.body() ?: emptyList())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
