package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.GratitudeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeDao {
    @Insert
    suspend fun insertGratitude(gratitude: GratitudeEntity)

    @Query("SELECT * FROM gratitude_entries ORDER BY createdAt DESC")
    fun getAllGratitudeEntries(): Flow<List<GratitudeEntity>>
}
