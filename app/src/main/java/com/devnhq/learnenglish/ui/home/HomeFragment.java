package com.devnhq.learnenglish.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.grammar.GrammarActivity;
import com.devnhq.learnenglish.activity.HistorysActivity;
import com.devnhq.learnenglish.activity.account.LoginActivity;
import com.devnhq.learnenglish.activity.grammar.MenuMoreappActivity;
import com.devnhq.learnenglish.activity.quiztest.MenuTestActivity;
import com.devnhq.learnenglish.activity.topic.ToPicActivity;
import com.devnhq.learnenglish.model.User;
import com.devnhq.learnenglish.network.ConnectivityHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CardView  car_vocabulary,car_test,grammar,cr_grammar;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser fuser;
    private List<User> listUsers;
    private CircleImageView img_user;
    private TextView tvName,tvEmail;
    private ImageView next_history;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if (!ConnectivityHelper.isConnectedToNetwork(getActivity())) {
            Toast.makeText(getActivity(), "no internet access", Toast.LENGTH_SHORT).show();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkMermission();
                }
            }, 100);
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.incrementProgressBy(2);
            pd.setMessage("Loading...");
            pd.getWindow();
            pd.show();
            pd.setCancelable(true);
            car_vocabulary = root.findViewById(R.id.card_vocabulary);
            car_test = root.findViewById(R.id.card_test);
            grammar = root.findViewById(R.id.quizgame);
            cr_grammar = root.findViewById(R.id.cr_grammar);
            tvName = root.findViewById(R.id.tvName);
            tvEmail = root.findViewById(R.id.tvEmail);
            img_user = root.findViewById(R.id.profile_image);
            next_history = root.findViewById(R.id.next_history);
            listUsers = new ArrayList<>();
            //
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), HistorysActivity.class));

                }
            });
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    listUsers.add(user);
                    String name, url, mail;
                    name = listUsers.get(0).getName();
                    url = listUsers.get(0).getUrl();
                    mail = listUsers.get(0).getEmail();
                    tvName.setText(name);
                    tvEmail.setText("Welcom to LearnEnglish");
                    if (listUsers.get(0).getUrl().equals("default")) {
                        img_user.setImageResource(R.drawable.loaduser);
                    } else {
                        Picasso.get().load(url).placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(img_user);
                    }
                    pd.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
            next_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Logout !");
                    builder.setMessage("confirm ");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }

            });
            car_vocabulary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ToPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("main", "vocabulary");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            car_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MenuTestActivity.class));
                }
            });
            grammar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), GrammarActivity.class));

                }
            });
            cr_grammar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MenuMoreappActivity.class));
                }
            });
        }

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void checkMermission(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.INTERNET

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()){
                    checkMermission();
                } else if (report.areAllPermissionsGranted()){
                } else {
                    checkMermission();
                }

            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}