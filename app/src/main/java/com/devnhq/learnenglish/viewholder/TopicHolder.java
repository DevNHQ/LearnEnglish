package com.devnhq.learnenglish.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.Topic;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicHolder extends RecyclerView.ViewHolder {
    CircleImageView url;
    TextView title,id,topicsdetail;

    public TopicHolder(@NonNull View itemView) {
        super(itemView);
        url = itemView.findViewById(R.id.img_topic);
        title = itemView.findViewById(R.id.tv_topic);
    }

    public void setItem(Topic item){
        title.setText(item.title);
        Picasso.get().load(item.url).resize(100,100).placeholder(R.drawable.opasity).into(url);
    }
}
