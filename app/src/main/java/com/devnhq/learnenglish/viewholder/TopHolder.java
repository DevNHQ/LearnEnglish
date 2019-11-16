package com.devnhq.learnenglish.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopHolder  extends RecyclerView.ViewHolder {
    TextView name, totalcount, totalscore;
    CircleImageView imgurl;

    public TopHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_Top);
        totalcount = itemView.findViewById(R.id.totalcount_top);
        totalscore = itemView.findViewById(R.id.totalpoint_top);
        imgurl = itemView.findViewById(R.id.img_top);

    }

    public void setItem(User item) {
        name.setText(item.name);
        totalcount.setText(item.totalcount);
        totalscore.setText(item.totalscore);
        if (item.url.equals("default")){
            imgurl.setImageResource(R.drawable.loaduser);
        }
        else{
            Picasso.get().load(item.url)
                    .placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(imgurl);
        }

    }
}
