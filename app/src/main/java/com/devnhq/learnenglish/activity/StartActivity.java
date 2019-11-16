package com.devnhq.learnenglish.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.account.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import ir.alirezabdn.wp7progress.WP7ProgressBar;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView = findViewById(R.id.imgmhc);
        textView = findViewById(R.id.tvmhc);
        textView.clearAnimation();
        final WP7ProgressBar progressBar = findViewById(R.id.wp7progressBar);
        progressBar.showProgressBar();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                if (fuser != null) {


                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}

