package com.devnhq.learnenglish.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.ResultTest;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HistoryHolder extends RecyclerView.ViewHolder {
    TextView point,date,topic;
    CircleImageView imgurl;
    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        point = itemView.findViewById(R.id.point);
        date = itemView.findViewById(R.id.date);
        topic = itemView.findViewById(R.id.topic);
        imgurl = itemView.findViewById(R.id.imgurl);

    }

    public void setItem(ResultTest item){
        point.setText("Thành tích: "+item.point +" điểm");
        date.setText("Ngày làm: "+item.date);
        topic.setText("Chủ đề: "+item.topic);
    }
}
