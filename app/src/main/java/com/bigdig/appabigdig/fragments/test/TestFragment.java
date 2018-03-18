package com.bigdig.appabigdig.fragments.test;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bigdig.appabigdig.R;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestFragment extends MvpFragment<ITestFragmentView, TestFragmentPresenter>
        implements ITestFragmentView {
    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.button) Button button;
    private View view;


    public TestFragment() {
    }

    @Override
    public TestFragmentPresenter createPresenter() {
        return new TestFragmentPresenter(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
    @OnClick(R.id.button)
    public void onClick(){
        if(!TextUtils.isEmpty(editText.getText().toString())) {
            getPresenter().onShowImage(editText.getText().toString());
        }
    }
    @OnClick(R.id.editText)
    public void onClickEditText(){
        onClick();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
