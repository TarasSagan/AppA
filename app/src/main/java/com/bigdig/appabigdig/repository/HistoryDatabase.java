package com.bigdig.appabigdig.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {HistoryDBModel.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static final String DB_NAME = "historyDatabase.db";
    private static volatile HistoryDatabase instance;

    public static synchronized HistoryDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static HistoryDatabase create(final Context context){
        return Room.databaseBuilder(
                context,
                HistoryDatabase.class,
                DB_NAME
        ).build();
    }
    public abstract HistoryDAO getHistoryDAO();
}
