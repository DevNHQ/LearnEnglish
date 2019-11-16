package com.devnhq.learnenglish.activity.topic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.Topic;
import com.devnhq.learnenglish.activity.quiztest.QuizGameActivity;
import com.devnhq.learnenglish.viewholder.TopicHolder;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class ToPicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference mDatabase;
    FirebaseRecyclerPagingAdapter<Topic, TopicHolder> mAdapter;
    private String main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_pic);
        main = getIntent().getExtras().getString("main");
        recyclerView = findViewById(R.id.rcv_topic);
        mSwipeRefreshLayout = findViewById(R.id.srlLayout);
        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Topics");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(3)
                .setPrefetchDistance(3)
                .setPageSize(2)
                .build();
        DatabasePagingOptions<Topic> options = new DatabasePagingOptions.Builder<Topic>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Topic.class)
                .build();
        //
        mAdapter = new FirebaseRecyclerPagingAdapter<Topic, TopicHolder>(options) {
            @NonNull
            @Override
            public TopicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TopicHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final TopicHolder holder,
                                            int position,
                                            @NonNull final Topic model) {
                holder.setItem(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (main.equals("vocabulary")) {
                            Intent intent = new Intent().setClass(view.getContext(), TopicDetailActivity.class);
                            Bundle bundle = new Bundle();
                            String id = model.id;
                            String title = model.title;
                            bundle.putString("id", id);
                            bundle.putString("title", title);
                            intent.putExtras(bundle);
                            view.getContext().startActivity(intent);
                            finish();
                        }
                        if (main.equals("quizgame")){
                            Intent intent = new Intent().setClass(view.getContext(), QuizGameActivity.class);
                            Bundle bundle = new Bundle();
                            String id = model.id;
                            String title = model.title;
                            bundle.putString("id", id);
                            bundle.putString("title", title);
                            bundle.putString("main",main);
                            intent.putExtras(bundle);
                            view.getContext().startActivity(intent);
                            finish();
                        }
                    }
                });
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

    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}
