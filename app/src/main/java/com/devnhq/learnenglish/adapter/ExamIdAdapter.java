package com.devnhq.learnenglish.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.quiztest.ExamIdActivity;
import com.devnhq.learnenglish.activity.quiztest.SlideActivity;

import java.util.List;

public class ExamIdAdapter extends RecyclerView.Adapter<ExamIdAdapter.ExamHolder> {
    ExamIdActivity context;
    List<String> examid;

    public ExamIdAdapter(ExamIdActivity context, List<String> examid) {
        this.context = context;
        this.examid = examid;
    }

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_examid, parent, false);
        return new ExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        holder.tvItemThiThu.setText(examid.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SlideActivity.class);
                context.startActivity(intent);
                context.finish();


            }
        });
    }

    @Override
    public int getItemCount() {
        return examid.size();
    }

    public class ExamHolder extends RecyclerView.ViewHolder {
        LinearLayout linearItemThiThu;
        TextView tvItemThiThu;
        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            linearItemThiThu = (LinearLayout) itemView.findViewById(R.id.linearItemThiThu);
            tvItemThiThu = (TextView) itemView.findViewById(R.id.tvItemThiThu);
        }
    }
}
