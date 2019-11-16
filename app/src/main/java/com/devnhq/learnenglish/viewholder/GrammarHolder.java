package com.devnhq.learnenglish.viewholder;

import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.Grammar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrammarHolder extends RecyclerView.ViewHolder {
    CircleImageView url;
    TextView title,id;

    public GrammarHolder(@NonNull View itemView) {
        super(itemView);
        url = itemView.findViewById(R.id.img_grammar);
        title = itemView.findViewById(R.id.title_grammar);
    }
    public void setItem(Grammar item){
        title.setText(item.title);
        Picasso.get().load(item.url).resize(100,100).placeholder(R.drawable.opasity).into(url);
    }

}
