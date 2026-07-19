package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface EmotionalStateApi {
    @POST("emotional-states")
    suspend fun saveEmotionalState(@Body body: EmotionalStateRequest): Response<EmotionalStateResponse>

    @GET("emotional-states/me")
    suspend fun getMyEmotionalStates(@Query("days") days: Int): Response<List<EmotionalStateResponse>>

    @GET("emotional-states/patient/{patientId}")
    suspend fun getPatientEmotionalStates(
        @Path("patientId") patientId: Int,
        @Query("days") days: Int
    ): Response<List<EmotionalStateResponse>>
}

data class EmotionalStateRequest(val emotional_state: String)
data class EmotionalStateResponse(val id: Int, val user_id: Int, val emotional_state: String, val registered_at: String)
