package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText et1, et2;
    FirebaseAuth fa;

    LinearLayout l;
    RelativeLayout r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        l = findViewById(R.id.inProcessLayout2);
        r = findViewById(R.id.signupRelLayout);

        et1 = findViewById(R.id.signupEmailEditText);
        et2 = findViewById(R.id.signupPwdEditText);
        fa = FirebaseAuth.getInstance();
    }

    public void hideSoftKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void register(View view) {
        hideSoftKeyboard(view);
        String s1 = et1.getText().toString();
        String s2 = et2.getText().toString();
        if(s1.isEmpty() || s2.isEmpty()) {
            Toast.makeText(this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
        }else {
            if (s2.length() < 6)
                //et2.setError("Password should atleast have 6 characters");
                Toast.makeText(this, "Password should atleast have 6 characters", Toast.LENGTH_LONG).show();
            else {
                r.setVisibility(View.INVISIBLE);
                l.setVisibility(View.VISIBLE);
                fa.createUserWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        r.setVisibility(View.VISIBLE);
                        l.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignupActivity.this, "You can Order Coffee, now", Toast.LENGTH_SHORT).show();
                            Intent intn = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intn);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SignupActivity.this.onDestroy();
                                }
                            }, 3700);

                        } else {
                            Toast.makeText(SignupActivity.this, "Unable to create new user!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignupActivity.this, "Check your internet, E-mail Id and try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
