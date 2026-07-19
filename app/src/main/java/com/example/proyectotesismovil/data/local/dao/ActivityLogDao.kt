package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.ActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Insert
    suspend fun insertLog(log: ActivityLogEntity)

    @Query("SELECT * FROM activity_logs ORDER BY completedAt DESC")
    fun getAllLogs(): Flow<List<ActivityLogEntity>>

    @Query("""
        SELECT * FROM activity_logs 
        WHERE userId = :userId 
        ORDER BY completedAt DESC 
        LIMIT :limit
    """)
    fun getRecentActivities(
        userId: String,
        limit: Int = 10
    ): Flow<List<ActivityLogEntity>>

    @Query("""
        SELECT activityType
        FROM activity_logs 
        WHERE userId = :userId 
        AND completedAt >= :sevenDaysAgo
        GROUP BY activityType 
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """)
    suspend fun getMostUsedActivity(
        userId: String,
        sevenDaysAgo: Long
    ): String?

    @Query("""
        SELECT COUNT(DISTINCT date(completedAt/1000, 'unixepoch')) 
        FROM activity_logs 
        WHERE userId = :userId 
        AND completedAt >= :sevenDaysAgo
    """)
    suspend fun getActiveDays(
        userId: String,
        sevenDaysAgo: Long
    ): Int
}
