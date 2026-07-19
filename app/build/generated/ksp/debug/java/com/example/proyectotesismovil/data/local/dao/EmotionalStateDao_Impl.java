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
import com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class EmotionalStateDao_Impl implements EmotionalStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EmotionalStateEntity> __insertionAdapterOfEmotionalStateEntity;

  public EmotionalStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEmotionalStateEntity = new EntityInsertionAdapter<EmotionalStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `emotional_states` (`id`,`userId`,`emotionalState`,`registeredAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EmotionalStateEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        statement.bindString(3, entity.getEmotionalState());
        statement.bindLong(4, entity.getRegisteredAt());
      }
    };
  }

  @Override
  public Object insertEmotionalState(final EmotionalStateEntity state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEmotionalStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<EmotionalStateEntity> getLastEmotionalState() {
    final String _sql = "SELECT * FROM emotional_states ORDER BY registeredAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"emotional_states"}, new Callable<EmotionalStateEntity>() {
      @Override
      @Nullable
      public EmotionalStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEmotionalState = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionalState");
          final int _cursorIndexOfRegisteredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "registeredAt");
          final EmotionalStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpEmotionalState;
            _tmpEmotionalState = _cursor.getString(_cursorIndexOfEmotionalState);
            final long _tmpRegisteredAt;
            _tmpRegisteredAt = _cursor.getLong(_cursorIndexOfRegisteredAt);
            _result = new EmotionalStateEntity(_tmpId,_tmpUserId,_tmpEmotionalState,_tmpRegisteredAt);
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
  public Flow<List<EmotionalStateEntity>> getLastSevenDays(final String userId,
      final long sevenDaysAgo) {
    final String _sql = "\n"
            + "        SELECT * FROM emotional_states \n"
            + "        WHERE userId = ? \n"
            + "        AND registeredAt >= ? \n"
            + "        ORDER BY registeredAt ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, sevenDaysAgo);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"emotional_states"}, new Callable<List<EmotionalStateEntity>>() {
      @Override
      @NonNull
      public List<EmotionalStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEmotionalState = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionalState");
          final int _cursorIndexOfRegisteredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "registeredAt");
          final List<EmotionalStateEntity> _result = new ArrayList<EmotionalStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EmotionalStateEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpEmotionalState;
            _tmpEmotionalState = _cursor.getString(_cursorIndexOfEmotionalState);
            final long _tmpRegisteredAt;
            _tmpRegisteredAt = _cursor.getLong(_cursorIndexOfRegisteredAt);
            _item = new EmotionalStateEntity(_tmpId,_tmpUserId,_tmpEmotionalState,_tmpRegisteredAt);
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
  public Object getMostFrequentEmotion(final String userId, final long sevenDaysAgo,
      final Continuation<? super String> $completion) {
    final String _sql = "\n"
            + "        SELECT emotionalState\n"
            + "        FROM emotional_states \n"
            + "        WHERE userId = ? \n"
            + "        AND registeredAt >= ?\n"
            + "        GROUP BY emotionalState \n"
            + "        ORDER BY COUNT(*) DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, sevenDaysAgo);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
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

  @Override
  public Object getTotalRegistrations(final String userId, final long sevenDaysAgo,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM emotional_states \n"
            + "        WHERE userId = ? \n"
            + "        AND registeredAt >= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, sevenDaysAgo);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
