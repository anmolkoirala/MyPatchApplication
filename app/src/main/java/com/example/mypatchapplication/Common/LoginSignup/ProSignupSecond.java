package com.example.mypatchapplication.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Common.InternetConnection;
import com.example.mypatchapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ProSignupSecond extends AppCompatActivity {

    private View decorView;
    ImageView goBackbtn;
    Button nextBtn, loginBtn;
    TextView titleText, pagenumtwo, spinnertitle;
    RadioGroup genderGroup;
    RadioButton selectedGender;
    DatePicker dobDate;
    Spinner caregorySpinner;
    String usertype, profCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_signup_second);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        goBackbtn       = findViewById(R.id.register_go_backtwo);
        nextBtn         = findViewById(R.id.signup_secnextbtntwo);
        loginBtn        = findViewById(R.id.signup_loginbtntwo);
        titleText       = findViewById(R.id.signup_titletwo);
        pagenumtwo      = findViewById(R.id.signup_pagenumtwo);
        genderGroup     = findViewById(R.id.gender_group);
        dobDate         = findViewById(R.id.dob_picker);
        caregorySpinner = findViewById(R.id.spinner_category);
        spinnertitle    = findViewById(R.id.spinner_title);

        usertype = getIntent().getStringExtra("usertype");

        ArrayList<String> categories = new ArrayList<>();
        categories.add(0,"Choose a category");
        categories.add("Cleaners");
        categories.add("Electrician");
        categories.add("Painters");
        categories.add("Tank Cleaners");
        categories.add("Plumbers");
        categories.add("Pest Control");
        categories.add("Roofers");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ProSignupSecond.this,
                R.layout.dropdown_menulist,
                categories
        );
        caregorySpinner.setAdapter(adapter);

        caregorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose a category")){
                    profCategory = "null";
                }else{
                    profCategory = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (usertype != null && usertype.equals("customer")){
            caregorySpinner.setVisibility(View.GONE);
            spinnertitle.setVisibility(View.GONE);
        }
        pagenumtwo.setText("2 / 4");

    }

    public void nextSignupsecondScreen(View view){
        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);

        Intent intentValues = getIntent();
        String fullname = intentValues.getStringExtra("fullname");
        String username = intentValues.getStringExtra("username");
        String email    = intentValues.getStringExtra("email");
        String password = intentValues.getStringExtra("password");

        if(!validateGender() | !validateDOB()){
            return;
        }
        selectedGender = findViewById(genderGroup.getCheckedRadioButtonId());
        String gender = selectedGender.getText().toString();

        int day = dobDate.getDayOfMonth();
        int month = dobDate.getMonth();
        int year = dobDate.getYear();
        String dob = day +"/"+month+"/"+year;

        Intent intent = new Intent(getApplicationContext(),ProAddressSignup.class);
        //transition here
        Pair [] pairs = new Pair[5];
        pairs[0] = new Pair<View,String>(goBackbtn,"transition_backarrow_btn");
        pairs[1] = new Pair<View,String>(nextBtn,"transition_next_button");
        pairs[2] = new Pair<View,String>(loginBtn,"transition_login_button");
        pairs[3] = new Pair<View,String>(titleText,"transition_title_text");
        pairs[4] = new Pair<View,String>(pagenumtwo,"transition_title_pagenum");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProSignupSecond.this,pairs);
        intent.putExtra("fullname", fullname);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("gender", gender);
        intent.putExtra("dob", dob);
        intent.putExtra("usertype", usertype);
        intent.putExtra("category", profCategory);
        startActivity(intent,options.toBundle());
    }

    private boolean validateGender(){
        if(genderGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return  true;
        }
    }

    private boolean validateDOB(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userage     = dobDate.getYear();
        int isAgeValid  = currentYear - userage;

        if (isAgeValid < 13) {
            Toast.makeText(this, "You must be atleast 13 to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void loginScreenBack(View view) {
        Intent intentHome = new Intent(getApplicationContext(),ProLogin.class);
        intentHome.putExtra("usertype",usertype);
        startActivity(intentHome);
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


    //end of removing status bar and default navigation at first
}