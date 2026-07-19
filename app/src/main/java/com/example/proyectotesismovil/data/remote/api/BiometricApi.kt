package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface BiometricApi {
    @POST("biometrics")
    suspend fun saveBiometric(@Body body: BiometricRequest): Response<BiometricResponse>

    @GET("biometrics/me")
    suspend fun getMyBiometrics(@Query("days") days: Int): Response<List<BiometricResponse>>

    @GET("biometrics/patient/{patientId}")
    suspend fun getPatientBiometrics(
        @Path("patientId") patientId: Int,
        @Query("days") days: Int
    ): Response<List<BiometricResponse>>
}

data class BiometricRequest(
    val heart_rate: Float?,
    val hrv: Float?,
    val spo2: Float?,
    val stress_level: Float?,
    val sleep_hours: Float?,
    val activity_level: Float?,
    val screen_unlocks: Int?,
    val app_usage_minutes: Int?
)

data class BiometricResponse(
    val id: Int,
    val user_id: Int,
    val heart_rate: Float?,
    val hrv: Float?,
    val spo2: Float?,
    val stress_level: Float?,
    val sleep_hours: Float?,
    val activity_level: Float?,
    val screen_unlocks: Int?,
    val app_usage_minutes: Int?,
    val timestamp: String
)
