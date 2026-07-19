package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.repository.VinculacionRepository
import retrofit2.Response

class VinculacionRepositoryImpl(private val api: VinculacionApi) : VinculacionRepository {

    override suspend fun getMyPatients(): Result<List<PatientSummaryResponse>> {
        return try {
            val response = api.getMyPatients()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMySpecialist(): Result<SpecialistResponse?> {
        return try {
            val response = api.getMySpecialist()
            if (response.isSuccessful) Result.success(response.body())
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateConsent(enabled: Boolean): Result<Unit> {
        return try {
            val response = api.updateConsent(ConsentRequest(enabled))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllVinculaciones(): kotlinx.coroutines.flow.Flow<List<VinculacionResponse>> = kotlinx.coroutines.flow.flow {
        try {
            val response = api.getAllVinculaciones()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun desvincular(vinculacionId: Int): Result<Unit> {
        return try {
            val response = api.deleteVinculacion(vinculacionId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun vincular(pacienteUid: String, especialistaUid: String): Result<Unit> {
        return try {
            val response = api.vincular(VincularRequest(pacienteUid, especialistaUid))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
