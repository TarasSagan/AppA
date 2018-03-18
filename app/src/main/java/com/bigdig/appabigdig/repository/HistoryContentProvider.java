package com.bigdig.appabigdig.repository;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;


public class HistoryContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.bigdig.appabigdig.repository";

    public static final Uri URI_HISTORY = Uri.parse(
            "content://" + AUTHORITY + "/" + HistoryDBModel.TABLE_NAME);

    private static final int CODE_HISTORY_DIR = 1;
    private static final int CODE_HISTORY_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, HistoryDBModel.TABLE_NAME, CODE_HISTORY_DIR);
        MATCHER.addURI(AUTHORITY, HistoryDBModel.TABLE_NAME + "/*", CODE_HISTORY_ITEM);
    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if(code == CODE_HISTORY_DIR || code == CODE_HISTORY_ITEM){
            final Context context = getContext();
            if (context == null){
                return null;
            }
            HistoryDAO historyDAO = HistoryDatabase.getInstance(context).getHistoryDAO();
            final Cursor cursor;
            if (code == CODE_HISTORY_DIR){
                cursor = historyDAO.selectAll();
            }else {
                cursor = historyDAO.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)){
            case CODE_HISTORY_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + HistoryDBModel.TABLE_NAME;
            case CODE_HISTORY_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + HistoryDBModel.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)){
            case CODE_HISTORY_DIR:
                final Context context = getContext();
                if (context == null){
                    return null;
                }
                final long id = HistoryDatabase.getInstance(context).getHistoryDAO()
                        .insert(HistoryDBModel.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_HISTORY_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)){
            case CODE_HISTORY_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_HISTORY_ITEM:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final int count = HistoryDatabase.getInstance(context).getHistoryDAO()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)){
            case CODE_HISTORY_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_HISTORY_ITEM:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final HistoryDBModel historyDBModel = HistoryDBModel.fromContentValues(values);
                historyDBModel.id = ContentUris.parseId(uri);
                final int count = HistoryDatabase.getInstance(context)
                        .getHistoryDAO().update(historyDBModel);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null){
            return new ContentProviderResult[0];
        }
        final HistoryDatabase database = HistoryDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (MATCHER.match(uri   )){
            case CODE_HISTORY_DIR:
                final Context context = getContext();
                if (context == null){
                    return 0;
                }
                final HistoryDatabase database = HistoryDatabase.getInstance(context);
                final HistoryDBModel[] historie = new HistoryDBModel[values.length];
                for (int i = 0; i < values.length; i++){
                    historie[i] = HistoryDBModel.fromContentValues(values[i]);
                }
                return database.getHistoryDAO().insertAll(historie).length;
            case CODE_HISTORY_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
