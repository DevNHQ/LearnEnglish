package com.devnhq.learnenglish.activity.topic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.TopicDetail;
import com.devnhq.learnenglish.activity.quiztest.QuizGameActivity;
import com.devnhq.learnenglish.viewholder.TopicDetailHolder;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class TopicDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference mDatabase;
    FirebaseRecyclerPagingAdapter<TopicDetail, TopicDetailHolder> mAdapter;
    String id, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        id = getIntent().getExtras().getString("id");
        Log.e("id", id + "");
        title = getIntent().getExtras().getString("title");
        recyclerView = findViewById(R.id.rcv_topic_detail);
        mSwipeRefreshLayout = findViewById(R.id.srlLayout);
        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Topics").child(id).child("Topicsdetail");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(3)
                .setPrefetchDistance(3)
                .setPageSize(2)
                .build();
        DatabasePagingOptions<TopicDetail> options = new DatabasePagingOptions.Builder<TopicDetail>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, TopicDetail.class)
                .build();
        //
        mAdapter = new FirebaseRecyclerPagingAdapter<TopicDetail, TopicDetailHolder>(options) {
            @NonNull
            @Override
            public TopicDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TopicDetailHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topicdetail, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final TopicDetailHolder holder,
                                            int position,
                                            @NonNull final TopicDetail model) {
                holder.setItem(model);

            }

            @SuppressLint("ResourceAsColor")
            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
                        mSwipeRefreshLayout.setNestedScrollingEnabled(true);
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setNestedScrollingEnabled(false);
                        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
                        break;

                    case FINISHED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };
        recyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0)
//                {
//                    bottomNav.setVisibility(View.GONE);
//                }
//                else {
//                    bottomNav.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }
    public void Back(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
      back();
    }
    public void  back(){
        Intent intent = new Intent(TopicDetailActivity.this, ToPicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("main", "vocabulary");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    public void Next(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TopicDetailActivity.this);
        builder.setTitle("Ôn tập từ vựng bằng trò chơi!");
        builder.setMessage("hãy chắc chắn rằng bạn đã học tất cả các từ vựng trong chủ đề này");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(TopicDetailActivity.this, QuizGameActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("id",id);
                bundle1.putString("main","vocabulary");
                intent.putExtras(bundle1);
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
    }

}
