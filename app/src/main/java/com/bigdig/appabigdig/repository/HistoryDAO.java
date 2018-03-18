package com.bigdig.appabigdig.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import io.reactivex.Flowable;

@Dao
public interface HistoryDAO {
    @Query("SELECT * FROM " + HistoryDBModel.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + HistoryDBModel.TABLE_NAME + " WHERE " + HistoryDBModel.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Insert
    long insert(HistoryDBModel historyDBModel);

    @Insert
    long[] insertAll(HistoryDBModel[] histories);

    @Query("DELETE FROM " + HistoryDBModel.TABLE_NAME + " WHERE " + HistoryDBModel.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Update
    int update(HistoryDBModel historyDBModel);
}
