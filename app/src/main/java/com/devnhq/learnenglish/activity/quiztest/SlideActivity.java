package com.devnhq.learnenglish.activity.quiztest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.Exam;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class SlideActivity extends AppCompatActivity {
    DatabaseReference reference;
    int z = 0;
    int correctcount = 0;
    String result;
    int count;
    String giaithich;
    private List<Exam> listExam;
    private RadioGroup radioGroup;
    private RadioButton rdA;
    private RadioButton rdB;
    private RadioButton rdC;
    private RadioButton rdD;
    private TextView tvNum, tvCauHoi, tvThoigian, tvexplain, tvR, tvN;
    private int Thoigian;
    private CounterClass time;
    private CardView carView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        final ProgressDialog pd = new ProgressDialog(SlideActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.incrementProgressBy(2);
        pd.setMessage("Waiting...");
        pd.getWindow();
        pd.show();
        pd.setCancelable(true);
        tvThoigian = findViewById(R.id.tvTimer);
        tvNum = findViewById(R.id.tvNum);
        tvCauHoi = findViewById(R.id.tvQuestion);
        carView = findViewById(R.id.carView);
        tvR = findViewById(R.id.tvR);
        tvN = findViewById(R.id.tvN);
        tvexplain = findViewById(R.id.tvexplain);
        rdA = findViewById(R.id.radA);
        rdB = findViewById(R.id.radB);
        rdC = findViewById(R.id.radC);
        rdD = findViewById(R.id.radD);
        radioGroup = findViewById(R.id.radGroup);
        listExam = new ArrayList<>();
        carView.setVisibility(View.GONE);
        Thoigian = 25;
        count = 20;
        time = new CounterClass(Thoigian * 60 * 1000, 1000);
        time.start();
        reference = FirebaseDatabase.getInstance().getReference("Exams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exam exam = snapshot.getValue(Exam.class);
                    listExam.add(exam);
                }
                for (int i = z; i < 20; i++) {
                    tvNum.setText("Question " + (z + 1) + "|20" + ":");
                    carView.setVisibility(View.GONE);
                    final Random random = new Random();
                    int choose = random.nextInt(160);
                    Exam exam = listExam.get(choose);
                    result = exam.result;
                    giaithich = exam.explain;
                    //cau hoi
                    radioGroup.
                            setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    if (checkedId == R.id.radA || checkedId == R.id.radB || checkedId == R.id.radC || checkedId == R.id.radD) {
                                        carView.setVisibility(View.VISIBLE);
                                        tvR.setText(result.substring(0, 1).toUpperCase() + result.substring(1));
                                        tvN.setText("Result : ");
                                        tvexplain.setText(giaithich);
                                        rdA.setEnabled(false);
                                        rdB.setEnabled(false);
                                        rdC.setEnabled(false);
                                        rdD.setEnabled(false);

                                    }

                                }

                            });
                    tvCauHoi.setText(exam.questions);
                    rdA.setChecked(false);
                    rdB.setChecked(false);
                    rdC.setChecked(false);
                    rdD.setChecked(false);
                    //cau tra loi
                    rdA.setText("A : " + exam.a);
                    rdB.setText("B : " + exam.b);
                    rdC.setText("C : " + exam.c);
                    rdD.setText("D : " + exam.d);
                    radioGroup.clearCheck();
//                    Log.e("z", z + "");
                    z++;
                    break;
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(SlideActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Nextttt(View view) {
        if (rdA.isChecked() || rdB.isChecked() || rdC.isChecked() || rdD.isChecked()) {
            kiemtra();
            quiz();
            radioGroup.clearCheck();
            rdA.setEnabled(true);
            rdB.setEnabled(true);
            rdC.setEnabled(true);
            rdD.setEnabled(true);
            tvR.setText("");
            tvN.setText("");
            tvexplain.setText("");
        } else {
            alerChecknext();
        }

    }

    public void quiz() {
        carView.setVisibility(View.GONE);
        if (z > 19) {
            Intent intent = new Intent(SlideActivity.this, ResultActivity.class);
            intent.putExtra("correctcount", String.valueOf(correctcount));
            intent.putExtra("topic", "TN");
            intent.putExtra("count", String.valueOf(count));
            startActivity(intent);
            finish();
        }
        for (int i = z; i < 20; i++) {
            tvNum.setText("Câu " + (z + 1) + "|20" + ":");
            carView.setVisibility(View.GONE);
            Random random = new Random();
            int choose = random.nextInt(160);
            Exam exam = listExam.get(choose);
            result = exam.result;
            giaithich = exam.explain;
            tvCauHoi.setText(exam.questions);
            rdA.setChecked(false);
            rdB.setChecked(false);
            rdC.setChecked(false);
            rdD.setChecked(false);
            rdA.setText("A : " + exam.a);
            rdB.setText("B : " + exam.b);
            rdC.setText("C : " + exam.c);
            rdD.setText("D : " + exam.d);
            radioGroup.clearCheck();
            z++;
            break;
        }
    }

    public void kiemtra() {
        carView.setVisibility(View.GONE);
        if (rdA.isChecked() || rdB.isChecked() || rdC.isChecked() || rdD.isChecked()) {
            //dalam++
        }
        if (rdA.isChecked()) {
            if (rdA.getText().equals("A : " + result)) {
                correctcount++;

            }
        } else if (rdB.isChecked()) {
            if (rdB.getText().equals("B : " + result)) {
                correctcount++;

            }
        } else if (rdC.isChecked()) {
            if (rdC.getText().equals("C : " + result)) {
                correctcount++;

            }
        } else if (rdD.isChecked()) {
            if (rdD.getText().equals("D : " + result)) {
                correctcount++;

            }
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SlideActivity.this);
        builder.setTitle("Bạn đang trong quá trình làm bài !");
        builder.setMessage("xác nhận thoát ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SlideActivity.this, ExamIdActivity.class);
                startActivity(intent);
                time.cancel();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void alerChecknext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SlideActivity.this);
        builder.setTitle("Bạn phải hoàn thành câu hỏi trước!");
        builder.setPositiveButton("tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        builder.setCancelable(true);
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String countTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            tvThoigian.setText(countTime);
        }

        @Override
        public void onFinish() {
            tvThoigian.setText("00:00");
            time.cancel();
            ShowAlertDialog();
            Intent intent = new Intent(SlideActivity.this, ResultActivity.class);
            intent.putExtra("correctcount", String.valueOf(correctcount));
            intent.putExtra("topic", "Quiz test");
            intent.putExtra("count", count);
            startActivity(intent);

        }

        public void ShowAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SlideActivity.this);
            builder.setTitle("Thông Báo !");
            builder.setMessage("Bạn đã hết thời gian làm bài !");
            builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(SlideActivity.this, ResultActivity.class);
                    intent.putExtra("correctcount", String.valueOf(correctcount));
                    intent.putExtra("topic", "Quiz test");
                    intent.putExtra("count", count);
                    time.cancel();
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
            builder.setCancelable(true);
        }
    }
}
