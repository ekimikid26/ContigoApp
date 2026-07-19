package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.data.remote.api.CalibrationStatusResponse

interface CalibrationRepository {
    suspend fun getCalibrationStatus(): Result<CalibrationStatusResponse>
    suspend fun saveCalibrationData(data: Map<String, Any>): Result<Unit>
    suspend fun completeCalibration(): Result<Unit>
}
