package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionalStateDao {
    @Insert
    suspend fun insertEmotionalState(state: EmotionalStateEntity)

    @Query("SELECT * FROM emotional_states ORDER BY registeredAt DESC LIMIT 1")
    fun getLastEmotionalState(): Flow<EmotionalStateEntity?>

    @Query("""
        SELECT * FROM emotional_states 
        WHERE userId = :userId 
        AND registeredAt >= :sevenDaysAgo 
        ORDER BY registeredAt ASC
    """)
    fun getLastSevenDays(
        userId: String,
        sevenDaysAgo: Long
    ): Flow<List<EmotionalStateEntity>>

    @Query("""
        SELECT emotionalState
        FROM emotional_states 
        WHERE userId = :userId 
        AND registeredAt >= :sevenDaysAgo
        GROUP BY emotionalState 
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """)
    suspend fun getMostFrequentEmotion(
        userId: String,
        sevenDaysAgo: Long
    ): String?

    @Query("""
        SELECT COUNT(*) FROM emotional_states 
        WHERE userId = :userId 
        AND registeredAt >= :sevenDaysAgo
    """)
    suspend fun getTotalRegistrations(
        userId: String,
        sevenDaysAgo: Long
    ): Int
}
