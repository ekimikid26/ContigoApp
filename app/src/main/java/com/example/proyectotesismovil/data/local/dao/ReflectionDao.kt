package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.ReflectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {
    @Insert
    suspend fun insertReflection(reflection: ReflectionEntity)

    @Query("SELECT * FROM reflections ORDER BY createdAt DESC LIMIT 3")
    fun getLatestReflections(): Flow<List<ReflectionEntity>>
    
    @Query("DELETE FROM reflections")
    suspend fun clearReflections()
}
