package com.example.mypatchapplication.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.R;

public class SelectRecoveryOptions extends AppCompatActivity implements View.OnClickListener {

    private View decorView;
    Animation animation;
    ImageView goback;
    RelativeLayout layoutone, layouttwo;
    TextView chooseTitle, chooseDescp;
    Button viaPhone, viaMail;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recovery_options);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        goback = findViewById(R.id.choose_back_icon);
        layoutone = findViewById(R.id.choose_first_layout);
        layouttwo = findViewById(R.id.choose_second_layout);
        chooseTitle = findViewById(R.id.choose_method_title);
        chooseDescp = findViewById(R.id.choose_method_descp);
        viaPhone = findViewById(R.id.recovery_via_phone);
        viaMail = findViewById(R.id.recovery_via_mail);

        viaMail.setOnClickListener(this);
        viaPhone.setOnClickListener(this);
        goback.setOnClickListener(this);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_anim);
        goback.setAnimation(animation);
        layoutone.setAnimation(animation);
        chooseTitle.setAnimation(animation);
        layouttwo.setAnimation(animation);
        chooseDescp.setAnimation(animation);

        usertype = getIntent().getStringExtra("usertype");

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recovery_via_mail) {
            Toast.makeText(this, "This feature is not available yet!", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.recovery_via_phone) {
            Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(viaPhone, "transition_forgotPsw");
            intent.putExtra("usertype", usertype);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SelectRecoveryOptions.this, pairs);
            startActivity(intent,activityOptions.toBundle());
        }
        if (v.getId() == R.id.choose_back_icon) {
            Intent intent = new Intent(getApplicationContext(), ProLogin.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);

        }
    }
    //end of removing status bar and default navigation at first
}