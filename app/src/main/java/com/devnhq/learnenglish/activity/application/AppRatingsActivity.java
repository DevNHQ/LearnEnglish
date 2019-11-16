package com.devnhq.learnenglish.activity.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.grammar.MenuMoreappActivity;
import com.devnhq.learnenglish.adapter.RateAdaper;
import com.devnhq.learnenglish.model.RateApp;
import com.devnhq.learnenglish.model.User;
import com.devnhq.learnenglish.viewholder.RateHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AppRatingsActivity extends AppCompatActivity {
    FirebaseRecyclerPagingAdapter<RateApp, RateHolder> mAdapter;
    DatabaseReference reference;
    DatabaseReference referenceyes;
    DatabaseReference referenceall;
    FirebaseUser fuser;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button btnCancel, btnDone;
    private EditText comment;
    private TextView update;
    private RatingBar rateting;
    private List<User> userList;
    private List<RateApp> rateApps;
    private List<RateApp> rateAppss;
    private List<RateApp> rateAppsall;
    private RateAdaper rateAdaper;
    private String url, name, rateapp;
    private Button btnRate;
    private int stars, stars1, stars2, stars3, stars4, stars5;
    private int count;
    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_ratings);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btnRate = findViewById(R.id.btnRate);
        userList = new ArrayList<>();
        rateApps = new ArrayList<>();
        rateAppsall = new ArrayList<>();
        rateAppss = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_rate);
        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout = findViewById(R.id.srlLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(AppRatingsActivity.this, AppRatingsActivity.class));
                rateApps.clear();
                rateAppsall.clear();
                rateAppss.clear();
                finish();
            }
        });
        //
        referenceall = FirebaseDatabase.getInstance().getReference().child("Rate");
//        referenceall.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    RateApp app = snapshot.getValue(RateApp.class);
//                    rateAppsall.add(app);
//                }
//                for (int i=0;i<rateAppsall.size();i++){
//                    if (Integer.parseInt(rateAppsall.get(i).getStart()) == 1){
//                        stars1++;
//                        Log.e("start1", String.valueOf(stars1));
//                    }
//                    if (Integer.parseInt(rateAppsall.get(i).getStart()) == 2){
//                        stars2++;
//                        Log.e("start2", String.valueOf(stars2));
//                    }
//                    if (Integer.parseInt(rateAppsall.get(i).getStart()) == 3){
//                        stars3++;
//                        Log.e("start3", String.valueOf(stars3));
//                    }
//                    if (Integer.parseInt(rateAppsall.get(i).getStart()) == 4){
//                        stars4++;
//                        Log.e("start4", String.valueOf(stars4));
//                    }
//                    if (Integer.parseInt(rateAppsall.get(i).getStart()) == 5){
//                        stars5++;
//                        Log.e("start5", String.valueOf(stars5));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User listUser = dataSnapshot.getValue(User.class);
                userList.add(listUser);
                url = userList.get(0).url;
                name = userList.get(0).name;
                rateapp = userList.get(0).rateapp;
                if (rateapp.equals("not")) {
                    btnRate.setText("Viết đánh giá");
                } else {
                    btnRate.setText("Chỉnh sửa đánh giá");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        referenceall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RateApp rateApp = snapshot.getValue(RateApp.class);
                    rateAppss.add(rateApp);
                }
                Collections.sort(rateAppss, RateApp.BY_Rate_ALPHABETICAL);
                rateAdaper = new RateAdaper(rateAppss, AppRatingsActivity.this);
                recyclerView.setAdapter(rateAdaper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    btnRate.setVisibility(View.GONE);
                } else {
                    btnRate.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void Rate(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        dialog = new BottomSheetDialog(this);
        btnCancel = dialogView.findViewById(R.id.cancel);
        btnDone = dialogView.findViewById(R.id.done);
        comment = dialogView.findViewById(R.id.edt_comment);
        update = dialogView.findViewById(R.id.update);
        rateting = dialogView.findViewById(R.id.rating);
        if (rateapp.equals("not")) {
            btnDone.setText("Đánh giá");
        } else {
            referenceyes = FirebaseDatabase.getInstance().getReference().child("Rate").child(fuser.getUid());
            referenceyes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RateApp rateApp = dataSnapshot.getValue(RateApp.class);
                    rateApps.add(rateApp);
                    comment.setText(rateApps.get(0).getComment());
                    rateting.setRating(Float.parseFloat(rateApps.get(0).star));
                    update.setText("Cập nhật gần nhất: " + rateApps.get(0).getDate());
                    stars = Integer.parseInt(rateApps.get(0).star);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            btnDone.setText("Cập nhật");

        }
        rateting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float touchPositionX = event.getX();
                float width = rateting.getWidth();
                float starsf = (touchPositionX / width) * 5.0f;
                if (stars == 0) {
                    stars = 1;
                } else {
                    stars = (int) starsf + 1;
                }
                rateting.setRating(stars);
                return false;
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRate();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void insertRate() {
        if (comment.getText().toString().trim().replaceAll("\\n", "").equals("") || rateting.getRating() == 0) {
            Toast.makeText(AppRatingsActivity.this, "Can not be empty!", Toast.LENGTH_SHORT).show();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss' - 'dd/MM/yyyy");
            Calendar cld = Calendar.getInstance();
            String date = sdf.format(cld.getTime());
            final RateApp rateApp = new RateApp(fuser.getUid(), name, url, comment.getText().toString().trim(), Integer.toString(stars), date, String.valueOf(cld.getTimeInMillis()));
            final ProgressDialog pd = new ProgressDialog(AppRatingsActivity.this);
            pd.setMessage("Waiting...");
            pd.show();
            pd.setCancelable(false);
            FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("rate").setValue(rateApp, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    FirebaseDatabase.getInstance().getReference("Rate").child(fuser.getUid()).setValue(rateApp, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            final HashMap<String, Object> map = new HashMap<>();
                            map.put("rateapp", "yes");
                            FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(AppRatingsActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dialog.dismiss();
                                        rateApps.clear();
                                        rateAppsall.clear();
                                        rateAppss.clear();
                                        startActivity(new Intent(AppRatingsActivity.this, AppRatingsActivity.class));
                                        finish();

                                    } else {
                                        mAdapter.refresh();
                                        pd.dismiss();
                                        Toast.makeText(AppRatingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
            });
        }


    }


    public void Back(View view) {
        startActivity(new Intent(AppRatingsActivity.this, MenuMoreappActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AppRatingsActivity.this, MenuMoreappActivity.class));
        finish();
    }

}
