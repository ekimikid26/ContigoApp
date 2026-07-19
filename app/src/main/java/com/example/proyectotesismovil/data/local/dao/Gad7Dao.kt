package com.example.proyectotesismovil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectotesismovil.data.local.entity.Gad7ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Gad7Dao {
    @Insert
    suspend fun insertResult(result: Gad7ResultEntity)

    @Query("""
        SELECT * FROM gad7_results 
        WHERE userId = :userId 
        ORDER BY appliedAt DESC
    """)
    fun getAllResults(userId: String): Flow<List<Gad7ResultEntity>>

    @Query("""
        SELECT * FROM gad7_results 
        WHERE userId = :userId 
        ORDER BY appliedAt DESC 
        LIMIT 1
    """)
    suspend fun getLatestResult(userId: String): Gad7ResultEntity?
}
