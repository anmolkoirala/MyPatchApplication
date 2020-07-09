package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ProSignupThird extends AppCompatActivity {

    private View decorView;
    TextView pagenumthree;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    String whattodo;
    String address;
    double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_signup_third);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        phoneNumber       = findViewById(R.id.phonenumber_layout);
        pagenumthree      = findViewById(R.id.signup_pagenumthree);
        countryCodePicker = findViewById(R.id.code_picker);

        address = getIntent().getStringExtra("address");
        lat = getIntent().getDoubleExtra("lat",0.0);
        longi = getIntent().getDoubleExtra("long",0.0);
        pagenumthree.setText("4 / 4");
    }


    public void callForVerification(View view) {
        if (!validatePhoneNumber()) {
            return;
        }
        Intent intentValues = getIntent();
        final String fullname  = intentValues.getStringExtra("fullname");
        final String username  = intentValues.getStringExtra("username");
        final String email     = intentValues.getStringExtra("email");
        final String password  = intentValues.getStringExtra("password");
        final String gender    = intentValues.getStringExtra("gender");
        final String dob       = intentValues.getStringExtra("dob");
        final String usertype  = intentValues.getStringExtra("usertype");
        final String category  = intentValues.getStringExtra("category");
        String getNum    = phoneNumber.getEditText().getText().toString().trim();
        final String Phone     = "+"+countryCodePicker.getSelectedCountryCode()+getNum;

        if(category == null && usertype != null && usertype.equals("customer")){
            whattodo = "insertuserdata";
        }
        else{
            whattodo = "insertprofdata";
        }

        if(usertype != null && usertype.equals("professional")){
            checkProfessionalUser(Phone, fullname,username,email,password, gender,dob,usertype,address,category);
        } else{
            final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(Phone);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Toast.makeText(ProSignupThird.this, "A user is already registered with this number.", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(getApplicationContext(),VerifyPhonenum.class);
                        intent.putExtra("fullname", fullname);
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("gender", gender);
                        intent.putExtra("address", address);
                        intent.putExtra("lat", lat);
                        intent.putExtra("long", longi);
                        intent.putExtra("dob", dob);
                        intent.putExtra("phone",Phone);
                        intent.putExtra("usertype",usertype);
                        intent.putExtra("category",category);
                        intent.putExtra("whattodo",whattodo);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProSignupThird.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    private void checkProfessionalUser(final String phones, final String fname, final String uname, final String eml, final String psw, final String gend, final String dobs, final String usrtyp, final String addrss, final String cats) {
        final Query checkProfUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(phones);
        checkProfUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(ProSignupThird.this, "A Professional is already registered with this number.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),VerifyPhonenum.class);
                    intent.putExtra("fullname", fname);
                    intent.putExtra("username", uname);
                    intent.putExtra("email", eml);
                    intent.putExtra("password", psw);
                    intent.putExtra("gender", gend);
                    intent.putExtra("address", address);
                    intent.putExtra("lat", lat);
                    intent.putExtra("long", longi);
                    intent.putExtra("dob", dobs);
                    intent.putExtra("phone",phones);
                    intent.putExtra("usertype",usrtyp);
                    intent.putExtra("category",cats);
                    intent.putExtra("whattodo",whattodo);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProSignupThird.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validatePhoneNumber() {
        String val  = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        }
        phoneNumber.setError(null);
        phoneNumber.setErrorEnabled(false);
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



    //end of removing status bar and default navigation at first
}