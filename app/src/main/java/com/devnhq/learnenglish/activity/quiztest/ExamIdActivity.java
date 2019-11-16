package com.devnhq.learnenglish.activity.quiztest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.adapter.ExamIdAdapter;

import java.util.ArrayList;
import java.util.Random;

public class ExamIdActivity extends AppCompatActivity {
    ExamIdAdapter examIdAdapter;
    ArrayList<String> list;
    private RecyclerView rcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_id);
        rcv = findViewById(R.id.rcv_examid);
        getListExam();
    }

    public void getListExam() {
        list = new ArrayList<>();
        for (int i = 1; i < 51; i++) {
            Random random = new Random();

            list.add("Đề " + random.nextInt(1000));

        }


        examIdAdapter = new ExamIdAdapter(ExamIdActivity.this, list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rcv.setLayoutManager(manager);
        rcv.setAdapter(examIdAdapter);
    }

    public void Back(View view) {
        startActivity(new Intent(ExamIdActivity.this, MenuTestActivity.class));
        finish();
    }
}
