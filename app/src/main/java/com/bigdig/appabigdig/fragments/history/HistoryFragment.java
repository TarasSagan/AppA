package com.bigdig.appabigdig.fragments.history;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bigdig.appabigdig.HistoryModel;
import com.bigdig.appabigdig.R;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryFragment extends MvpFragment<IHistoryFragmentView, HistoryFragmentPresenter>
        implements IHistoryFragmentView {
    private View view;
    private HistoryRecyclerAdapter historyRecyclerAdapter;
    @BindView(R.id.history_list) RecyclerView recyclerView;

    public HistoryFragment() {
    }

    @Override
    public HistoryFragmentPresenter createPresenter() {
        return new HistoryFragmentPresenter(getContext(), this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_list, container, false);
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        historyRecyclerAdapter = new HistoryRecyclerAdapter(new ArrayList<>(), getPresenter(), getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(historyRecyclerAdapter);
        initToolbar(view);
        getPresenter().getHistory();
        return view;
    }
    private void initToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){actionBar.setTitle("");}
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.history_sort, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sort) {

        } else if (id == R.id.menu_sort_latest) {
            getPresenter().onSortByLatest();
        } else if (id == R.id.menu_sort_oldest) {
            getPresenter().onSortByOldest();
        } else if (id == R.id.menu_sort_success_top) {
            getPresenter().onSortBySuccessTop();
        }else if (id == R.id.menu_sort_success_bottom) {
            getPresenter().onSortBySuccessBottom();
        }
        return true;
    }

    @Override
    public void showHistory(List<HistoryModel> histories) {
        historyRecyclerAdapter.updateData(histories);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();

    }
}
