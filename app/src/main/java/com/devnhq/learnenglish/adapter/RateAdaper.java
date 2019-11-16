package com.devnhq.learnenglish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.RateApp;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateAdaper extends RecyclerView.Adapter<RateAdaper.ViewHolder> {
    private List<RateApp> rateList;
    private Context context;

    public RateAdaper(List<RateApp> rateList, Context context) {
        this.rateList = rateList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate,parent,false);
        return  new RateAdaper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  RateApp rate = rateList.get(position);
        holder.tvComment.setText(rate.comment);
        holder.ratingBar.setRating(Float.parseFloat(rate.star));
        holder.tvName.setText(rate.name);
        holder.tvDate.setText("Cập nhật: " +rate.date);
        if (rate.url.equals("default")){
            holder.img_rate.setImageResource(R.drawable.loaduser);
        }
        else{
            Picasso.get().load(rate.url).placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(holder.img_rate);
        }
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvComment,tvName,tvDate;
        CircleImageView img_rate;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            tvName = itemView.findViewById(R.id.tv_nameRate);
            tvDate = itemView.findViewById(R.id.tv_date);
            img_rate = itemView.findViewById(R.id.imgRate);
        }
    }
}
