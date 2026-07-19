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
import com.example.proyectotesismovil.data.local.entity.Gad7ResultEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class Gad7Dao_Impl implements Gad7Dao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Gad7ResultEntity> __insertionAdapterOfGad7ResultEntity;

  public Gad7Dao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGad7ResultEntity = new EntityInsertionAdapter<Gad7ResultEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `gad7_results` (`id`,`userId`,`q1`,`q2`,`q3`,`q4`,`q5`,`q6`,`q7`,`totalScore`,`severityLevel`,`appliedAt`,`syncedToServer`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Gad7ResultEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        statement.bindLong(3, entity.getQ1());
        statement.bindLong(4, entity.getQ2());
        statement.bindLong(5, entity.getQ3());
        statement.bindLong(6, entity.getQ4());
        statement.bindLong(7, entity.getQ5());
        statement.bindLong(8, entity.getQ6());
        statement.bindLong(9, entity.getQ7());
        statement.bindLong(10, entity.getTotalScore());
        statement.bindString(11, entity.getSeverityLevel());
        statement.bindLong(12, entity.getAppliedAt());
        final int _tmp = entity.getSyncedToServer() ? 1 : 0;
        statement.bindLong(13, _tmp);
      }
    };
  }

  @Override
  public Object insertResult(final Gad7ResultEntity result,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGad7ResultEntity.insert(result);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Gad7ResultEntity>> getAllResults(final String userId) {
    final String _sql = "\n"
            + "        SELECT * FROM gad7_results \n"
            + "        WHERE userId = ? \n"
            + "        ORDER BY appliedAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gad7_results"}, new Callable<List<Gad7ResultEntity>>() {
      @Override
      @NonNull
      public List<Gad7ResultEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfQ1 = CursorUtil.getColumnIndexOrThrow(_cursor, "q1");
          final int _cursorIndexOfQ2 = CursorUtil.getColumnIndexOrThrow(_cursor, "q2");
          final int _cursorIndexOfQ3 = CursorUtil.getColumnIndexOrThrow(_cursor, "q3");
          final int _cursorIndexOfQ4 = CursorUtil.getColumnIndexOrThrow(_cursor, "q4");
          final int _cursorIndexOfQ5 = CursorUtil.getColumnIndexOrThrow(_cursor, "q5");
          final int _cursorIndexOfQ6 = CursorUtil.getColumnIndexOrThrow(_cursor, "q6");
          final int _cursorIndexOfQ7 = CursorUtil.getColumnIndexOrThrow(_cursor, "q7");
          final int _cursorIndexOfTotalScore = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScore");
          final int _cursorIndexOfSeverityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "severityLevel");
          final int _cursorIndexOfAppliedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "appliedAt");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<Gad7ResultEntity> _result = new ArrayList<Gad7ResultEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Gad7ResultEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final int _tmpQ1;
            _tmpQ1 = _cursor.getInt(_cursorIndexOfQ1);
            final int _tmpQ2;
            _tmpQ2 = _cursor.getInt(_cursorIndexOfQ2);
            final int _tmpQ3;
            _tmpQ3 = _cursor.getInt(_cursorIndexOfQ3);
            final int _tmpQ4;
            _tmpQ4 = _cursor.getInt(_cursorIndexOfQ4);
            final int _tmpQ5;
            _tmpQ5 = _cursor.getInt(_cursorIndexOfQ5);
            final int _tmpQ6;
            _tmpQ6 = _cursor.getInt(_cursorIndexOfQ6);
            final int _tmpQ7;
            _tmpQ7 = _cursor.getInt(_cursorIndexOfQ7);
            final int _tmpTotalScore;
            _tmpTotalScore = _cursor.getInt(_cursorIndexOfTotalScore);
            final String _tmpSeverityLevel;
            _tmpSeverityLevel = _cursor.getString(_cursorIndexOfSeverityLevel);
            final long _tmpAppliedAt;
            _tmpAppliedAt = _cursor.getLong(_cursorIndexOfAppliedAt);
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            _item = new Gad7ResultEntity(_tmpId,_tmpUserId,_tmpQ1,_tmpQ2,_tmpQ3,_tmpQ4,_tmpQ5,_tmpQ6,_tmpQ7,_tmpTotalScore,_tmpSeverityLevel,_tmpAppliedAt,_tmpSyncedToServer);
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
  public Object getLatestResult(final String userId,
      final Continuation<? super Gad7ResultEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM gad7_results \n"
            + "        WHERE userId = ? \n"
            + "        ORDER BY appliedAt DESC \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Gad7ResultEntity>() {
      @Override
      @Nullable
      public Gad7ResultEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfQ1 = CursorUtil.getColumnIndexOrThrow(_cursor, "q1");
          final int _cursorIndexOfQ2 = CursorUtil.getColumnIndexOrThrow(_cursor, "q2");
          final int _cursorIndexOfQ3 = CursorUtil.getColumnIndexOrThrow(_cursor, "q3");
          final int _cursorIndexOfQ4 = CursorUtil.getColumnIndexOrThrow(_cursor, "q4");
          final int _cursorIndexOfQ5 = CursorUtil.getColumnIndexOrThrow(_cursor, "q5");
          final int _cursorIndexOfQ6 = CursorUtil.getColumnIndexOrThrow(_cursor, "q6");
          final int _cursorIndexOfQ7 = CursorUtil.getColumnIndexOrThrow(_cursor, "q7");
          final int _cursorIndexOfTotalScore = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScore");
          final int _cursorIndexOfSeverityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "severityLevel");
          final int _cursorIndexOfAppliedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "appliedAt");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final Gad7ResultEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final int _tmpQ1;
            _tmpQ1 = _cursor.getInt(_cursorIndexOfQ1);
            final int _tmpQ2;
            _tmpQ2 = _cursor.getInt(_cursorIndexOfQ2);
            final int _tmpQ3;
            _tmpQ3 = _cursor.getInt(_cursorIndexOfQ3);
            final int _tmpQ4;
            _tmpQ4 = _cursor.getInt(_cursorIndexOfQ4);
            final int _tmpQ5;
            _tmpQ5 = _cursor.getInt(_cursorIndexOfQ5);
            final int _tmpQ6;
            _tmpQ6 = _cursor.getInt(_cursorIndexOfQ6);
            final int _tmpQ7;
            _tmpQ7 = _cursor.getInt(_cursorIndexOfQ7);
            final int _tmpTotalScore;
            _tmpTotalScore = _cursor.getInt(_cursorIndexOfTotalScore);
            final String _tmpSeverityLevel;
            _tmpSeverityLevel = _cursor.getString(_cursorIndexOfSeverityLevel);
            final long _tmpAppliedAt;
            _tmpAppliedAt = _cursor.getLong(_cursorIndexOfAppliedAt);
            final boolean _tmpSyncedToServer;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _tmpSyncedToServer = _tmp != 0;
            _result = new Gad7ResultEntity(_tmpId,_tmpUserId,_tmpQ1,_tmpQ2,_tmpQ3,_tmpQ4,_tmpQ5,_tmpQ6,_tmpQ7,_tmpTotalScore,_tmpSeverityLevel,_tmpAppliedAt,_tmpSyncedToServer);
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
