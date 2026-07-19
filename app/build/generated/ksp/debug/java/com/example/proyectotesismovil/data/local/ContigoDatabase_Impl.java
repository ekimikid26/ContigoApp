package com.example.proyectotesismovil.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.proyectotesismovil.data.local.dao.ActivityLogDao;
import com.example.proyectotesismovil.data.local.dao.ActivityLogDao_Impl;
import com.example.proyectotesismovil.data.local.dao.BiomarkerDao;
import com.example.proyectotesismovil.data.local.dao.BiomarkerDao_Impl;
import com.example.proyectotesismovil.data.local.dao.CalibrationDao;
import com.example.proyectotesismovil.data.local.dao.CalibrationDao_Impl;
import com.example.proyectotesismovil.data.local.dao.EmotionalStateDao;
import com.example.proyectotesismovil.data.local.dao.EmotionalStateDao_Impl;
import com.example.proyectotesismovil.data.local.dao.Gad7Dao;
import com.example.proyectotesismovil.data.local.dao.Gad7Dao_Impl;
import com.example.proyectotesismovil.data.local.dao.GratitudeDao;
import com.example.proyectotesismovil.data.local.dao.GratitudeDao_Impl;
import com.example.proyectotesismovil.data.local.dao.ReflectionDao;
import com.example.proyectotesismovil.data.local.dao.ReflectionDao_Impl;
import com.example.proyectotesismovil.data.local.dao.UserProfileDao;
import com.example.proyectotesismovil.data.local.dao.UserProfileDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ContigoDatabase_Impl extends ContigoDatabase {
  private volatile BiomarkerDao _biomarkerDao;

  private volatile ActivityLogDao _activityLogDao;

  private volatile ReflectionDao _reflectionDao;

  private volatile EmotionalStateDao _emotionalStateDao;

  private volatile GratitudeDao _gratitudeDao;

  private volatile CalibrationDao _calibrationDao;

  private volatile Gad7Dao _gad7Dao;

  private volatile UserProfileDao _userProfileDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(7) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `biometric_readings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `heartRate` REAL, `hrv` REAL, `spO2` REAL, `stressLevel` REAL, `sleepHours` REAL, `activityLevel` REAL, `screenUnlocks` INTEGER, `appUsageMinutes` INTEGER, `timestamp` INTEGER NOT NULL, `syncedToServer` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `activity_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `activityType` TEXT NOT NULL, `completedAt` INTEGER NOT NULL, `durationSeconds` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reflections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `emotional_states` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `emotionalState` TEXT NOT NULL, `registeredAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `gratitude_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `calibration` (`userId` TEXT NOT NULL, `startDate` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `completedAt` INTEGER, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_calibration` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `dayNumber` INTEGER NOT NULL, `date` INTEGER NOT NULL, `avgHeartRate` REAL, `avgHrv` REAL, `sleepHours` REAL, `avgActivityLevel` REAL, `avgScreenUnlocks` REAL, `avgAppUsageMinutes` REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `baseline` (`userId` TEXT NOT NULL, `avgHeartRate` REAL, `stdHeartRate` REAL, `avgHrv` REAL, `stdHrv` REAL, `avgSleepHours` REAL, `stdSleepHours` REAL, `avgActivityLevel` REAL, `stdActivityLevel` REAL, `avgScreenUnlocks` REAL, `stdScreenUnlocks` REAL, `avgAppUsageMinutes` REAL, `stdAppUsageMinutes` REAL, `calibrationCompletedAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `gad7_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `q1` INTEGER NOT NULL, `q2` INTEGER NOT NULL, `q3` INTEGER NOT NULL, `q4` INTEGER NOT NULL, `q5` INTEGER NOT NULL, `q6` INTEGER NOT NULL, `q7` INTEGER NOT NULL, `totalScore` INTEGER NOT NULL, `severityLevel` TEXT NOT NULL, `appliedAt` INTEGER NOT NULL, `syncedToServer` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`uid` TEXT NOT NULL, `nombre` TEXT NOT NULL, `correo` TEXT NOT NULL, `rol` TEXT NOT NULL, `edad` INTEGER, PRIMARY KEY(`uid`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '632d4c6acab2eebf112a064af37926dc')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `biometric_readings`");
        db.execSQL("DROP TABLE IF EXISTS `activity_logs`");
        db.execSQL("DROP TABLE IF EXISTS `reflections`");
        db.execSQL("DROP TABLE IF EXISTS `emotional_states`");
        db.execSQL("DROP TABLE IF EXISTS `gratitude_entries`");
        db.execSQL("DROP TABLE IF EXISTS `calibration`");
        db.execSQL("DROP TABLE IF EXISTS `daily_calibration`");
        db.execSQL("DROP TABLE IF EXISTS `baseline`");
        db.execSQL("DROP TABLE IF EXISTS `gad7_results`");
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBiometricReadings = new HashMap<String, TableInfo.Column>(12);
        _columnsBiometricReadings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("heartRate", new TableInfo.Column("heartRate", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("hrv", new TableInfo.Column("hrv", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("spO2", new TableInfo.Column("spO2", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("stressLevel", new TableInfo.Column("stressLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("sleepHours", new TableInfo.Column("sleepHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("activityLevel", new TableInfo.Column("activityLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("screenUnlocks", new TableInfo.Column("screenUnlocks", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("appUsageMinutes", new TableInfo.Column("appUsageMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBiometricReadings.put("syncedToServer", new TableInfo.Column("syncedToServer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBiometricReadings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBiometricReadings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBiometricReadings = new TableInfo("biometric_readings", _columnsBiometricReadings, _foreignKeysBiometricReadings, _indicesBiometricReadings);
        final TableInfo _existingBiometricReadings = TableInfo.read(db, "biometric_readings");
        if (!_infoBiometricReadings.equals(_existingBiometricReadings)) {
          return new RoomOpenHelper.ValidationResult(false, "biometric_readings(com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity).\n"
                  + " Expected:\n" + _infoBiometricReadings + "\n"
                  + " Found:\n" + _existingBiometricReadings);
        }
        final HashMap<String, TableInfo.Column> _columnsActivityLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsActivityLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActivityLogs.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActivityLogs.put("activityType", new TableInfo.Column("activityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActivityLogs.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActivityLogs.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysActivityLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesActivityLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoActivityLogs = new TableInfo("activity_logs", _columnsActivityLogs, _foreignKeysActivityLogs, _indicesActivityLogs);
        final TableInfo _existingActivityLogs = TableInfo.read(db, "activity_logs");
        if (!_infoActivityLogs.equals(_existingActivityLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "activity_logs(com.example.proyectotesismovil.data.local.entity.ActivityLogEntity).\n"
                  + " Expected:\n" + _infoActivityLogs + "\n"
                  + " Found:\n" + _existingActivityLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsReflections = new HashMap<String, TableInfo.Column>(3);
        _columnsReflections.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflections.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReflections.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReflections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReflections = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReflections = new TableInfo("reflections", _columnsReflections, _foreignKeysReflections, _indicesReflections);
        final TableInfo _existingReflections = TableInfo.read(db, "reflections");
        if (!_infoReflections.equals(_existingReflections)) {
          return new RoomOpenHelper.ValidationResult(false, "reflections(com.example.proyectotesismovil.data.local.entity.ReflectionEntity).\n"
                  + " Expected:\n" + _infoReflections + "\n"
                  + " Found:\n" + _existingReflections);
        }
        final HashMap<String, TableInfo.Column> _columnsEmotionalStates = new HashMap<String, TableInfo.Column>(4);
        _columnsEmotionalStates.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionalStates.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionalStates.put("emotionalState", new TableInfo.Column("emotionalState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmotionalStates.put("registeredAt", new TableInfo.Column("registeredAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmotionalStates = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmotionalStates = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmotionalStates = new TableInfo("emotional_states", _columnsEmotionalStates, _foreignKeysEmotionalStates, _indicesEmotionalStates);
        final TableInfo _existingEmotionalStates = TableInfo.read(db, "emotional_states");
        if (!_infoEmotionalStates.equals(_existingEmotionalStates)) {
          return new RoomOpenHelper.ValidationResult(false, "emotional_states(com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity).\n"
                  + " Expected:\n" + _infoEmotionalStates + "\n"
                  + " Found:\n" + _existingEmotionalStates);
        }
        final HashMap<String, TableInfo.Column> _columnsGratitudeEntries = new HashMap<String, TableInfo.Column>(3);
        _columnsGratitudeEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGratitudeEntries.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGratitudeEntries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGratitudeEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGratitudeEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGratitudeEntries = new TableInfo("gratitude_entries", _columnsGratitudeEntries, _foreignKeysGratitudeEntries, _indicesGratitudeEntries);
        final TableInfo _existingGratitudeEntries = TableInfo.read(db, "gratitude_entries");
        if (!_infoGratitudeEntries.equals(_existingGratitudeEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "gratitude_entries(com.example.proyectotesismovil.data.local.entity.GratitudeEntity).\n"
                  + " Expected:\n" + _infoGratitudeEntries + "\n"
                  + " Found:\n" + _existingGratitudeEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsCalibration = new HashMap<String, TableInfo.Column>(4);
        _columnsCalibration.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalibration.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalibration.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalibration.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCalibration = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCalibration = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCalibration = new TableInfo("calibration", _columnsCalibration, _foreignKeysCalibration, _indicesCalibration);
        final TableInfo _existingCalibration = TableInfo.read(db, "calibration");
        if (!_infoCalibration.equals(_existingCalibration)) {
          return new RoomOpenHelper.ValidationResult(false, "calibration(com.example.proyectotesismovil.data.local.entity.CalibrationEntity).\n"
                  + " Expected:\n" + _infoCalibration + "\n"
                  + " Found:\n" + _existingCalibration);
        }
        final HashMap<String, TableInfo.Column> _columnsDailyCalibration = new HashMap<String, TableInfo.Column>(10);
        _columnsDailyCalibration.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("dayNumber", new TableInfo.Column("dayNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("avgHeartRate", new TableInfo.Column("avgHeartRate", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("avgHrv", new TableInfo.Column("avgHrv", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("sleepHours", new TableInfo.Column("sleepHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("avgActivityLevel", new TableInfo.Column("avgActivityLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("avgScreenUnlocks", new TableInfo.Column("avgScreenUnlocks", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyCalibration.put("avgAppUsageMinutes", new TableInfo.Column("avgAppUsageMinutes", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyCalibration = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyCalibration = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyCalibration = new TableInfo("daily_calibration", _columnsDailyCalibration, _foreignKeysDailyCalibration, _indicesDailyCalibration);
        final TableInfo _existingDailyCalibration = TableInfo.read(db, "daily_calibration");
        if (!_infoDailyCalibration.equals(_existingDailyCalibration)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_calibration(com.example.proyectotesismovil.data.local.entity.DailyCalibrationEntity).\n"
                  + " Expected:\n" + _infoDailyCalibration + "\n"
                  + " Found:\n" + _existingDailyCalibration);
        }
        final HashMap<String, TableInfo.Column> _columnsBaseline = new HashMap<String, TableInfo.Column>(14);
        _columnsBaseline.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgHeartRate", new TableInfo.Column("avgHeartRate", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdHeartRate", new TableInfo.Column("stdHeartRate", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgHrv", new TableInfo.Column("avgHrv", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdHrv", new TableInfo.Column("stdHrv", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgSleepHours", new TableInfo.Column("avgSleepHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdSleepHours", new TableInfo.Column("stdSleepHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgActivityLevel", new TableInfo.Column("avgActivityLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdActivityLevel", new TableInfo.Column("stdActivityLevel", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgScreenUnlocks", new TableInfo.Column("avgScreenUnlocks", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdScreenUnlocks", new TableInfo.Column("stdScreenUnlocks", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("avgAppUsageMinutes", new TableInfo.Column("avgAppUsageMinutes", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("stdAppUsageMinutes", new TableInfo.Column("stdAppUsageMinutes", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBaseline.put("calibrationCompletedAt", new TableInfo.Column("calibrationCompletedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBaseline = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBaseline = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBaseline = new TableInfo("baseline", _columnsBaseline, _foreignKeysBaseline, _indicesBaseline);
        final TableInfo _existingBaseline = TableInfo.read(db, "baseline");
        if (!_infoBaseline.equals(_existingBaseline)) {
          return new RoomOpenHelper.ValidationResult(false, "baseline(com.example.proyectotesismovil.data.local.entity.BaselineEntity).\n"
                  + " Expected:\n" + _infoBaseline + "\n"
                  + " Found:\n" + _existingBaseline);
        }
        final HashMap<String, TableInfo.Column> _columnsGad7Results = new HashMap<String, TableInfo.Column>(13);
        _columnsGad7Results.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q1", new TableInfo.Column("q1", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q2", new TableInfo.Column("q2", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q3", new TableInfo.Column("q3", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q4", new TableInfo.Column("q4", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q5", new TableInfo.Column("q5", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q6", new TableInfo.Column("q6", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("q7", new TableInfo.Column("q7", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("totalScore", new TableInfo.Column("totalScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("severityLevel", new TableInfo.Column("severityLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("appliedAt", new TableInfo.Column("appliedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGad7Results.put("syncedToServer", new TableInfo.Column("syncedToServer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGad7Results = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGad7Results = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGad7Results = new TableInfo("gad7_results", _columnsGad7Results, _foreignKeysGad7Results, _indicesGad7Results);
        final TableInfo _existingGad7Results = TableInfo.read(db, "gad7_results");
        if (!_infoGad7Results.equals(_existingGad7Results)) {
          return new RoomOpenHelper.ValidationResult(false, "gad7_results(com.example.proyectotesismovil.data.local.entity.Gad7ResultEntity).\n"
                  + " Expected:\n" + _infoGad7Results + "\n"
                  + " Found:\n" + _existingGad7Results);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(5);
        _columnsUserProfile.put("uid", new TableInfo.Column("uid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("correo", new TableInfo.Column("correo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("rol", new TableInfo.Column("rol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("edad", new TableInfo.Column("edad", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.example.proyectotesismovil.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "632d4c6acab2eebf112a064af37926dc", "687ceb2e4f6dd3ee6ac397b2c95bd80c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "biometric_readings","activity_logs","reflections","emotional_states","gratitude_entries","calibration","daily_calibration","baseline","gad7_results","user_profile");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `biometric_readings`");
      _db.execSQL("DELETE FROM `activity_logs`");
      _db.execSQL("DELETE FROM `reflections`");
      _db.execSQL("DELETE FROM `emotional_states`");
      _db.execSQL("DELETE FROM `gratitude_entries`");
      _db.execSQL("DELETE FROM `calibration`");
      _db.execSQL("DELETE FROM `daily_calibration`");
      _db.execSQL("DELETE FROM `baseline`");
      _db.execSQL("DELETE FROM `gad7_results`");
      _db.execSQL("DELETE FROM `user_profile`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BiomarkerDao.class, BiomarkerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ActivityLogDao.class, ActivityLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReflectionDao.class, ReflectionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EmotionalStateDao.class, EmotionalStateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GratitudeDao.class, GratitudeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CalibrationDao.class, CalibrationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(Gad7Dao.class, Gad7Dao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BiomarkerDao biomarkerDao() {
    if (_biomarkerDao != null) {
      return _biomarkerDao;
    } else {
      synchronized(this) {
        if(_biomarkerDao == null) {
          _biomarkerDao = new BiomarkerDao_Impl(this);
        }
        return _biomarkerDao;
      }
    }
  }

  @Override
  public ActivityLogDao activityLogDao() {
    if (_activityLogDao != null) {
      return _activityLogDao;
    } else {
      synchronized(this) {
        if(_activityLogDao == null) {
          _activityLogDao = new ActivityLogDao_Impl(this);
        }
        return _activityLogDao;
      }
    }
  }

  @Override
  public ReflectionDao reflectionDao() {
    if (_reflectionDao != null) {
      return _reflectionDao;
    } else {
      synchronized(this) {
        if(_reflectionDao == null) {
          _reflectionDao = new ReflectionDao_Impl(this);
        }
        return _reflectionDao;
      }
    }
  }

  @Override
  public EmotionalStateDao emotionalStateDao() {
    if (_emotionalStateDao != null) {
      return _emotionalStateDao;
    } else {
      synchronized(this) {
        if(_emotionalStateDao == null) {
          _emotionalStateDao = new EmotionalStateDao_Impl(this);
        }
        return _emotionalStateDao;
      }
    }
  }

  @Override
  public GratitudeDao gratitudeDao() {
    if (_gratitudeDao != null) {
      return _gratitudeDao;
    } else {
      synchronized(this) {
        if(_gratitudeDao == null) {
          _gratitudeDao = new GratitudeDao_Impl(this);
        }
        return _gratitudeDao;
      }
    }
  }

  @Override
  public CalibrationDao calibrationDao() {
    if (_calibrationDao != null) {
      return _calibrationDao;
    } else {
      synchronized(this) {
        if(_calibrationDao == null) {
          _calibrationDao = new CalibrationDao_Impl(this);
        }
        return _calibrationDao;
      }
    }
  }

  @Override
  public Gad7Dao gad7Dao() {
    if (_gad7Dao != null) {
      return _gad7Dao;
    } else {
      synchronized(this) {
        if(_gad7Dao == null) {
          _gad7Dao = new Gad7Dao_Impl(this);
        }
        return _gad7Dao;
      }
    }
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }
}
