package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.data.remote.api.NotaResponse

interface NotaRepository {
    suspend fun saveNota(pacienteId: Int, texto: String): Result<NotaResponse>
    suspend fun getPatientNotas(pacienteId: Int): Result<List<NotaResponse>>
}
