package com.devnhq.learnenglish.activity.grammar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.Grammar;
import com.devnhq.learnenglish.viewholder.GrammarHolder;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class GrammarActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseRecyclerPagingAdapter<Grammar, GrammarHolder> mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar2);
        recyclerView = findViewById(R.id.rcv_grammar);
        back = findViewById(R.id.back);
        mSwipeRefreshLayout = findViewById(R.id.srlLayout);
        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Grammar");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(3)
                .setPrefetchDistance(3)
                .setPageSize(2)
                .build();
        DatabasePagingOptions<Grammar> options = new DatabasePagingOptions.Builder<Grammar>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Grammar.class)
                .build();
        //
        mAdapter = new FirebaseRecyclerPagingAdapter<Grammar, GrammarHolder>(options) {
            @NonNull
            @Override
            public GrammarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new GrammarHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grammar, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final GrammarHolder holder,
                                            int position,
                                            @NonNull final Grammar model) {
                holder.setItem(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent().setClass(view.getContext(), GrammarDetailActivity.class);
                        Bundle bundle = new Bundle();
                        String id = model.id;
                        bundle.putString("id", id);
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                        view.getContext().fileList();

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

}
