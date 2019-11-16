package com.devnhq.learnenglish.activity.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SigUpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference reference;
    private EditText editName, editEmail, editPassword, editRePassword;
    private Toolbar toolbar;
    private ProgressDialog prsDlg;
    private String tt;
    private TextView tv_anm_sigup;
    private LinearLayout ln_anm_sigup;
    private LinearLayout ln_all_anm, ln_bt_anm, linearLayout2, LnEDIText;
    private Button btn_anm_sigup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_up);
        mAuth = FirebaseAuth.getInstance();
        editName = findViewById(R.id.edtUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editRePassword = findViewById(R.id.editRePassword);
        tv_anm_sigup = findViewById(R.id.txt_anim_sigup);
        ln_anm_sigup = findViewById(R.id.ln_anim_sigup);
        ln_all_anm = findViewById(R.id.ln_bg_anm);
        LnEDIText = findViewById(R.id.LnEDIText);
        btn_anm_sigup = findViewById(R.id.btn_anim_sigup);
        linearLayout2 = findViewById(R.id.linearLayout2);
        ln_bt_anm = findViewById(R.id.ln_bt_anm);
        prsDlg = new ProgressDialog(this);
        fade();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null && checkNetworkConnection()) {
        }

    }

    public void signUp(View view) {
        // get all value in edit text
        final String name = editName.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String repassword = editRePassword.getText().toString().trim();
        if (name.equals("")) {
            editName.setError(getText(R.string.emty));
            return;
        }
        if (email.equals("")) {
            editEmail.setError(getText(R.string.emty));
            return;
        }
        if (password.equals("")) {
            editPassword.setError(getText(R.string.emty));
            return;
        }
        if (repassword.equals("")) {
            editRePassword.setError(getText(R.string.emty));
            return;
        }
        if (name.length() < 3 || name.length() > 20) {
            editName.setError(getText(R.string.lenght));
            return;
        }
        if (password.length() < 6 || password.length() > 20) {
            editPassword.setError(getText(R.string.lenght));
            return;
        }
        if (!password.equals(repassword)) {
            editRePassword.setError(getText(R.string.confirmpass));
            return;
        } else {
            register(name, email, password);
        }
    }

    private void register(final String name, final String email, final String password) {
        final ProgressDialog pd = new ProgressDialog(SigUpActivity.this);
        pd.setMessage("Creating...");
        pd.getWindow();
        pd.show();
        pd.setCancelable(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(SigUpActivity.this, "Created account successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("name", name);
                            hashMap.put("url", "default");
                            hashMap.put("email", email);
                            hashMap.put("totalscore", "" + 0.0);
                            hashMap.put("totalcount", "" + 0);
                            hashMap.put("rateapp", "not");
                            hashMap.put("password", password);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(SigUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(SigUpActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(SigUpActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Back(View view) {
        Intent intent = new Intent(SigUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog() {
        prsDlg.setMessage(tt);
        prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prsDlg.setIndeterminate(true);
        prsDlg.setCancelable(false);
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                prsDlg.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
        prsDlg.show();
    }

    public void dismissProgressDialog() {
        if (prsDlg != null) {
            if (prsDlg.isShowing()) {
                prsDlg.dismiss();
            }
        }

    }

    //
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            Connect();
        } else {
            eR();
        }

        return isConnected;
    }

    public void Connect() {
        final ProgressDialog progress = new ProgressDialog(SigUpActivity.this);
        progress.setTitle("Waiting...");
        progress.setCancelable(false);
        progress.show();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
    }

    public void eR() {
        final ProgressDialog progress = new ProgressDialog(SigUpActivity.this);
        progress.setMessage("check internet connection !");
        progress.setTitle("No internet access...");
        progress.setCancelable(false);
        progress.show();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();

            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);
    }

    public void fade() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_left_to_right);
        Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zomin);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_right_to_left);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_out);
        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_in);
        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animati_blink);
        Animation animation6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_fade);
        ln_anm_sigup.startAnimation(animation1);
        tv_anm_sigup.startAnimation(animation2);
        btn_anm_sigup.startAnimation(animation3);
        ln_all_anm.startAnimation(animation4);
        ln_bt_anm.startAnimation(animation5);
        linearLayout2.startAnimation(animation);
        LnEDIText.startAnimation(animation6);
    }
}
