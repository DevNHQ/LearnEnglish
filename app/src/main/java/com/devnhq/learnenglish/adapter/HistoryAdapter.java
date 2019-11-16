package com.devnhq.learnenglish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.HistorysActivity;
import com.devnhq.learnenglish.model.ResultTest;
import com.devnhq.learnenglish.viewholder.HistoryHolder;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    List<ResultTest> resultTests;

    public HistoryAdapter(Context context, List<ResultTest> resultTests) {
        this.context = context;
        this.resultTests = resultTests;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ResultTest resultTest = resultTests.get(position);
        holder.point.setText(resultTest.point);
        holder.date.setText(resultTest.date);
        holder.topic.setText(resultTest.topic);
    }

    @Override
    public int getItemCount() {
        return resultTests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView point;
        TextView  date;
        TextView   topic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.point);
            date = itemView.findViewById(R.id.date);
            topic = itemView.findViewById(R.id.topic);
        }
    }
}
