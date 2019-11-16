package com.devnhq.learnenglish.activity.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.grammar.MenuMoreappActivity;

public class TERMSOFUSEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsofuse);
    }

    public void Back(View view) {
        startActivity(new Intent(TERMSOFUSEActivity.this, MenuMoreappActivity.class));
        finish();
    }
}
