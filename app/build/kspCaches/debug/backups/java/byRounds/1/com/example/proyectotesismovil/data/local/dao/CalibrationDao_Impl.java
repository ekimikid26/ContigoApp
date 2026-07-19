package com.example.proyectotesismovil.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.proyectotesismovil.data.local.entity.BaselineEntity;
import com.example.proyectotesismovil.data.local.entity.CalibrationEntity;
import com.example.proyectotesismovil.data.local.entity.DailyCalibrationEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class CalibrationDao_Impl implements CalibrationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CalibrationEntity> __insertionAdapterOfCalibrationEntity;

  private final EntityInsertionAdapter<DailyCalibrationEntity> __insertionAdapterOfDailyCalibrationEntity;

  private final EntityInsertionAdapter<BaselineEntity> __insertionAdapterOfBaselineEntity;

  public CalibrationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCalibrationEntity = new EntityInsertionAdapter<CalibrationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `calibration` (`userId`,`startDate`,`isCompleted`,`completedAt`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalibrationEntity entity) {
        statement.bindString(1, entity.getUserId());
        statement.bindLong(2, entity.getStartDate());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(3, _tmp);
        if (entity.getCompletedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCompletedAt());
        }
      }
    };
    this.__insertionAdapterOfDailyCalibrationEntity = new EntityInsertionAdapter<DailyCalibrationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `daily_calibration` (`id`,`userId`,`dayNumber`,`date`,`avgHeartRate`,`avgHrv`,`sleepHours`,`avgActivityLevel`,`avgScreenUnlocks`,`avgAppUsageMinutes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyCalibrationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        statement.bindLong(3, entity.getDayNumber());
        statement.bindLong(4, entity.getDate());
        if (entity.getAvgHeartRate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getAvgHeartRate());
        }
        if (entity.getAvgHrv() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getAvgHrv());
        }
        if (entity.getSleepHours() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getSleepHours());
        }
        if (entity.getAvgActivityLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getAvgActivityLevel());
        }
        if (entity.getAvgScreenUnlocks() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getAvgScreenUnlocks());
        }
        if (entity.getAvgAppUsageMinutes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getAvgAppUsageMinutes());
        }
      }
    };
    this.__insertionAdapterOfBaselineEntity = new EntityInsertionAdapter<BaselineEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `baseline` (`userId`,`avgHeartRate`,`stdHeartRate`,`avgHrv`,`stdHrv`,`avgSleepHours`,`stdSleepHours`,`avgActivityLevel`,`stdActivityLevel`,`avgScreenUnlocks`,`stdScreenUnlocks`,`avgAppUsageMinutes`,`stdAppUsageMinutes`,`calibrationCompletedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BaselineEntity entity) {
        statement.bindString(1, entity.getUserId());
        if (entity.getAvgHeartRate() == null) {
          statement.bindNull(2);
        } else {
          statement.bindDouble(2, entity.getAvgHeartRate());
        }
        if (entity.getStdHeartRate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getStdHeartRate());
        }
        if (entity.getAvgHrv() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getAvgHrv());
        }
        if (entity.getStdHrv() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getStdHrv());
        }
        if (entity.getAvgSleepHours() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getAvgSleepHours());
        }
        if (entity.getStdSleepHours() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getStdSleepHours());
        }
        if (entity.getAvgActivityLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getAvgActivityLevel());
        }
        if (entity.getStdActivityLevel() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getStdActivityLevel());
        }
        if (entity.getAvgScreenUnlocks() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getAvgScreenUnlocks());
        }
        if (entity.getStdScreenUnlocks() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getStdScreenUnlocks());
        }
        if (entity.getAvgAppUsageMinutes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getAvgAppUsageMinutes());
        }
        if (entity.getStdAppUsageMinutes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.getStdAppUsageMinutes());
        }
        statement.bindLong(14, entity.getCalibrationCompletedAt());
      }
    };
  }

  @Override
  public Object insertCalibration(final CalibrationEntity calibration,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCalibrationEntity.insert(calibration);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDailySummary(final DailyCalibrationEntity summary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyCalibrationEntity.insert(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBaseline(final BaselineEntity baseline,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBaselineEntity.insert(baseline);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<CalibrationEntity> getCalibration(final String userId) {
    final String _sql = "SELECT * FROM calibration WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calibration"}, new Callable<CalibrationEntity>() {
      @Override
      @Nullable
      public CalibrationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final CalibrationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new CalibrationEntity(_tmpUserId,_tmpStartDate,_tmpIsCompleted,_tmpCompletedAt);
          } else {
            _result = null;
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
  public Object getDailySummaries(final String userId,
      final Continuation<? super List<DailyCalibrationEntity>> $completion) {
    final String _sql = "SELECT * FROM daily_calibration WHERE userId = ? ORDER BY dayNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DailyCalibrationEntity>>() {
      @Override
      @NonNull
      public List<DailyCalibrationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
          final int _cursorIndexOfAvgHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHrv");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfAvgActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "avgActivityLevel");
          final int _cursorIndexOfAvgScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "avgScreenUnlocks");
          final int _cursorIndexOfAvgAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "avgAppUsageMinutes");
          final List<DailyCalibrationEntity> _result = new ArrayList<DailyCalibrationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyCalibrationEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final int _tmpDayNumber;
            _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final Float _tmpAvgHeartRate;
            if (_cursor.isNull(_cursorIndexOfAvgHeartRate)) {
              _tmpAvgHeartRate = null;
            } else {
              _tmpAvgHeartRate = _cursor.getFloat(_cursorIndexOfAvgHeartRate);
            }
            final Float _tmpAvgHrv;
            if (_cursor.isNull(_cursorIndexOfAvgHrv)) {
              _tmpAvgHrv = null;
            } else {
              _tmpAvgHrv = _cursor.getFloat(_cursorIndexOfAvgHrv);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Float _tmpAvgActivityLevel;
            if (_cursor.isNull(_cursorIndexOfAvgActivityLevel)) {
              _tmpAvgActivityLevel = null;
            } else {
              _tmpAvgActivityLevel = _cursor.getFloat(_cursorIndexOfAvgActivityLevel);
            }
            final Float _tmpAvgScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfAvgScreenUnlocks)) {
              _tmpAvgScreenUnlocks = null;
            } else {
              _tmpAvgScreenUnlocks = _cursor.getFloat(_cursorIndexOfAvgScreenUnlocks);
            }
            final Float _tmpAvgAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfAvgAppUsageMinutes)) {
              _tmpAvgAppUsageMinutes = null;
            } else {
              _tmpAvgAppUsageMinutes = _cursor.getFloat(_cursorIndexOfAvgAppUsageMinutes);
            }
            _item = new DailyCalibrationEntity(_tmpId,_tmpUserId,_tmpDayNumber,_tmpDate,_tmpAvgHeartRate,_tmpAvgHrv,_tmpSleepHours,_tmpAvgActivityLevel,_tmpAvgScreenUnlocks,_tmpAvgAppUsageMinutes);
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
  public Object getBaseline(final String userId,
      final Continuation<? super BaselineEntity> $completion) {
    final String _sql = "SELECT * FROM baseline WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BaselineEntity>() {
      @Override
      @Nullable
      public BaselineEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
          final int _cursorIndexOfStdHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "stdHeartRate");
          final int _cursorIndexOfAvgHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHrv");
          final int _cursorIndexOfStdHrv = CursorUtil.getColumnIndexOrThrow(_cursor, "stdHrv");
          final int _cursorIndexOfAvgSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "avgSleepHours");
          final int _cursorIndexOfStdSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "stdSleepHours");
          final int _cursorIndexOfAvgActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "avgActivityLevel");
          final int _cursorIndexOfStdActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "stdActivityLevel");
          final int _cursorIndexOfAvgScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "avgScreenUnlocks");
          final int _cursorIndexOfStdScreenUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "stdScreenUnlocks");
          final int _cursorIndexOfAvgAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "avgAppUsageMinutes");
          final int _cursorIndexOfStdAppUsageMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "stdAppUsageMinutes");
          final int _cursorIndexOfCalibrationCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "calibrationCompletedAt");
          final BaselineEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Float _tmpAvgHeartRate;
            if (_cursor.isNull(_cursorIndexOfAvgHeartRate)) {
              _tmpAvgHeartRate = null;
            } else {
              _tmpAvgHeartRate = _cursor.getFloat(_cursorIndexOfAvgHeartRate);
            }
            final Float _tmpStdHeartRate;
            if (_cursor.isNull(_cursorIndexOfStdHeartRate)) {
              _tmpStdHeartRate = null;
            } else {
              _tmpStdHeartRate = _cursor.getFloat(_cursorIndexOfStdHeartRate);
            }
            final Float _tmpAvgHrv;
            if (_cursor.isNull(_cursorIndexOfAvgHrv)) {
              _tmpAvgHrv = null;
            } else {
              _tmpAvgHrv = _cursor.getFloat(_cursorIndexOfAvgHrv);
            }
            final Float _tmpStdHrv;
            if (_cursor.isNull(_cursorIndexOfStdHrv)) {
              _tmpStdHrv = null;
            } else {
              _tmpStdHrv = _cursor.getFloat(_cursorIndexOfStdHrv);
            }
            final Float _tmpAvgSleepHours;
            if (_cursor.isNull(_cursorIndexOfAvgSleepHours)) {
              _tmpAvgSleepHours = null;
            } else {
              _tmpAvgSleepHours = _cursor.getFloat(_cursorIndexOfAvgSleepHours);
            }
            final Float _tmpStdSleepHours;
            if (_cursor.isNull(_cursorIndexOfStdSleepHours)) {
              _tmpStdSleepHours = null;
            } else {
              _tmpStdSleepHours = _cursor.getFloat(_cursorIndexOfStdSleepHours);
            }
            final Float _tmpAvgActivityLevel;
            if (_cursor.isNull(_cursorIndexOfAvgActivityLevel)) {
              _tmpAvgActivityLevel = null;
            } else {
              _tmpAvgActivityLevel = _cursor.getFloat(_cursorIndexOfAvgActivityLevel);
            }
            final Float _tmpStdActivityLevel;
            if (_cursor.isNull(_cursorIndexOfStdActivityLevel)) {
              _tmpStdActivityLevel = null;
            } else {
              _tmpStdActivityLevel = _cursor.getFloat(_cursorIndexOfStdActivityLevel);
            }
            final Float _tmpAvgScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfAvgScreenUnlocks)) {
              _tmpAvgScreenUnlocks = null;
            } else {
              _tmpAvgScreenUnlocks = _cursor.getFloat(_cursorIndexOfAvgScreenUnlocks);
            }
            final Float _tmpStdScreenUnlocks;
            if (_cursor.isNull(_cursorIndexOfStdScreenUnlocks)) {
              _tmpStdScreenUnlocks = null;
            } else {
              _tmpStdScreenUnlocks = _cursor.getFloat(_cursorIndexOfStdScreenUnlocks);
            }
            final Float _tmpAvgAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfAvgAppUsageMinutes)) {
              _tmpAvgAppUsageMinutes = null;
            } else {
              _tmpAvgAppUsageMinutes = _cursor.getFloat(_cursorIndexOfAvgAppUsageMinutes);
            }
            final Float _tmpStdAppUsageMinutes;
            if (_cursor.isNull(_cursorIndexOfStdAppUsageMinutes)) {
              _tmpStdAppUsageMinutes = null;
            } else {
              _tmpStdAppUsageMinutes = _cursor.getFloat(_cursorIndexOfStdAppUsageMinutes);
            }
            final long _tmpCalibrationCompletedAt;
            _tmpCalibrationCompletedAt = _cursor.getLong(_cursorIndexOfCalibrationCompletedAt);
            _result = new BaselineEntity(_tmpUserId,_tmpAvgHeartRate,_tmpStdHeartRate,_tmpAvgHrv,_tmpStdHrv,_tmpAvgSleepHours,_tmpStdSleepHours,_tmpAvgActivityLevel,_tmpStdActivityLevel,_tmpAvgScreenUnlocks,_tmpStdScreenUnlocks,_tmpAvgAppUsageMinutes,_tmpStdAppUsageMinutes,_tmpCalibrationCompletedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
