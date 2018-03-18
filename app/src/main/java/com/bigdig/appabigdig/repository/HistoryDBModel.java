package com.bigdig.appabigdig.repository;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;

@Entity(tableName = HistoryDBModel.TABLE_NAME)
public class HistoryDBModel {
    public static final String TABLE_NAME = "history";
//    public static final String TABLE_NAME = "historyDatabase.db";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_OPEN_TIME = "openTime";


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = COLUMN_URL)
    public String url;

    @ColumnInfo(name = COLUMN_STATUS)
    public int status;

    @ColumnInfo(name = COLUMN_OPEN_TIME)
    public long openTime;

    public static HistoryDBModel fromContentValues(ContentValues values) {
        final HistoryDBModel historyDBModel = new HistoryDBModel();
        if (values.containsKey(COLUMN_ID)) {
            historyDBModel.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_URL)) {
            historyDBModel.url = values.getAsString(COLUMN_URL);
        }
        if (values.containsKey(COLUMN_STATUS)) {
            historyDBModel.status = values.getAsInteger(COLUMN_STATUS);
        }
        if (values.containsKey(COLUMN_OPEN_TIME)) {
            historyDBModel.openTime = values.getAsLong(COLUMN_OPEN_TIME);
        }
        return historyDBModel;
    }
}