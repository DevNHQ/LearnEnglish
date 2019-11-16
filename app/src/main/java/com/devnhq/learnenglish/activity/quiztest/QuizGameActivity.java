package com.devnhq.learnenglish.activity.quiztest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.topic.ToPicActivity;
import com.devnhq.learnenglish.activity.topic.TopicDetailActivity;
import com.devnhq.learnenglish.model.QuizGame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizGameActivity extends AppCompatActivity {
    String[] keys, keys1, keys2, keys3;
    TextView textTime, textCount;
    ImageView imgQuestion;
    Animation smallbigforth;
    DatabaseReference reference;
    String title, title1, check, sound, id, main;
    private MediaPlayer player, playerfail, playerDone;
    private int maxPresCounter, count;
    private String textAnswer;
    private List<QuizGame> listQuiz;
    private CounterClass time;
    private int Thoigian, correctcount = 0, z = 0, presCounter = 0;
    private EditText editText;
    private LinearLayout linearLayout, linearLayoutt;
    private AlertDialog.Builder builderr;
    private AlertDialog alertDialogFail, alertDialogCorrect;
    private Boolean alertErr = false;
    private QuizGame exam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        final ProgressDialog pd = new ProgressDialog(QuizGameActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.incrementProgressBy(2);
        pd.setMessage("Waiting...");
        pd.getWindow();
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        id = getIntent().getExtras().getString("id");
        main = getIntent().getExtras().getString("main");
        builderr = new AlertDialog.Builder(QuizGameActivity.this);
        player = new MediaPlayer();
        playerfail = new MediaPlayer();
        playerDone = new MediaPlayer();
        count = 10;
        textCount = findViewById(R.id.textCount);
        textTime = findViewById(R.id.textTime);
        linearLayout = findViewById(R.id.layoutParent);
        linearLayoutt = findViewById(R.id.layoutParentt);
        editText = findViewById(R.id.editText);
        imgQuestion = findViewById(R.id.imgquiz);
        if (main.equals("quizgamerandom")) {
            reference = FirebaseDatabase.getInstance().getReference("Quizgame").child("Topicsdetail");
        } else {
            reference = FirebaseDatabase.getInstance().getReference("Topics").child(id).child("Topicsdetail");
        }
        listQuiz = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizGame quiz = snapshot.getValue(QuizGame.class);
                    listQuiz.add(quiz);
                }
                for (int i = z; i < 10; i++) {
                    Thoigian = 1;
                    time = new CounterClass(Thoigian * 60 * 750, 1000);
                    time.start();
                    textCount.setText("Câu " + (z + 1) + "|10" + ":");
                    if (main.equals("quizgamerandom")) {
                        final Random random = new Random();
                        int choose = random.nextInt(listQuiz.size());
                        exam = listQuiz.get(choose);
                    } else {
                        exam = listQuiz.get(i);
                    }
                    textAnswer = exam.title;
                    sound = exam.sound;
                    check = exam.title.replaceAll("\\s+", "");
                    Picasso.get().load(exam.url)
                            .placeholder(R.drawable.opasity).error(R.drawable.opasity).into(imgQuestion);
                    if (check.length() > 6) {
                        title = ("F" + exam.title + "P").replaceAll("\\s+", "").substring(0, 7).replaceAll(".(?=.)", "$0 ").trim().toUpperCase();
                        title1 = ("J" + exam.title + "L").replaceAll("\\s+", "").substring(7).replaceAll(".(?=.)", "$0 ").trim().toUpperCase();

                        keys = title.split(" ");
                        smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                        keys = shuffleArray(keys);
                        for (String key : keys) {
                            addView(((LinearLayout) findViewById(R.id.layoutParent)), key, ((EditText) findViewById(R.id.editText)));
                        }
                        keys1 = title1.split(" ");
                        smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                        keys1 = shuffleArray(keys1);
                        for (String key : keys1) {
                            addView(((LinearLayout) findViewById(R.id.layoutParentt)), key, ((EditText) findViewById(R.id.editText)));
                        }
                    } else {
                        title = ("T" + exam.title + "X").replaceAll("\\s+", "").replaceAll(".(?=.)", "$0 ").trim().toUpperCase();
                        keys = title.split(" ");
                        smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                        keys = shuffleArray(keys);
                        for (String key : keys) {
                            addView(((LinearLayout) findViewById(R.id.layoutParent)), key, ((EditText) findViewById(R.id.editText)));
                        }
                    }
                    maxPresCounter = exam.title.replaceAll("\\s+", "").length();
                    z++;
                    break;
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private String[] shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    @SuppressLint("ResourceAsColor")
    private void addView(LinearLayout viewParent, final String text, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutParams.setMargins(0, 0, 0, 0);
        linearLayoutParams.gravity = Gravity.CENTER;

        final TextView textView = new TextView(this);
        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.ic_iconfinder_f_circle_128_308017));
        textView.setTextColor(this.getResources().getColor(R.color.colorBackground));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(18);
        textView.setTextColor(R.drawable.gradients_background_boderbottom);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FredokaOneRegular.ttf");
        textCount.setTypeface(typeface);
        textCount.setTypeface(typeface);
        textTime.setTypeface(typeface);
        textTime.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);
        textView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (presCounter < maxPresCounter) {
                    if (presCounter == 0)
                        editText.setText("");
                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(smallbigforth);
                    textView.animate().alpha(0).setDuration(500);
                    textView.setClickable(false);
                    presCounter++;

                    if (presCounter == maxPresCounter)
                        doValidate();
                }
            }
        });

        viewParent.addView(textView);
    }

    private void doValidate() {
        presCounter = 0;
        if (editText.getText().toString().equals(textAnswer.toUpperCase().replaceAll("\\s+", ""))) {
            correctcount++;
            textTime.setText("00:00");
            time.cancel();
            showCustomDialog();
        } else {
            showCustomDialogFail();
            shakeItBaby();
            editText.setText("");
            linearLayout.removeAllViews();
            linearLayoutt.removeAllViews();
            if (check.length() > 6) {
                keys2 = title.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys2 = shuffleArray(keys2);
                for (String ks : keys2) {
                    addView(((LinearLayout) findViewById(R.id.layoutParent)), ks, ((EditText) findViewById(R.id.editText)));
                }

                keys3 = title1.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys3 = shuffleArray(keys3);
                for (String ks : keys3)
                    addView(((LinearLayout) findViewById(R.id.layoutParentt)), ks, ((EditText) findViewById(R.id.editText)));
            } else {

                keys2 = title.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys2 = shuffleArray(keys2);
                for (String ks : keys2) {
                    addView(((LinearLayout) findViewById(R.id.layoutParent)), ks, ((EditText) findViewById(R.id.editText)));
                }
            }

        }
    }

    private void shakeItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            soundFail();
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            soundFail();
        }
    }

    public void quiz() {
        if (z > 9) {
            Intent intent = new Intent(QuizGameActivity.this, ResultActivity.class);
            intent.putExtra("correctcount", String.valueOf(correctcount));
            intent.putExtra("topic", "Quiz game");
            intent.putExtra("count", String.valueOf(count));
            startActivity(intent);
            finish();
        }
        editText.setText("");
        linearLayout.removeAllViews();
        linearLayoutt.removeAllViews();
        for (int i = z; i < 10; i++) {
            Thoigian = 1;
            time = new CounterClass(Thoigian * 60 * 750, 1000);
            time.start();
            textCount.setText("Câu " + (z + 1) + "|10" + ":");
            if (main.equals("quizgamerandom")) {
                final Random random = new Random();
                int choose = random.nextInt(listQuiz.size());
                exam = listQuiz.get(choose);
            } else {
                exam = listQuiz.get(i);
            }
            textAnswer = exam.title;
            sound = exam.sound;
            Picasso.get().load(exam.url)
                    .placeholder(R.drawable.opasity).error(R.drawable.opasity).into(imgQuestion);
            check = exam.title.replaceAll("\\s+", "");
            if (exam.title.replaceAll("\\s+", "").length() > 6) {
                title = ("N" + exam.title + "F").replaceAll("\\s+", "").substring(0, 7).replaceAll(".(?=.)", "$0 ").trim().toUpperCase();
                title1 = ("O" + exam.title + "K").replaceAll("\\s+", "").substring(7).replaceAll(".(?=.)", "$0 ").trim().toUpperCase();


                keys = title.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys = shuffleArray(keys);
                for (String key : keys) {
                    addView(((LinearLayout) findViewById(R.id.layoutParent)), key, ((EditText) findViewById(R.id.editText)));
                }

                keys1 = title1.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys1 = shuffleArray(keys1);
                for (String key : keys1) {
                    addView(((LinearLayout) findViewById(R.id.layoutParentt)), key, ((EditText) findViewById(R.id.editText)));
                }

            } else {
                title = ("I" + exam.title + "A").replaceAll("\\s+", "").replaceAll(".(?=.)", "$0 ").trim().toUpperCase();
                keys = title.split(" ");
                smallbigforth = AnimationUtils.loadAnimation(QuizGameActivity.this, R.anim.smallbigforth);
                keys = shuffleArray(keys);
                for (String key : keys) {
                    addView(((LinearLayout) findViewById(R.id.layoutParent)), key, ((EditText) findViewById(R.id.editText)));
                }

            }
            maxPresCounter = exam.title.replaceAll("\\s+", "").length();
            z++;
            break;
        }
    }

    public void Click(View view) {

        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(sound);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizGameActivity.this);
        builder.setTitle("Bạn đang trong quá trình làm bài !");
        builder.setMessage("xác nhận thoát ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (main.equals("vocabulary")) {
                    time.cancel();
                    listQuiz.clear();
                    Intent intent = new Intent(QuizGameActivity.this, TopicDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                if (main.equals("quizgame")) {
                    listQuiz.clear();
                    time.cancel();
                    Intent intent = new Intent(QuizGameActivity.this, ToPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("main", "quizgame");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                if (main.equals("quizgamerandom")) {
                    time.cancel();
                    listQuiz.clear();
                    Intent intent = new Intent(QuizGameActivity.this, MenuTestActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_done, viewGroup, false);
        Button btn = dialogView.findViewById(R.id.buttonOk);
        TextView txt = dialogView.findViewById(R.id.textRs);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialogCorrect = builder.create();
        alertDialogCorrect.show();
        alertDialogCorrect.setCanceledOnTouchOutside(false);
        txt.setText("Result: " + textAnswer);
        soundDone();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCorrect.dismiss();
                playerDone.reset();
                quiz();
                alertErr = true;
            }
        });
    }

    private void showCustomDialogFail() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_fail, viewGroup, false);
        Button btn = dialogView.findViewById(R.id.buttonOk);
        builderr.setView(dialogView);
        alertDialogFail = builderr.create();
        alertDialogFail.show();
        alertDialogFail.setCanceledOnTouchOutside(false);
        alertErr = true;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogFail.dismiss();
                alertErr = false;
            }
        });
    }

    public void soundFail() {
        try {
            playerfail.reset();
            playerfail.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playerfail.setDataSource("https://d1490khl9dq1ow.cloudfront.net/audio/sfx/mp3preview/BsTwCwBHBjzwub4i4/jg-032316-sfx-video-game-fail-sound-2_NWM.mp3");
            if (playerfail != null && playerfail.isPlaying()) {
                playerfail.stop();
                playerfail.reset();
                playerfail.release();
                playerfail = null;
            }
            playerfail.prepare();
            playerfail.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void soundDone() {
        try {
            playerDone.reset();
            playerDone.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playerDone.setDataSource("https://d1490khl9dq1ow.cloudfront.net/audio/sfx/mp3preview/BsTwCwBHBjzwub4i4/cheering-2_GJMBAnN__NWM.mp3");
            if (playerDone != null && playerDone.isPlaying()) {
                playerDone.stop();
                playerDone.reset();
                playerDone.release();
                playerDone = null;
            }
            playerDone.prepare();
            playerDone.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Next(View view) {
        textTime.setText("00:00");
        time.cancel();
        quiz();
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String countTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            textTime.setText(countTime);
        }

        @Override
        public void onFinish() {
            if (alertErr == true) {
                alertDialogFail.dismiss();
                textTime.setText("00:00");
                time.cancel();
                quiz();
            } else {
                textTime.setText("00:00");
                time.cancel();
                quiz();
            }
        }
    }
}