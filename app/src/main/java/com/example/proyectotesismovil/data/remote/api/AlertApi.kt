package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface AlertApi {
    @POST("alerts")
    suspend fun saveAlert(@Body body: AlertRequest): Response<AlertResponse>

    @GET("alerts/me")
    suspend fun getMyAlerts(@Query("days") days: Int): Response<List<AlertResponse>>

    @GET("alerts/patient/{patientId}")
    suspend fun getPatientAlerts(@Path("patientId") patientId: Int): Response<List<AlertResponse>>
}

data class AlertRequest(val risk_level: String)
data class AlertResponse(val id: Int, val user_id: Int, val risk_level: String, val generated_at: String, val acknowledged: Boolean)
