package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface CalibrationApi {
    @GET("calibration/status")
    suspend fun getCalibrationStatus(): Response<CalibrationStatusResponse>

    @POST("calibration/data")
    suspend fun saveCalibrationData(@Body body: CalibrationDataRequest): Response<MessageResponse>

    @PUT("calibration/complete")
    suspend fun completeCalibration(): Response<MessageResponse>
}

data class CalibrationStatusResponse(val calibration_completed: Boolean, val baseline: Map<String, Float>?)
data class CalibrationDataRequest(val data: Map<String, Any>)
