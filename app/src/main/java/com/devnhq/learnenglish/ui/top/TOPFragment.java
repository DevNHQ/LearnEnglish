package com.devnhq.learnenglish.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.adapter.TopAdapter;
import com.devnhq.learnenglish.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TOPFragment extends Fragment {
    DatabaseReference mDatabase;
    private TopAdapter mTeamAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<User> users = new ArrayList<>();
    private TextView name, score, count;
    private ImageView img;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
        mSwipeRefreshLayout = root.findViewById(R.id.srlLayout);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mRecyclerView = root.findViewById(R.id.rcv_top);
        name = root.findViewById(R.id.name);
        img = root.findViewById(R.id.img_top1);
        textView = root.findViewById(R.id.click);
        count = root.findViewById(R.id.count);
        score = root.findViewById(R.id.score);
        StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        users = new ArrayList<>();



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot
                            .getValue(User.class);
                    users.add(user);
                }
                Collections.sort(users, User.BY_NAME_ALPHABETICAL);
                textView.setText("TOP 1");
                name.setText(users.get(0).getName());
                score.setText("Thành tích: " + users.get(0).getTotalscore() + " điểm");
                count.setText("Đã làm: " + users.get(0).getTotalcount() + " bài");
                if (users.get(0).getUrl().equals("default")) {
                    img.setImageResource(R.drawable.loaduser);
                } else {
                    Picasso.get().load(users.get(0).url).placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(img);
                }
                users.remove(0);
                mTeamAdapter = new TopAdapter(users, getActivity());
                mRecyclerView.setAdapter(mTeamAdapter);
                mTeamAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    bottomNav.setVisibility(View.GONE);
                } else {
                    bottomNav.setVisibility(View.VISIBLE);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUserVisibleHint(isVisible());
            }
        });
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}