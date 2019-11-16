package com.devnhq.learnenglish.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devnhq.learnenglish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Animation animFadein;
    Button sigUp, btnLogin;
    EditText send_email;
    LinearLayout linearLayout2, ln_login, ln_bt_anm, LnEDIText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btnLogin = findViewById(R.id.btLogin);
        linearLayout2 = findViewById(R.id.linearLayout2);
        sigUp = findViewById(R.id.sigup);
        send_email = findViewById(R.id.edt_Email_confirm);
        ln_bt_anm = findViewById(R.id.ln_bt_anm);
        ln_login = findViewById(R.id.ln_login);
        LnEDIText = findViewById(R.id.LnEDIText);
        firebaseAuth = FirebaseAuth.getInstance();
        Faded();
    }

    public void Confirm(View view) {
        final String email = send_email.getText().toString();

        if (email.equals("")) {
            Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email của bạn!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Kiểm tra email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    } else {
                        String error = task.getException().getMessage();
                        send_email.setError(error);
                    }
                }
            });
        }
    }

    public void Faded() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_left_to_right);
        Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zomin);
        Animation animation6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_in);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animtion_up);
        Animation animation7 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_out);
        LnEDIText.startAnimation(animation7);
        ln_bt_anm.startAnimation(animation5);
        ln_login.startAnimation(animation6);
        linearLayout2.startAnimation(animation);
        btnLogin.startAnimation(animation6);
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }
}
