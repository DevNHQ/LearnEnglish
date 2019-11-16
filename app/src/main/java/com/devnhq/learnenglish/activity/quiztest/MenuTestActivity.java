package com.devnhq.learnenglish.activity.quiztest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.topic.ToPicActivity;

public class MenuTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }

    public void QuizGame(View view) {
        Intent intent = new Intent(MenuTestActivity.this, ToPicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("main", "quizgame");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void Quiz_Multiplechoice(View view) {
        startActivity(new Intent(MenuTestActivity.this, ExamIdActivity.class));
        finish();
    }

    public void QuizGameRandom(View view) {
        Intent intent = new Intent(MenuTestActivity.this, QuizGameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("main", "quizgamerandom");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
