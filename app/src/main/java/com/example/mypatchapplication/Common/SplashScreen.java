package com.example.mypatchapplication.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypatchapplication.Common.LoginSignup.ProLogin;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserDashboard;

public class SplashScreen extends AppCompatActivity {

    ImageView backgroundImage;
    TextView loadingText;
    //splash screen animation
    Animation sideAnim, bottomAnim;
    SharedPreferences onBoardPreference;

    //setting the splash delay timer vari able
    private static int SPLASH_TIMER = 3000;
    private View decorView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        backgroundImage   = findViewById(R.id.splash_image);
        loadingText       = findViewById(R.id.loading_content);
        //for splash screen animation
        sideAnim          = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim        = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //setting the animation on elements
        backgroundImage.setAnimation(sideAnim);
        loadingText.setAnimation(bottomAnim);

        //handler to manage the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardPreference     = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstAppUse = onBoardPreference.getBoolean("firsttime", true);
                if(isFirstAppUse){
                    SharedPreferences.Editor editor = onBoardPreference.edit();
                    editor.putBoolean("firsttime", false);
                    editor.apply();

                    Intent intent = new Intent(SplashScreen.this, onBoarding.class);
                    startActivity(intent);
                    finish();
                }else {

                    SessionHolder sessionHolder = new SessionHolder(SplashScreen.this);
                    if( sessionHolder.checkLogin()){
                        Intent intent = new Intent(SplashScreen.this, UserDashboard.class);
                        intent.putExtra("usertype","customer");
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreen.this, ProLogin.class);
                        intent.putExtra("usertype","customer");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        },SPLASH_TIMER);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
       return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
}