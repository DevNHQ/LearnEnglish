package com.devnhq.learnenglish.activity.quiztest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.HistorysActivity;
import com.devnhq.learnenglish.activity.MainActivity;
import com.devnhq.learnenglish.model.ResultTest;
import com.devnhq.learnenglish.model.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {
    int count;
    int correctcount;
    int sai;
    double score;
    String topic, topics;
    FirebaseUser fuser;
    String url, totalscore, totalcount;
    DatabaseReference reference;
    DatabaseReference reference1;
    String TAG = "LoginFaceActivity";
    private TextView tvquestioncorrect, tvquestionerr, tvScore;
    private String ld;
    private Boolean save = false;
    private List<User> users;
    private TextView title;
    private ShareDialog shareDialog;
    private CallbackManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        shareDialog = new ShareDialog(ResultActivity.this);
        manager = CallbackManager.Factory.create();
        title = findViewById(R.id.title);
        Intent intent = getIntent();
        count = Integer.parseInt(intent.getStringExtra("count"));
        correctcount = Integer.parseInt(intent.getStringExtra("correctcount"));
        topic = intent.getStringExtra("topic");
        if (topic.equals("TN")) {
            topics = "Trắc nghiệm";
        } else {
            topics = "Game từ vựng";
        }
        tvquestionerr = findViewById(R.id.tvquestionerr);
        tvquestioncorrect = findViewById(R.id.tvquestioncorrect);
        tvScore = findViewById(R.id.tvScore);
        if (count == 10) {
            sai = count - correctcount;
            score = 1 * correctcount;
            tvquestioncorrect.setText(correctcount + " / " + count);
            tvquestionerr.setText(sai + " / " + count);
            tvScore.setText(1 * correctcount + " / 10");
        } else {
            sai = count - correctcount;
            score = 0.5 * correctcount;
            tvquestioncorrect.setText(correctcount + " / " + count);
            tvquestionerr.setText(sai + " / " + count);
            tvScore.setText(score + " / 10");
        }
        if (score < 7.0) {
            title.setText("Cố gắng thêm nha");
        }
        if (score < 9.0 && score > 7.0) {
            title.setText("Thành tích tuyệt vời");
        }
        if (score > 9) {
            title.setText("Thành tích xuất sắc");
        }
        users = new ArrayList<>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference1 = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
                url = users.get(0).url;
                totalcount = users.get(0).totalcount;
                totalscore = users.get(0).totalscore;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        manager = CallbackManager.Factory.create();
        shareDialog.registerCallback(manager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(ResultActivity.this.getApplicationContext(), "Share success!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fb onSuccess");
            }

            @Override
            public void onCancel() {
                Toast.makeText(ResultActivity.this.getApplicationContext(), "Did not share", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fb onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ResultActivity.this.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fb onError");
            }
        }, 90);
    }

    public void insertHistory() {
        users.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cld = Calendar.getInstance();
        String date = sdf.format(cld.getTime());
        Calendar calendar1 = Calendar.getInstance();
        Random random = new Random();
        String currentId = "history" + calendar1.getTimeInMillis() + random.nextInt(100) + random.nextInt(100);
        ResultTest resultTest = new ResultTest(currentId, String.valueOf(correctcount), String.valueOf(sai), Double.toString(score), topics, date, url);
        FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("history").child(currentId).setValue(resultTest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(ResultActivity.this, "save successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResultActivity.this, HistorysActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResultActivity.this, "save error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalcount", "1");
        map.put("totalscore", score + "");
        FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {

                } else {
                }
            }
        });
    }

    public void insertNotdefault() {
        users.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cld = Calendar.getInstance();
        String date = sdf.format(cld.getTime());
        Calendar calendar1 = Calendar.getInstance();
        Random random = new Random();
        String currentId = "history" + calendar1.getTimeInMillis() + random.nextInt(100) + random.nextInt(100);
        ResultTest resultTest = new ResultTest(currentId, String.valueOf(correctcount), String.valueOf(sai), Double.toString(score), topics, date, url);
        FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("history").child(currentId).setValue(resultTest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(ResultActivity.this, "save successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResultActivity.this, HistorysActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResultActivity.this, "save error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String n = "1";
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalcount", (Integer.parseInt(n) + Integer.parseInt(totalcount)) + "");
        map.put("totalscore", (score + Double.parseDouble(totalscore)) + "");
        FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {

                } else {
                }
            }
        });
    }

    public void ShowCustomDialog() {
        if (score < 7) {
            ld = "Thành tích chưa thực sự tốt";
        }
        if (score > 7) {
            ld = "Lưu điểm";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);

        builder.setTitle(ld);
        builder.setMessage("Bạn có muốn lưu ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save = true;
                if (totalcount.equals("0")) {
                    insertHistory();
                    finish();
                } else {
                    insertNotdefault();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
        builder.setCancelable(true);
    }

    public void saveResult(View view) {
        ShowCustomDialog();
    }

    public void Replay(View view) {
        Intent intent = new Intent(ResultActivity.this, MenuTestActivity.class);
        startActivity(intent);
        finish();

    }

    public void Exit(View view) {
        eXit();
    }

    @Override
    public void onBackPressed() {
        eXit();
    }

    public void eXit() {
        if (save = true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
            builder.setTitle("Bạn chưa lưu điểm !");
            builder.setMessage("xác nhận thoát ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();

        } else {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void Share(View view) {
        checkInternet();
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ResultActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            final ProgressDialog progress = new ProgressDialog(ResultActivity.this);
            progress.setMessage("Không có kết nối mạng...");
            progress.setCancelable(true);
            progress.show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            final ProgressDialog progress = new ProgressDialog(ResultActivity.this);
            progress.setMessage("Không có kết nối mạng...");
            progress.setCancelable(true);
            progress.show();
            return false;
        }
        if (!networkInfo.isAvailable()) {
            final ProgressDialog progress = new ProgressDialog(ResultActivity.this);
            progress.setMessage("Không có kết nối mạng...");
            progress.setCancelable(true);
            progress.show();
            return false;
        } else {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://drive.google.com/drive/u/0/folders/1dT4JgX0Zik4KF3PpoPKPCtcqnrYQLHa0"))
                    .setQuote("Learn English " + " - " + " " +
                            "Thành tích : " + score + " điểm")
                    .build();
            shareDialog.show(content);
            return true;
        }
    }

    public void Back(View view) {
        startActivity(new Intent(ResultActivity.this, MenuTestActivity.class));
        finish();
    }
}
