package com.example.mypatchapplication.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypatchapplication.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProSignUp extends AppCompatActivity {

    private View decorView;
    ImageView goBackbtn;
    Button nextBtn, loginBtn;
    TextView titleText, pagenumone;
    TextInputLayout fullname, username, email, password;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_sign_up);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        goBackbtn  = findViewById(R.id.register_go_backone);
        nextBtn    = findViewById(R.id.signup_nextbtnone);
        loginBtn   = findViewById(R.id.signup_loginbtnone);
        titleText  = findViewById(R.id.signup_titleone);
        pagenumone = findViewById(R.id.signup_pagenumone);

        fullname   = findViewById(R.id.fullname_layout);
        username   = findViewById(R.id.username_layout);
        email      = findViewById(R.id.email_layout);
        password   = findViewById(R.id.password_layout);

        pagenumone.setText("1 / 4");
        userType = getIntent().getStringExtra("usertype");

        goBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void nextSignupScreen(View view){

        if (!validateFullname() | !validateUsername() | !validateEmail() | !validatePassword()) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(),ProSignupSecond.class);
        //transition here
        Pair [] pairs = new Pair[5];
        pairs[0] = new Pair<View,String>(goBackbtn,"transition_backarrow_btn");
        pairs[1] = new Pair<View,String>(nextBtn,"transition_next_button");
        pairs[2] = new Pair<View,String>(loginBtn,"transition_login_button");
        pairs[3] = new Pair<View,String>(titleText,"transition_title_text");
        pairs[4] = new Pair<View,String>(pagenumone,"transition_title_pagenum");

        String fname  = fullname.getEditText().getText().toString().trim();
        String Uname  = username.getEditText().getText().toString().trim();
        String eml    = email.getEditText().getText().toString().trim();
        String psw    = password.getEditText().getText().toString().trim();

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProSignUp.this,pairs);
        intent.putExtra("fullname", fname);
        intent.putExtra("username", Uname);
        intent.putExtra("email", eml);
        intent.putExtra("password", psw);
        intent.putExtra("usertype", userType);
        startActivity(intent,options.toBundle());
    }

    private boolean validateFullname() {
        String val = fullname.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("No White spaces are allowed!");
            return false;
        }
        username.setError(null);
        username.setErrorEnabled(false);
        return true;
    }

//    private boolean validateAddress() {
//        String val = address.getEditText().getText().toString().trim();
//        //String checkspaces = "\\A\\w{1,20}\\z";
//        if (val.isEmpty()) {
//            address.setError("Field can not be empty");
//            return false;
//        } else if (val.length() > 35) {
//            address.setError("Address is too large!");
//            return false;
//        }
//        address.setError(null);
//        address.setErrorEnabled(false);
//        return true;
//    }

    private boolean validateEmail() {
        String val   = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        }
        email.setError(null);
        email.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        String val   = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"\\A\\w{1,20}\\z" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters!");
            return false;
        }
        password.setError(null);
        password.setErrorEnabled(false);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    //end of removing status bar and default navigation at first.



}
