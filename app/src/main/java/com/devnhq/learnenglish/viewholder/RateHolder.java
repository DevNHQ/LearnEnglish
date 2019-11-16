package com.devnhq.learnenglish.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.RateApp;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateHolder extends RecyclerView.ViewHolder {
    TextView tvComment,tvName,tvDate;
    CircleImageView img_rate;
    RatingBar ratingBar;
    public RateHolder(@NonNull View itemView) {
        super(itemView);
        tvComment = itemView.findViewById(R.id.tvComment);
        ratingBar = itemView.findViewById(R.id.ratingbar);
        tvName = itemView.findViewById(R.id.tv_nameRate);
        tvDate = itemView.findViewById(R.id.tv_date);
        img_rate = itemView.findViewById(R.id.imgRate);

    }
    public void setItem(RateApp item){
        tvComment.setText(item.comment);
        ratingBar.setRating(Float.parseFloat(item.star));
        tvName.setText(item.name);
        tvDate.setText("Cập nhật: "+item.date);
        if (item.url.equals("default")){
            img_rate.setImageResource(R.drawable.loaduser);
        }
        else{
            Picasso.get().load(item.url).placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(img_rate);
        }
    }
}
