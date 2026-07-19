package com.example.proyectotesismovil.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BiomarkerDao_Impl implements BiomarkerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BiometricReadingEntity> __insertionAdapterOfBiometricReadingEntity;

  public BiomarkerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBiometricReadingEntity = new EntityInsertionAdapter<BiometricReadingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `biometric_readings` (`id`,`userId`,`heartRate`,`hrv`,`spO2`,`stressLevel`,`sleepHours`,`activityLevel`,`screenUnlocks`,`appUsageMinutes`,`timestamp`,`syncedToServer`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BiometricReadingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        if (entity.getHeartRate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getHeartRate());
        }
        if (entity.getHrv() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getHrv());
        }
        if (entity.getSpO2() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getSpO2());
        }
        if (entity.getStressLevel() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getStressLevel());
        }
        if (entity.getSleepHours() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getSleepHours());
        }
        if (entity.getActivityLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getActivityLevel());
        }
        if (entity.getScreenUnlocks() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getScreenUnlocks());
        }
        if (entity.getAppUsageMinutes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getAppUsageMinutes());
        }
        statement.bindLong(11, entity.getTimestamp());
        final int _tmp = entity.getSyncedToServer() ? 1 : 0;
        statement.bindLong(12, _tmp);
      }
    };
  }

  @Override
  public Object insertBiometricReading(final BiometricReadingEntity reading,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBiometricReadingEntity.insert(reading);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BiometricReadingEntity>> getAllReadings() {
    final String _sql = "SELECT * FROM biometric_readings ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"biometric_readings"}, new Callable<List<BiometricReadingEntity>>() {
      @Override
      @NonNull
      public List<BiometricReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "hrv");
          final int _cursorIndexOfSpO2 = CursorUtil.getColumnIndexOrThrow(_cursor, "spO2");
          final int _cursorIndexOfStressLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "stressLevel");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "screenUnlocks");
          final int _cursorIndexOfAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "appUsageMinutes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<BiometricReadingEntity> _result = new ArrayList<BiometricReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BiometricReadingEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Float _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getFloat(_cursorIndexOfHeartRate);
            }
            final Float _tmpHrv;
            if (_cursor.isNull(_cursorIndexOfHrv)) {
              _tmpHrv = null;
            } else {
              _tmpHrv = _cursor.getFloat(_cursorIndexOfHrv);
            }
            final Float _tmpSpO2;
            if (_cursor.isNull(_cursorIndexOfSpO2)) {
              _tmpSpO2 = null;
            } else {
              _tmpSpO2 = _cursor.getFloat(_cursorIndexOfSpO2);
            }
            final Float _tmpStressLevel;
            if (_cursor.isNull(_cursorIndexOfStressLevel)) {
              _tmpStressLevel = null;
            } else {
              _tmpStressLevel = _cursor.getFloat(_cursorIndexOfStressLevel);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Float _tmpActivityLevel;
            if (_cursor.isNull(_cursorIndexOfActivityLevel)) {
              _tmpActivityLevel = null;
            } else {
              _tmpActivityLevel = _cursor.getFloat(_cursorIndexOfActivityLevel);
            }
            final Integer _tmpScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfScreenUnlocks)) {
              _tmpScreenUnlocks = null;
            } else {
              _tmpScreenUnlocks = _cursor.getInt(_cursorIndexOfScreenUnlocks);
            }
            final Integer _tmpAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfAppUsageMinutes)) {
              _tmpAppUsageMinutes = null;
            } else {
              _tmpAppUsageMinutes = _cursor.getInt(_cursorIndexOfAppUsageMinutes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            _item = new BiometricReadingEntity(_tmpId,_tmpUserId,_tmpHeartRate,_tmpHrv,_tmpSpO2,_tmpStressLevel,_tmpSleepHours,_tmpActivityLevel,_tmpScreenUnlocks,_tmpAppUsageMinutes,_tmpTimestamp,_tmpSyncedToServer);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BiometricReadingEntity>> getReadingsSince(final long startTime) {
    final String _sql = "SELECT * FROM biometric_readings WHERE timestamp >= ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"biometric_readings"}, new Callable<List<BiometricReadingEntity>>() {
      @Override
      @NonNull
      public List<BiometricReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "hrv");
          final int _cursorIndexOfSpO2 = CursorUtil.getColumnIndexOrThrow(_cursor, "spO2");
          final int _cursorIndexOfStressLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "stressLevel");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "screenUnlocks");
          final int _cursorIndexOfAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "appUsageMinutes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<BiometricReadingEntity> _result = new ArrayList<BiometricReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BiometricReadingEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Float _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getFloat(_cursorIndexOfHeartRate);
            }
            final Float _tmpHrv;
            if (_cursor.isNull(_cursorIndexOfHrv)) {
              _tmpHrv = null;
            } else {
              _tmpHrv = _cursor.getFloat(_cursorIndexOfHrv);
            }
            final Float _tmpSpO2;
            if (_cursor.isNull(_cursorIndexOfSpO2)) {
              _tmpSpO2 = null;
            } else {
              _tmpSpO2 = _cursor.getFloat(_cursorIndexOfSpO2);
            }
            final Float _tmpStressLevel;
            if (_cursor.isNull(_cursorIndexOfStressLevel)) {
              _tmpStressLevel = null;
            } else {
              _tmpStressLevel = _cursor.getFloat(_cursorIndexOfStressLevel);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Float _tmpActivityLevel;
            if (_cursor.isNull(_cursorIndexOfActivityLevel)) {
              _tmpActivityLevel = null;
            } else {
              _tmpActivityLevel = _cursor.getFloat(_cursorIndexOfActivityLevel);
            }
            final Integer _tmpScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfScreenUnlocks)) {
              _tmpScreenUnlocks = null;
            } else {
              _tmpScreenUnlocks = _cursor.getInt(_cursorIndexOfScreenUnlocks);
            }
            final Integer _tmpAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfAppUsageMinutes)) {
              _tmpAppUsageMinutes = null;
            } else {
              _tmpAppUsageMinutes = _cursor.getInt(_cursorIndexOfAppUsageMinutes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            _item = new BiometricReadingEntity(_tmpId,_tmpUserId,_tmpHeartRate,_tmpHrv,_tmpSpO2,_tmpStressLevel,_tmpSleepHours,_tmpActivityLevel,_tmpScreenUnlocks,_tmpAppUsageMinutes,_tmpTimestamp,_tmpSyncedToServer);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedReadings(
      final Continuation<? super List<BiometricReadingEntity>> $completion) {
    final String _sql = "SELECT * FROM biometric_readings WHERE syncedToServer = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BiometricReadingEntity>>() {
      @Override
      @NonNull
      public List<BiometricReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "hrv");
          final int _cursorIndexOfSpO2 = CursorUtil.getColumnIndexOrThrow(_cursor, "spO2");
          final int _cursorIndexOfStressLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "stressLevel");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activityLevel");
          final int _cursorIndexOfScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "screenUnlocks");
          final int _cursorIndexOfAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "appUsageMinutes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<BiometricReadingEntity> _result = new ArrayList<BiometricReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BiometricReadingEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Float _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getFloat(_cursorIndexOfHeartRate);
            }
            final Float _tmpHrv;
            if (_cursor.isNull(_cursorIndexOfHrv)) {
              _tmpHrv = null;
            } else {
              _tmpHrv = _cursor.getFloat(_cursorIndexOfHrv);
            }
            final Float _tmpSpO2;
            if (_cursor.isNull(_cursorIndexOfSpO2)) {
              _tmpSpO2 = null;
            } else {
              _tmpSpO2 = _cursor.getFloat(_cursorIndexOfSpO2);
            }
            final Float _tmpStressLevel;
            if (_cursor.isNull(_cursorIndexOfStressLevel)) {
              _tmpStressLevel = null;
            } else {
              _tmpStressLevel = _cursor.getFloat(_cursorIndexOfStressLevel);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Float _tmpActivityLevel;
            if (_cursor.isNull(_cursorIndexOfActivityLevel)) {
              _tmpActivityLevel = null;
            } else {
              _tmpActivityLevel = _cursor.getFloat(_cursorIndexOfActivityLevel);
            }
            final Integer _tmpScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfScreenUnlocks)) {
              _tmpScreenUnlocks = null;
            } else {
              _tmpScreenUnlocks = _cursor.getInt(_cursorIndexOfScreenUnlocks);
            }
            final Integer _tmpAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfAppUsageMinutes)) {
              _tmpAppUsageMinutes = null;
            } else {
              _tmpAppUsageMinutes = _cursor.getInt(_cursorIndexOfAppUsageMinutes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            _item = new BiometricReadingEntity(_tmpId,_tmpUserId,_tmpHeartRate,_tmpHrv,_tmpSpO2,_tmpStressLevel,_tmpSleepHours,_tmpActivityLevel,_tmpScreenUnlocks,_tmpAppUsageMinutes,_tmpTimestamp,_tmpSyncedToServer);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsSynced(final List<Integer> ids,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE biometric_readings SET syncedToServer = 1 WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (int _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
