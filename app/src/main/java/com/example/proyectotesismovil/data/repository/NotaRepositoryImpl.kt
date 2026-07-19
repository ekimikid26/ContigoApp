package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.repository.NotaRepository
import retrofit2.Response

class NotaRepositoryImpl(private val api: NotaApi) : NotaRepository {

    override suspend fun saveNota(pacienteId: Int, texto: String): Result<NotaResponse> {
        return try {
            val response = api.saveNota(NotaRequest(pacienteId, texto))
            if (response.isSuccessful && response.body() != null) Result.success(response.body()!!)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPatientNotas(pacienteId: Int): Result<List<NotaResponse>> {
        return try {
            val response = api.getPatientNotas(pacienteId)
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
