package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface VinculacionApi {
    @GET("vinculaciones/my-patients")
    suspend fun getMyPatients(): Response<List<PatientSummaryResponse>>

    @GET("vinculaciones/my-specialist")
    suspend fun getMySpecialist(): Response<SpecialistResponse>

    @POST("vinculaciones")
    suspend fun createVinculacion(@Body body: VinculacionRequest): Response<VinculacionResponse>

    @DELETE("vinculaciones/{vinculacionId}")
    suspend fun deleteVinculacion(@Path("vinculacionId") vinculacionId: Int): Response<MessageResponse>

    @PUT("vinculaciones/consent")
    suspend fun updateConsent(@Body body: ConsentRequest): Response<MessageResponse>

    @GET("vinculaciones/all")
    suspend fun getAllVinculaciones(): Response<List<VinculacionResponse>>

    @POST("vinculaciones/vincular")
    suspend fun vincular(@Body body: VincularRequest): Response<Unit>
}

data class VincularRequest(val paciente_uid: String, val especialista_uid: String)
data class PatientSummaryResponse(val id: Int, val uid: String, val nombre: String, val last_emotion: String?)
data class SpecialistResponse(val id: Int, val uid: String, val nombre: String, val especialidad: String?)
data class VinculacionRequest(val paciente_id: Int, val especialista_id: Int)
data class VinculacionResponse(val id: Int, val paciente_id: Int, val especialista_id: Int, val fecha_vinculacion: String, val activa: Boolean, val consentimiento_dado: Boolean)
data class ConsentRequest(val consentimiento_dado: Boolean)
