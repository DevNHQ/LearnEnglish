package com.devnhq.learnenglish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {
    private List<User> mSortedList;
    private Context context;

    public TopAdapter(List<User> mSortedList, Context context) {
        this.mSortedList = mSortedList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false);
        return new TopAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user =  mSortedList.get(position) ;
        int top = position + 2;
        holder.name.setText(user.name.substring(0,1).toUpperCase()+user.name.substring(1));
        holder.totalcount.setText("Bài đã làm: "+user.totalcount);
        holder.totalscore.setText("Thành tích: "+user.totalscore);
        holder.top_counts.setText("top"+top);
        if (user.url.equals("default")){
            holder.imgurl.setImageResource(R.drawable.loaduser);
        }
        else{
            Picasso.get().load(user.url)
                    .placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(holder.imgurl);
        }
    }

    @Override
    public int getItemCount() {
        return mSortedList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public   TextView name, totalcount, totalscore,top_counts;
        public  CircleImageView imgurl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_Top);
            totalcount = itemView.findViewById(R.id.totalcount_top);
            totalscore = itemView.findViewById(R.id.totalpoint_top);
            top_counts = itemView.findViewById(R.id.top_counts);
            imgurl = itemView.findViewById(R.id.img_top);
        }
    }
}
