package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.data.remote.api.PatientSummaryResponse
import com.example.proyectotesismovil.data.remote.api.SpecialistResponse
import retrofit2.Response

interface VinculacionRepository {
    suspend fun getMyPatients(): Result<List<PatientSummaryResponse>>
    suspend fun getMySpecialist(): Result<SpecialistResponse?>
    suspend fun updateConsent(enabled: Boolean): Result<Unit>
    fun getAllVinculaciones(): kotlinx.coroutines.flow.Flow<List<com.example.proyectotesismovil.data.remote.api.VinculacionResponse>>
    suspend fun desvincular(vinculacionId: Int): Result<Unit>
    suspend fun vincular(pacienteUid: String, especialistaUid: String): Result<Unit>
}
