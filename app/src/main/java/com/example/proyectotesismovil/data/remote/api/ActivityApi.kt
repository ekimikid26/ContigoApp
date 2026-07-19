package com.example.proyectotesismovil.data.remote.api

import retrofit2.Response
import retrofit2.http.*

interface ActivityApi {
    @POST("activities")
    suspend fun saveActivity(@Body body: ActivityRequest): Response<ActivityResponse>

    @GET("activities/me")
    suspend fun getMyActivities(
        @Query("days") days: Int,
        @Query("limit") limit: Int
    ): Response<List<ActivityResponse>>
}

data class ActivityRequest(val activity_type: String, val duration_seconds: Int)
data class ActivityResponse(val id: Int, val user_id: Int, val activity_type: String, val completed_at: String, val duration_seconds: Int)
