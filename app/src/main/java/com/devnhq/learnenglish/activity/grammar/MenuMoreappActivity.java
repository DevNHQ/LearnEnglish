package com.devnhq.learnenglish.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.application.AppRatingsActivity;
import com.devnhq.learnenglish.activity.application.TERMSOFUSEActivity;

public class MenuMoreappActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_moreapp);
    }

    public void Rateapp(View view) {
        startActivity(new Intent(MenuMoreappActivity.this, AppRatingsActivity.class));
        finish();
    }

    public void Termsofuse(View view) {
        startActivity(new Intent(MenuMoreappActivity.this, TERMSOFUSEActivity.class));
        finish();
    }

    public void Usermanual(View view) {
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}
