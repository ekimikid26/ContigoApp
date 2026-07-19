package com.example.proyectotesismovil.data.local.dao

import androidx.room.*
import com.example.proyectotesismovil.data.local.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUser(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun deleteAll()
}
