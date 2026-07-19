package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface NotaApi {
    @POST("notas")
    suspend fun saveNota(@Body body: NotaRequest): Response<NotaResponse>

    @GET("notas/patient/{patientId}")
    suspend fun getPatientNotas(@Path("patientId") patientId: Int): Response<List<NotaResponse>>
}

data class NotaRequest(val paciente_id: Int, val texto: String)
data class NotaResponse(val id: Int, val especialista_id: Int, val paciente_id: Int, val texto: String, val created_at: String)
