package com.devnhq.learnenglish.activity.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Animation animFadein;
    Button sigUp, btnLogin;
    LinearLayout linearLayout2, ln_login, ln_bt_anm, LnEDIText;
    TextView forgot;
    private EditText email_login, password_login;
    private ProgressDialog prsDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            prsDlg = new ProgressDialog(this);
            mAuth = FirebaseAuth.getInstance();
            email_login = findViewById(R.id.edt_Email_login);
            password_login = findViewById(R.id.edt_password_login);
            btnLogin = findViewById(R.id.btLogin);
            forgot = findViewById(R.id.loginFace);
            linearLayout2 = findViewById(R.id.linearLayout2);
            sigUp = findViewById(R.id.sigup);
            ln_bt_anm = findViewById(R.id.ln_bt_anm);
            ln_login = findViewById(R.id.ln_login);
            LnEDIText = findViewById(R.id.LnEDIText);
            Faded();

            sigUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itDangKy = new Intent(LoginActivity.this, SigUpActivity.class);
                    startActivity(itDangKy);

                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Loging();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Loging() {
        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();
        if (email.isEmpty()) {
            email_login.setError(getText(R.string.emty));
            email_login.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            password_login.setError(getText(R.string.emty));
            password_login.requestFocus();
            return;
        } else {
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loging...");
            pd.getWindow();
            pd.show();
            pd.setCancelable(false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();


                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }

    public void signIn(View view) {

    }

    public void customEdt() {
        email_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                }
                return false;
            }
        });
        password_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //perfrom your action
                }
                return false;
            }
        });
    }

    public void showProgressDialog() {
        prsDlg.setMessage("Please wait...");
        prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prsDlg.setIndeterminate(true);
        prsDlg.setCancelable(false);
        prsDlg.show();
    }

    public void dismissProgressDialog() {
        if (prsDlg != null) {
            if (prsDlg.isShowing()) {
                prsDlg.dismiss();
            }
        }

    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            dismissProgressDialog();
            Connect();
        } else {
            eR();
        }

        return isConnected;
    }

    public void Connect() {
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setTitle("Waiting...");
        progress.setCancelable(false);
        progress.show();
        progress.dismiss();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                progress.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);
    }

    public void eR() {
        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("check internet connection !");
        progress.setTitle("No internet access...");
        progress.setCancelable(false);
        progress.show();
        progress.dismiss();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();

            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);
    }

    public void Back(View view) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void Faded() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_left_to_right);
        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zomin);
        Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zomin);
        Animation animation6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_in);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animtion_up);
        Animation animation7 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_out);
        LnEDIText.startAnimation(animation7);
        ln_bt_anm.startAnimation(animation5);
        ln_login.startAnimation(animation6);
        forgot.startAnimation(animation4);
        linearLayout2.startAnimation(animation);
        btnLogin.startAnimation(animation6);
    }

    public void Forgotpass(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    @Override
    public void onBackPressed() {

    }

}
