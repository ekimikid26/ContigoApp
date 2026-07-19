package com.example.proyectotesismovil.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users/me")
    suspend fun getMe(): Response<UserResponse>

    @PUT("users/me")
    suspend fun updateMe(@Body body: UpdateUserRequest): Response<UserResponse>

    @GET("users/all")
    suspend fun getAllUsers(
        @Query("rol") rol: String?,
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<UserResponse>>

    @PUT("users/{userId}/status")
    suspend fun updateUserStatus(
        @Path("userId") userId: Int,
        @Body body: StatusRequest
    ): Response<UserResponse>

    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<MessageResponse>

    @PUT("users/{uid}/admin-update")
    suspend fun updateOtherUser(
        @Path("uid") uid: String,
        @Body body: UpdateOtherUserRequest
    ): Response<Unit>
}

data class UpdateOtherUserRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("activo") val activo: Boolean
)

data class UpdateUserRequest(
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("edad") val edad: Int?
)

data class StatusRequest(
    @SerializedName("activo") val activo: Boolean
)

data class MessageResponse(
    @SerializedName("message") val message: String
)
