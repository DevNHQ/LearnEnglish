package com.devnhq.learnenglish.viewholder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.TopicDetail;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailHolder extends RecyclerView.ViewHolder {
    private MediaPlayer player;
    CircleImageView url, soundd;
    TextView title, clause, translate, example;

    public TopicDetailHolder(@NonNull View itemView) {
        super(itemView);
        url = itemView.findViewById(R.id.img_topic_detail);
        soundd = itemView.findViewById(R.id.img_sound_topic);
        title = itemView.findViewById(R.id.tv_title_topicdetail);
        clause = itemView.findViewById(R.id.tv_clause_topicdetail);
        translate = itemView.findViewById(R.id.tv_translate_topicdetail);
        example = itemView.findViewById(R.id.tv_example_topicdetail);
        player = new MediaPlayer();

    }

    public void setItem(final TopicDetail item) {
        title.setText(item.title.substring(0, 1).toUpperCase() + item.title.substring(1));
        clause.setText(item.clause);
        translate.setText("Translate: " + item.translate.substring(0, 1).toUpperCase() + item.translate.substring(1));
        example.setText("Example: " + item.example.substring(0, 1).toUpperCase() + item.example.substring(1));
        Picasso.get().load(item.url).resize(150, 150).placeholder(R.drawable.opasity).into(url);
        soundd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = item.sound;
                try {
                    player.reset();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(url);
                    if (player != null && player.isPlaying()) {
                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                    }
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
