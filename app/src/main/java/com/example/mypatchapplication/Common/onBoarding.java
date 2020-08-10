package com.example.mypatchapplication.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mypatchapplication.Common.LoginSignup.ProLogin;
import com.example.mypatchapplication.Helperclass.SliderAdapter;
import com.example.mypatchapplication.R;

public class onBoarding extends AppCompatActivity {

    ViewPager viewPager;
    Button getStartedbtn, skipAllbtn;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    private View decorView;
    TextView[] dots;
    Animation getStarted_anim;
    //getting position for the next button
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        viewPager     = findViewById(R.id.slider);
        getStartedbtn = findViewById(R.id.btn_get_started);
        skipAllbtn    = findViewById(R.id.btn_skip);

        dotsLayout    = findViewById(R.id.dots);
        //calling and assigning slider adapter in view pager
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        //calling dots function to add it in the layout
        addDots(0);
        //adding the page change listner to change the color of dots
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view) {
        Intent intent = new Intent(getApplicationContext(), ProLogin.class);
        intent.putExtra("usertype", "customer");
        startActivity(intent);
        finish();
    }

    public void next(View view) {
        //changing the current position of slider to next
        viewPager.setCurrentItem(currentPosition+1);
    }

    public void callGetStarted(View view) {
        Intent intent = new Intent(getApplicationContext(), ProLogin.class);
        intent.putExtra("usertype", "customer");
        startActivity(intent);
        finish();
    }

    private void addDots(int position) {
        //creating the dots
        dots = new TextView[4];
        //clear the view of the dots at each call
        dotsLayout.removeAllViews();


        for (int i = 0; i < dots.length; i++) {
            //setting context and assigning the dot character using Html.
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            //adding the created dots to the layout created in xml.
            dotsLayout.addView(dots[i]);
        }
        //sets the color of the dots as per the page position
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //no usable content for now
        }

        @Override
        public void onPageSelected(int position) {
            //selecting the current position of the page and passing it to the dots function.
            addDots(position);
           //setting the position to next button
            currentPosition = position;
            if (position < 3) {
                getStartedbtn.setVisibility(View.INVISIBLE);
            } else {
                getStarted_anim = AnimationUtils.loadAnimation(onBoarding.this, R.anim.bottom_anim);
                getStartedbtn.setAnimation(getStarted_anim);
                getStartedbtn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //no usable content for now
        }
    };

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
}