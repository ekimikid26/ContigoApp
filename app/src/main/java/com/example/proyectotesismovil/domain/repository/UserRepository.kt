package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getMe(): Result<User>
    suspend fun updateMe(nombre: String?, edad: Int?): Result<User>
    fun getAllUsers(rol: String?, search: String?): Flow<List<User>>
    suspend fun updateUserStatus(userId: Int, active: Boolean): Result<Unit>
    suspend fun deleteUser(userId: Int): Result<Unit>
    suspend fun updateOtherUser(
        uid: String,
        nombre: String,
        correo: String,
        rol: String,
        activo: Boolean
    ): Result<Unit>
}
