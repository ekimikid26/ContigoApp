package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.local.dao.ActivityLogDao
import com.example.proyectotesismovil.data.local.entity.ActivityLogEntity
import com.example.proyectotesismovil.data.remote.api.ActivityApi
import com.example.proyectotesismovil.data.remote.api.ActivityRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ActivityRepositoryImpl(
    private val api: ActivityApi,
    private val dao: ActivityLogDao
) {
    suspend fun saveActivity(activity: ActivityLogEntity) {
        dao.insertLog(activity)
        try {
            api.saveActivity(ActivityRequest(activity.activityType, activity.durationSeconds))
        } catch (e: Exception) {
            // Log sync failure
        }
    }

    fun getMyActivities(days: Int): Flow<List<ActivityLogEntity>> = flow {
        // Implement offline-first
        emit(emptyList())
    }
}
