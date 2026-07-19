package com.example.proyectotesismovil.data.remote.api

import com.example.proyectotesismovil.data.remote.model.RegisterRequest
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<RefreshResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<MessageResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<MessageResponse>
}

// Data models for requests and responses
data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)

data class ForgotPasswordRequest(
    @SerializedName("correo") val correo: String
)

data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("new_password") val newPassword: String
)

data class AuthResponse(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("user") val user: UserResponse
)

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("uid") val uid: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("edad") val edad: Int? = null,
    @SerializedName("cedula_profesional") val cedulaProfesional: String? = null,
    @SerializedName("cedula_especialidad") val cedulaEspecialidad: String? = null,
    @SerializedName("especialidad") val especialidad: String? = null,
    @SerializedName("tipo_especialidad") val tipoEspecialidad: String? = null,
    @SerializedName("licenciatura_psicologia") val institucionLicenciatura: String? = null,
    @SerializedName("institucion") val institucion: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("lista_medicamentos") val listaMedicamentos: String? = null,
    @SerializedName("alergias") val alergias: String? = null,
    @SerializedName("plan_tratamiento") val planTratamiento: String? = null,
    @SerializedName("historial_medico") val historialMedico: String? = null,
    @SerializedName("especialista_asignado") val especialistaAsignado: String? = null,
    @SerializedName("subscription_plan") val subscriptionPlan: String? = null,
    @SerializedName("activo") val activo: Boolean? = true,
    @SerializedName("fecha_registro") val fechaRegistro: String? = null,
    @SerializedName("ultimo_acceso") val ultimoAcceso: String? = null
)

data class RefreshRequest(
    @SerializedName("refresh_token") val refresh_token: String
)

data class RefreshResponse(
    @SerializedName("access_token") val access_token: String
)
