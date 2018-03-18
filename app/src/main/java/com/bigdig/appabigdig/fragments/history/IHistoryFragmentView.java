package com.bigdig.appabigdig.fragments.history;

import com.bigdig.appabigdig.HistoryModel;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import java.util.List;

public interface IHistoryFragmentView extends MvpView{
    void showHistory(List<HistoryModel> histories);
    void showMessage(String message);
}
