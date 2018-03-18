package com.bigdig.appabigdig.fragments.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigdig.appabigdig.HistoryModel;
import com.bigdig.appabigdig.R;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private  Context context;
    private List<HistoryModel> mValues;
    private IHistoryRecyclerListener mListener;


    public HistoryRecyclerAdapter(List<HistoryModel> items, IHistoryRecyclerListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    public void addData(List<HistoryModel> items){
        int positionStart = getItemCount();
        mValues.addAll(items);
        notifyItemRangeInserted(positionStart, getItemCount());
    }

    public void updateData(List<HistoryModel> items){
        mValues.clear();
        mValues.addAll(items);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        bindDate(holder, position);
        bindUrl(holder, position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mValues.get(position));

            }
        });
    }
    private void bindDate(final ViewHolder holder, int position){
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(mValues.get(position).getOpenTime());
        holder.textOpenTime.setText(date);
    }

    private void bindUrl(final ViewHolder holder, int position){
        holder.mContentView.setText(mValues.get(position).getUrl());
        int status = mValues.get(position).getStatus();
        Log.d("TAG", Integer.toString(status));
        if (status == 1){
            holder.mContentView.setBackground(
                    context.getDrawable(R.drawable.item_adress_history_status1));
        }else if (status == 2){
            holder.mContentView.setBackground(
                    context.getDrawable(R.drawable.item_adress_history_status2));
        }else if (status == 3){
            holder.mContentView.setBackground(
                    context.getDrawable(R.drawable.item_adress_history_status3));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public HistoryModel mItem;
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.textOpenTime) TextView textOpenTime;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }
    }
}
