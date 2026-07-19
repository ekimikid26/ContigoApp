package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val api: UserApi) : UserRepository {

    override suspend fun getMe(): Result<User> {
        return try {
            val response = api.getMe()
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapToDomain(response.body()!!))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMe(nombre: String?, edad: Int?): Result<User> {
        return try {
            val response = api.updateMe(UpdateUserRequest(nombre, edad))
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapToDomain(response.body()!!))
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllUsers(rol: String?, search: String?): Flow<List<User>> = flow {
        try {
            val response = api.getAllUsers(rol, search, 1, 100)
            if (response.isSuccessful) {
                emit(response.body()?.map { mapToDomain(it) } ?: emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun updateUserStatus(userId: Int, active: Boolean): Result<Unit> {
        return try {
            val response = api.updateUserStatus(userId, StatusRequest(active))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: Int): Result<Unit> {
        return try {
            val response = api.deleteUser(userId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateOtherUser(
        uid: String,
        nombre: String,
        correo: String,
        rol: String,
        activo: Boolean
    ): Result<Unit> {
        return try {
            val response = api.updateOtherUser(
                uid,
                UpdateOtherUserRequest(nombre, correo, rol, activo)
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al actualizar usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapToDomain(apiUser: UserResponse): User {
        return User(
            id = apiUser.id,
            uid = apiUser.uid,
            nombre = apiUser.nombre,
            correo = apiUser.correo,
            rol = apiUser.rol,
            edad = apiUser.edad,
            cedulaProfesional = apiUser.cedulaProfesional,
            cedulaEspecialidad = apiUser.cedulaEspecialidad,
            tipoEspecialidad = apiUser.especialidad 
                ?: apiUser.tipoEspecialidad,
            institucionActual = apiUser.institucion 
                ?: apiUser.institucionLicenciatura,
            telefono = apiUser.telefono,
            medicamentos = apiUser.listaMedicamentos,
            alergias = apiUser.alergias,
            planTratamiento = apiUser.planTratamiento,
            historialMedico = apiUser.historialMedico,
            especialistaAsignado = apiUser.especialistaAsignado,
            subscriptionPlan = apiUser.subscriptionPlan,
            activo = apiUser.activo ?: true
        )
    }
}
