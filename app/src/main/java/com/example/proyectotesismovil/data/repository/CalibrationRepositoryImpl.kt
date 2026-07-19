package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.repository.CalibrationRepository
import retrofit2.Response

class CalibrationRepositoryImpl(private val api: CalibrationApi) : CalibrationRepository {

    override suspend fun getCalibrationStatus(): Result<CalibrationStatusResponse> {
        return try {
            val response = api.getCalibrationStatus()
            if (response.isSuccessful && response.body() != null) Result.success(response.body()!!)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveCalibrationData(data: Map<String, Any>): Result<Unit> {
        return try {
            val response = api.saveCalibrationData(CalibrationDataRequest(data))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeCalibration(): Result<Unit> {
        return try {
            val response = api.completeCalibration()
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
