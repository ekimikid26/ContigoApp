package com.example.proyectotesismovil.data.local.dao

import androidx.room.*
import com.example.proyectotesismovil.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CalibrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalibration(calibration: CalibrationEntity)

    @Query("SELECT * FROM calibration WHERE userId = :userId")
    fun getCalibration(userId: String): Flow<CalibrationEntity?>

    @Insert
    suspend fun insertDailySummary(summary: DailyCalibrationEntity)

    @Query("SELECT * FROM daily_calibration WHERE userId = :userId ORDER BY dayNumber ASC")
    suspend fun getDailySummaries(userId: String): List<DailyCalibrationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaseline(baseline: BaselineEntity)

    @Query("SELECT * FROM baseline WHERE userId = :userId")
    suspend fun getBaseline(userId: String): BaselineEntity?
}
