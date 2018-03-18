package com.bigdig.appabigdig.fragments.test;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bigdig.appabigdig.R;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public class TestFragmentPresenter extends MvpBasePresenter<ITestFragmentView>{
    private Context context;
    public TestFragmentPresenter(Context context) {
        this.context = context;
    }

    public void onShowImage(String url){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(context.getResources().getString(R.string.intent_filter_url_key), url);

        intent.putExtra(context.getResources().getString(R.string.intent_filter_key_bundle), bundle);
        try {
            intent.setComponent(new ComponentName(
                    context.getString(R.string.intent_filter_key_pkg),
                    context.getString(R.string.intent_filter_key_cls)));
            context.startActivity(intent);
        }catch (Throwable throwable){
            ifViewAttached(view -> view.showMessage(context.getString(R.string.need_app_b)));
        }
    }
}
