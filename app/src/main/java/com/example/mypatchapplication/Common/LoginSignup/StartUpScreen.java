package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mypatchapplication.R;

public class StartUpScreen extends AppCompatActivity {

    private View decorView;
    Button loginButton,signUpbutton;
    String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        loginButton  = findViewById(R.id.btn_pro_login);
        signUpbutton = findViewById(R.id.btn_pro_signup);
        userType = getIntent().getStringExtra("usertype");
    }
    public void screenGoBack(View view) {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void callProLoginScreen(View view){
        Intent intent = new Intent(getApplicationContext(), ProLogin.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(loginButton, "transition_proLogin");
        intent.putExtra("usertype","professional");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(StartUpScreen.this, pairs);
        startActivity(intent,activityOptions.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void callProSignupScreen(View view){
        Intent intent = new Intent(getApplicationContext(), ProSignUp.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(signUpbutton, "transition_proSignup");
        intent.putExtra("usertype","professional");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(StartUpScreen.this, pairs);
        startActivity(intent,activityOptions.toBundle());

    }

    //removing status bar and default navigation at first
    //appears after screen is pulled down.
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


    //end of removing status bar and default navigation at first
}