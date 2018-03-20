package com.bigdig.appabigdig.fragments.history;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObservable;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.bigdig.appabigdig.HistoryModel;
import com.bigdig.appabigdig.Keys;
import com.bigdig.appabigdig.MainActivity;
import com.bigdig.appabigdig.R;
import com.bigdig.appabigdig.repository.HistoryDBModel;
import com.bigdig.appabigdig.repository.HistoryContentProvider;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class HistoryFragmentPresenter extends MvpBasePresenter<IHistoryFragmentView> implements IHistoryRecyclerListener {
    private Context context;
    private List<HistoryModel> histories;
    private HistoryFragment fragment;


    public HistoryFragmentPresenter(Context context, HistoryFragment fragment) {
        this.fragment = fragment;
        this.context = context;
        histories = new ArrayList<>();
    }

    public void getHistory(){
        if (!histories.isEmpty()){
            ifViewAttached(view -> view.showHistory(histories));
        }else {init();}
    }

    private void init(){
      fragment.getLoaderManager().initLoader(MainActivity.LOADER_CHEESES,
                null, mLoaderCallbacks);

    }
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case MainActivity.LOADER_CHEESES:
                            return new CursorLoader(context.getApplicationContext(),
                                    HistoryContentProvider.URI_HISTORY,
                                    new String[]{HistoryDBModel.COLUMN_ID},
                                    null, null, null);
                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    switch (loader.getId()) {
                        case MainActivity.LOADER_CHEESES:
                            if(data != null){
                                if (data.moveToFirst()) {
                                    do {
                                        histories.clear();
                                        histories.add(new HistoryModel(
                                                data.getLong(data.getColumnIndex(HistoryDBModel.COLUMN_ID))
                                                ,data.getString(data.getColumnIndex(HistoryDBModel.COLUMN_URL))
                                                ,data.getInt(data.getColumnIndex(HistoryDBModel.COLUMN_STATUS))
                                                ,data.getLong(data.getColumnIndex(HistoryDBModel.COLUMN_OPEN_TIME))
                                        ));
                                    }while (data.moveToNext());
                                }
                                onSortByLatest(); //This method show or update data
                            }else {
                                ifViewAttached(view -> view.showMessage(context.getString(R.string.history_is_empty)))
                                ;}
                                break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case MainActivity.LOADER_CHEESES:
                            break;
                    }
                }
            };

    @Override
    public void onClick(HistoryModel item) {
        Intent intent = new Intent();
        intent.putExtra(Keys.URL_KEY, item.getUrl());
        intent.putExtra(Keys.STATUS_KEY, item.getStatus());
        intent.putExtra(Keys.ID_KEY, item.getId());

        try {
            intent.setComponent(new ComponentName(
                    context.getString(R.string.intent_filter_key_pkg),
                    context.getString(R.string.intent_filter_key_cls)));
            context.startActivity(intent);
        }catch (Throwable throwable){
            ifViewAttached(view -> view.showMessage(context.getString(R.string.need_app_b)));
        }
    }

    private List<HistoryModel> sortByLatest(List<HistoryModel> list){
        Collections.sort(list, new Comparator<HistoryModel>() {
            @Override
            public int compare(HistoryModel o1, HistoryModel o2) {
                return Long.compare(o2.getOpenTime(), o1.getOpenTime());
            }
        });
        return list;
    }

    public void onSortByLatest(){
        histories = sortByLatest(histories);
        ifViewAttached(view -> view.showHistory(histories));
    }

    private List<HistoryModel> sortByOldest(List<HistoryModel> list) {
        Collections.sort(list, new Comparator<HistoryModel>() {
            @Override
            public int compare(HistoryModel o1, HistoryModel o2) {
                return Long.compare(o1.getOpenTime(), o2.getOpenTime());
            }
        });
        return list;
    }

    public void onSortByOldest(){
        histories = sortByOldest(histories);
        ifViewAttached(view -> view.showHistory(histories));
    }

    private List<HistoryModel> sortBySuccessTop(List<HistoryModel> list) {
        List<HistoryModel> historyList = new ArrayList<>();
        if (!list.isEmpty()){
            List<HistoryModel> listStatus1 = new ArrayList<>();
            List<HistoryModel> listStatus2 = new ArrayList<>();
            List<HistoryModel> listStatus3 = new ArrayList<>();
            for (HistoryModel history : list) {
                if (history.getStatus() == 1) {listStatus1.add(history);
                } else if (history.getStatus() == 2) {listStatus2.add(history);
                } else if (history.getStatus() == 3) {listStatus3.add(history);
                }
            }
            historyList.addAll(sortByLatest(listStatus1));
            historyList.addAll(sortByLatest(listStatus2));
            historyList.addAll(sortByLatest(listStatus3));
        }
        return historyList;
    }

    public void onSortBySuccessTop(){
        histories = sortBySuccessTop(histories);
        ifViewAttached(view -> view.showHistory(histories));
    }
    private List<HistoryModel> sortBySuccessBottom(List<HistoryModel> list) {
        List<HistoryModel> historyList = new ArrayList<>();
        if (!list.isEmpty()){
            List<HistoryModel> listStatus1 = new ArrayList<>();
            List<HistoryModel> listStatus2 = new ArrayList<>();
            List<HistoryModel> listStatus3 = new ArrayList<>();
            for (HistoryModel history : list) {
                if (history.getStatus() == 1) {listStatus1.add(history);
                } else if (history.getStatus() == 2) {listStatus2.add(history);
                } else if (history.getStatus() == 3) {listStatus3.add(history);
                }
            }
            historyList.addAll(sortByLatest(listStatus3));
            historyList.addAll(sortByLatest(listStatus2));
            historyList.addAll(sortByLatest(listStatus1));
        }
        return historyList;
    }

    public void onSortBySuccessBottom(){
        histories = sortBySuccessBottom(histories);
        ifViewAttached(view -> view.showHistory(histories));
    }

}
