package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.TokenManager
import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.domain.repository.AuthRepository
import com.example.proyectotesismovil.data.remote.model.RegisterRequest
import com.example.proyectotesismovil.data.notification.Gad7ReminderWorker
import kotlinx.coroutines.flow.*
import retrofit2.Response
import android.util.Log
import com.example.proyectotesismovil.BuildConfig

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val tokenManager: TokenManager,
    private val database: ContigoDatabase,
    private val context: android.content.Context
) : AuthRepository {

    private val TAG = "AuthRepository"

    override suspend fun login(email: String, password: String): Result<User> {
        Log.d(TAG, "Attempting login for $email with BASE_URL: ${BuildConfig.BASE_URL}")
        return try {
            val response = authApi.login(LoginRequest(email, password))
            when {
                response.isSuccessful -> {
                    val authResponse = response.body() ?: throw Exception("Cuerpo de respuesta nulo")
                    tokenManager.saveAuthData(
                        authResponse.access_token,
                        authResponse.refresh_token,
                        authResponse.user.id,
                        authResponse.user.uid
                    )
                    val user = mapToDomain(authResponse.user)
                    try {
                        database.userProfileDao().insertOrUpdate(
                            com.example.proyectotesismovil.data.local.entity.UserProfileEntity(
                                uid = user.uid,
                                nombre = user.nombre,
                                correo = user.correo,
                                rol = user.rol,
                                edad = user.edad
                            )
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error saving to local cache", e)
                    }
                    Result.success(user)
                }
                response.code() == 401 -> Result.failure(Exception("Correo o contraseña incorrectos."))
                response.code() == 403 -> Result.failure(Exception("Tu cuenta está desactivada. Contacta al administrador."))
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Login server error: ${response.code()} - $errorBody")
                    Result.failure(Exception("Error del servidor (${response.code()})"))
                }
            }
        } catch (e: java.io.IOException) {
            Log.e(TAG, "Network error during login", e)
            Result.failure(Exception("Error de conexión: No se pudo alcanzar el servidor. Verifica tu internet y que el servidor en Railway esté activo."))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected login error", e)
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    override suspend fun register(user: User, password: String): Result<Unit> {
        return try {
            val request = RegisterRequest(
                nombre = user.nombre,
                correo = user.correo,
                password = password,
                rol = user.rol,
                edad = user.edad,
                sexo = user.sexo,
                emergenciaNombre = user.emergenciaNombre,
                emergenciaTel = user.emergenciaTel,
                medicamentos = user.medicamentos,
                alergias = user.alergias,
                planTratamiento = user.planTratamiento,
                cedulaProfesional = user.cedulaProfesional,
                institucionLicenciatura = user.licenciaturaInstitucion,
                cedulaEspecialidad = user.cedulaEspecialidad,
                tipoEspecialidad = user.tipoEspecialidad,
                aniosExperiencia = user.aniosExperiencia,
                institucion = user.institucionActual,
                enfoqueTerapeutico = user.enfoqueTerapeutico,
                telefono = user.telefono,
                historialMedico = user.historialMedico
            )

            val response = authApi.register(request)
            when {
                response.isSuccessful -> {
                    val body = response.body() ?: throw Exception("Cuerpo de respuesta nulo")
                    tokenManager.saveAuthData(
                        body.access_token,
                        body.refresh_token,
                        body.user.id,
                        body.user.uid
                    )
                    if (user.rol == "paciente") {
                        try {
                            Gad7ReminderWorker.schedule(context)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error scheduling Gad7 worker", e)
                        }
                    }
                    Result.success(Unit)
                }
                response.code() == 400 -> Result.failure(Exception("Este correo ya está registrado."))
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error en el registro (${response.code()})"))
                }
            }
        } catch (e: java.io.IOException) {
            Result.failure(Exception("Error de conexión con el servidor."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val response = authApi.forgotPassword(ForgotPasswordRequest(correo = email))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al enviar correo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            val response = authApi.resetPassword(ResetPasswordRequest(token, newPassword))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al resetear contraseña"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            val response = userApi.getMe()
            if (response.isSuccessful) response.body()?.let { mapToDomain(it) } else null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting current user", e)
            null
        }
    }

    override suspend fun getUserByUid(uid: String): User? = null

    override fun observeAuthState(): Flow<User?> = flow {
        val token = tokenManager.getAccessToken().firstOrNull()
        if (token == null) {
            emit(null)
            return@flow
        }

        // Primero emitir datos locales si existen
        val localUser = try {
            database.userProfileDao().getUser()
        } catch (e: Exception) { null }
        
        if (localUser != null) {
            emit(User(
                uid = localUser.uid,
                nombre = localUser.nombre,
                correo = localUser.correo,
                rol = localUser.rol,
                edad = localUser.edad
            ))
        }

        // Luego obtener datos frescos del backend
        try {
            val response = userApi.getMe()
            if (response.isSuccessful) {
                val apiUser = response.body() ?: return@flow
                val user = mapToDomain(apiUser)
                try {
                    database.userProfileDao().insertOrUpdate(
                        com.example.proyectotesismovil.data.local.entity.UserProfileEntity(
                            uid = user.uid,
                            nombre = user.nombre,
                            correo = user.correo,
                            rol = user.rol,
                            edad = user.edad
                        )
                    )
                } catch (e: Exception) { }
                emit(user)
            } else if (response.code() == 401) {
                tokenManager.clearTokens()
                emit(null)
            }
        } catch (e: Exception) {
            if (localUser == null) emit(null)
        }
    }.catch { 
        Log.e(TAG, "Error in observeAuthState", it)
        emit(null) 
    }

    override suspend fun logout() {
        try {
            tokenManager.clearTokens()
            database.userProfileDao().deleteAll()
            database.clearAllTables()
        } catch (e: Exception) {
            Log.e(TAG, "Error during logout", e)
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

    override suspend fun updateProfile(uid: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            val nombre = updates["nombre"] as? String
            val response = userApi.updateMe(UpdateUserRequest(nombre, null))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al actualizar perfil"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUserData(uid: String): Result<Unit> = Result.success(Unit)

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = getCurrentUser() ?: return Result.failure(Exception("No hay usuario"))
            val response = userApi.deleteUser(user.id)
            if (response.isSuccessful) {
                logout()
                Result.success(Unit)
            } else Result.failure(Exception("Error al borrar cuenta"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleDataCollection(uid: String, paused: Boolean): Result<Unit> = Result.success(Unit)

    override suspend fun updateDataSharingConsent(patientUid: String, enabled: Boolean): Result<Unit> = Result.success(Unit)

    override fun observeDataSharingConsent(patientUid: String): Flow<Boolean> = flowOf(false)

    override fun getPatientsForSpecialist(specialistUid: String): Flow<List<User>> = flowOf(emptyList())

    override fun getAllUsers(): Flow<List<User>> = flowOf(emptyList())

    override suspend fun updateUserStatus(uid: String, active: Boolean): Result<Unit> {
        return try {
            val user = getCurrentUser() ?: return Result.failure(Exception("No hay usuario"))
            val response = userApi.updateUserStatus(user.id, StatusRequest(active))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al actualizar estado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun createInvitation(specialistUid: String, specialistNombre: String, patientEmail: String): Result<String> = Result.success("")
    override suspend fun acceptInvitation(patientUid: String, patientEmail: String, code: String): Result<Unit> = Result.success(Unit)
    override fun getInvitationsForSpecialist(specialistUid: String): Flow<List<com.example.proyectotesismovil.domain.model.Invitacion>> = flowOf(emptyList())
}
