package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Common.InternetConnection;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.WifiConnection;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserDashboard;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ProLogin extends AppCompatActivity {

    private View decorView;
    Button forgotPsw, userloginBtn, usercreateAcc;
    CheckBox rememberme;
    TextInputLayout phoneNumber, password;
    CountryCodePicker loginCodePicker;
    WifiConnection wifiConnection = new WifiConnection();
    String userType;
    ImageView headerImage;
    TextView beAPro;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_login);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        phoneNumber       = findViewById(R.id.login_phonenum);
        password          = findViewById(R.id.login_password);
        loginCodePicker   = findViewById(R.id.login_code_picker);
        rememberme        = findViewById(R.id.login_rememberne);
        forgotPsw         = findViewById(R.id.login_forgotpassword);
        userloginBtn      = findViewById(R.id.login_button);
        usercreateAcc     = findViewById(R.id.login_createUserAcc);
        headerImage       = findViewById(R.id.login_header_image);
        beAPro            = findViewById(R.id.login_beapro);
        progressbar       = findViewById(R.id.progress_bar_layout);

        progressbar.setVisibility(View.GONE);

        userType = getIntent().getStringExtra("usertype");

        if(userType.equals("customer")){
            headerImage.setImageResource(R.drawable.patch);
        }else if(userType.equals("professional")){
            headerImage.setImageResource(R.drawable.goback);
            headerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            beAPro.setText(R.string.user_login);
            beAPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ProLogin.class);
                    i.putExtra("usertype", "customer");
                    startActivity(i);
                }
            });
        }

    }

    public void callProWelcome(View view) {
        Intent intent = new Intent(ProLogin.this, StartUpScreen.class);
        intent.putExtra("usertype","professional");
        startActivity(intent);
    }

    public void callUserSignIn(View view) {
        Intent intent = new Intent(ProLogin.this, ProSignUp.class);
        intent.putExtra("usertype",userType);
        startActivity(intent);
    }

    public void forgotPasswordCall(View view) {
        Intent intent = new Intent(getApplicationContext(), SelectRecoveryOptions.class);
        intent.putExtra("usertype", userType);
        startActivity(intent);
    }

    public void userLoginApp(View view) {

        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);
        if (!validatePhoneNumber() | !validatePassword()) {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        String phone = phoneNumber.getEditText().getText().toString().trim();
        final String passw = password.getEditText().getText().toString().trim();
        final String compPhonenum = "+"+loginCodePicker.getSelectedCountryCode()+phone;

        if(userType != null && userType.equals("professional")){
            professionaLogin(compPhonenum, passw);
        }else{
            final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(compPhonenum);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);

                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            String systemPassword = datas.child("password").getValue().toString();
                            if (systemPassword.equals(passw)) {
                                password.setError(null);
                                password.setErrorEnabled(false);

                                String userid = datas.getKey();
                                String fullname  = datas.child("fullname").getValue().toString();
                                String username  = datas.child("username").getValue().toString();
                                String email     = datas.child("email").getValue().toString();
                                String dob       = datas.child("dob").getValue().toString();
                                String address   = datas.child("address").getValue().toString();
                                String gender    = datas.child("gender").getValue().toString();
                                String phone     = datas.child("phonenumber").getValue().toString();
                                String passwrd   = datas.child("password").getValue().toString();
                                String usertype  = datas.child("usertype").getValue().toString();

                                boolean loggedin = false;
                                if (rememberme.isChecked()){
                                    loggedin = true;
                                }
                                SessionHolder sessionHolder = new SessionHolder(ProLogin.this);
                                sessionHolder.createLoginSession(loggedin,userid,fullname,username,email,passwrd,address,gender,dob,phone,usertype);
                                Intent intent = new Intent(ProLogin.this, UserDashboard.class);
                                startActivity(intent);
                            } else {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(ProLogin.this, "Password does not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(ProLogin.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(ProLogin.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    private void professionaLogin(String phone, final String passe) {
        final Query checkProfUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(phone);
        checkProfUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String systemPassword = datas.child("password").getValue().toString();
                        if (systemPassword.equals(passe)) {
                            password.setError(null);
                            password.setErrorEnabled(false);
                            String userid = datas.getKey();
                            String fullname  = datas.child("fullname").getValue().toString();
                            String username  = datas.child("username").getValue().toString();
                            String email     = datas.child("email").getValue().toString();
                            String dob       = datas.child("dob").getValue().toString();
                            String address   = datas.child("address").getValue().toString();
                            String gender    = datas.child("gender").getValue().toString();
                            String phone     = datas.child("phonenumber").getValue().toString();
                            String category  = datas.child("category").getValue().toString();
                            String passwrd   = datas.child("password").getValue().toString();
                            String usertype  = datas.child("usertype").getValue().toString();

                            boolean loggedin = false;
                            if (rememberme.isChecked()){
                                loggedin = true;
                            }
                            SessionHolder sessionHolder = new SessionHolder(ProLogin.this);
                            sessionHolder.createLoginSession(loggedin,userid,fullname,username,email,passwrd,address,gender,dob,phone,usertype);
                            Intent intent = new Intent(ProLogin.this, UserDashboard.class);
                            startActivity(intent);
                        } else {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(ProLogin.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(ProLogin.this, "Professional User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProLogin.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword() {
        String val   = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +        //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                //"\\A\\w{1,20}\\z" +     //no white spaces
                ".{4,}" +                  //at least 4 characters
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