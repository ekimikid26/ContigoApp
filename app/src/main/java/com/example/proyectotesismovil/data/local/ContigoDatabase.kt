package com.example.proyectotesismovil.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectotesismovil.data.local.dao.*
import com.example.proyectotesismovil.data.local.entity.*
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        BiometricReadingEntity::class,
        ActivityLogEntity::class,
        ReflectionEntity::class,
        EmotionalStateEntity::class,
        GratitudeEntity::class,
        CalibrationEntity::class,
        DailyCalibrationEntity::class,
        BaselineEntity::class,
        Gad7ResultEntity::class,
        UserProfileEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class ContigoDatabase : RoomDatabase() {
    abstract fun biomarkerDao(): BiomarkerDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun emotionalStateDao(): EmotionalStateDao
    abstract fun gratitudeDao(): GratitudeDao
    abstract fun calibrationDao(): CalibrationDao
    abstract fun gad7Dao(): Gad7Dao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        private const val TAG = "ContigoDatabase"
        
        @Volatile
        private var INSTANCE: ContigoDatabase? = null

        val MIGRATION_REMOVE_KEYSTROKE = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE biometric_readings_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId TEXT NOT NULL,
                        heartRate REAL,
                        hrv REAL,
                        spO2 REAL,
                        stressLevel REAL,
                        sleepHours REAL,
                        activityLevel REAL,
                        screenUnlocks INTEGER,
                        appUsageMinutes INTEGER,
                        timestamp INTEGER NOT NULL,
                        syncedToServer INTEGER NOT NULL DEFAULT 0
                    )
                """)
                db.execSQL("""
                    INSERT INTO biometric_readings_new 
                    SELECT id, userId, heartRate, hrv, spO2, stressLevel,
                    sleepHours, activityLevel, screenUnlocks, appUsageMinutes,
                    timestamp, syncedToServer
                    FROM biometric_readings
                """)
                db.execSQL("DROP TABLE biometric_readings")
                db.execSQL("ALTER TABLE biometric_readings_new RENAME TO biometric_readings")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS gad7_results (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId TEXT NOT NULL,
                        q1 INTEGER NOT NULL, q2 INTEGER NOT NULL, q3 INTEGER NOT NULL,
                        q4 INTEGER NOT NULL, q5 INTEGER NOT NULL, q6 INTEGER NOT NULL,
                        q7 INTEGER NOT NULL, totalScore INTEGER NOT NULL,
                        severityLevel TEXT NOT NULL, appliedAt INTEGER NOT NULL,
                        syncedToServer INTEGER NOT NULL DEFAULT 0
                    )
                """)
            }
        }

        val MIGRATION_DB_RENAME = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Room maneja el renombre
            }
        }

        val MIGRATION_ADD_USER_PROFILE = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS user_profile (
                        uid TEXT PRIMARY KEY NOT NULL,
                        nombre TEXT NOT NULL,
                        correo TEXT NOT NULL,
                        rol TEXT NOT NULL,
                        edad INTEGER
                    )
                """)
            }
        }

        fun getDatabase(context: Context): ContigoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context): ContigoDatabase {
            return try {
                val passphrase = KeystoreManager.getOrCreateDatabaseKey(context.applicationContext)
                val factory = SupportFactory(passphrase)
                Room.databaseBuilder(
                    context.applicationContext,
                    ContigoDatabase::class.java,
                    "contigo_database"
                )
                    .openHelperFactory(factory)
                    .addMigrations(MIGRATION_REMOVE_KEYSTROKE, MIGRATION_4_5, MIGRATION_DB_RENAME, MIGRATION_ADD_USER_PROFILE)
                    .build()
            } catch (e: Exception) {
                Log.e(TAG, "Fallo al construir la base de datos segura, reintentando con limpieza", e)
                // Si falla por corrupción de clave, Room suele lanzar una excepción al intentar abrirla.
                // Aquí forzamos una recreación si el error persiste.
                Room.databaseBuilder(
                    context.applicationContext,
                    ContigoDatabase::class.java,
                    "contigo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}
