package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypatchapplication.Common.InternetConnection;
import com.example.mypatchapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SetNewPassword extends AppCompatActivity {

    private View decorView;
    Animation animation;
    LinearLayout newCredentials;
    Button setNewPassBtn;
    String phonenum;
    TextInputLayout newpassword, confirmpassword;
    String usertype,whattodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        newCredentials   = findViewById(R.id.new_credentials_layout);
        setNewPassBtn    = findViewById(R.id.newpass_setbtn);
        newpassword      = findViewById(R.id.newPassword_layout);
        confirmpassword  = findViewById(R.id.newConfirmPassword_layout);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_anim);
        newCredentials.setAnimation(animation);

        usertype = getIntent().getStringExtra("usertype");
        whattodo = getIntent().getStringExtra("whattodo");
        phonenum = getIntent().getStringExtra("phone");
        Toast.makeText(this, whattodo, Toast.LENGTH_SHORT).show();
    }

    public void callSetnewPassword(View view) {
        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);

        if (!validateNewPassword() | !validateNewConfirmPassword()) {
            return;
        }

        final String newPsw = newpassword.getEditText().getText().toString().trim();

        if(whattodo != null && whattodo.equals("updateprofessionaldata")){
            updateProfPassword(newPsw);
        }else{
            final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(phonenum);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            String userid = datas.getKey();
                            String systemPassword = datas.child("password").getValue().toString();

                            if(systemPassword.equals(newPsw)){
                                Toast.makeText(SetNewPassword.this, "Old password cannot be new Password", Toast.LENGTH_SHORT).show();
                            }else{
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(userid).child("password").setValue(newPsw);

                                Intent intent = new Intent(getApplicationContext(),PasswordChangeSuccess.class);
                                intent.putExtra("usertype", usertype);
                                startActivity(intent);
                                finish();
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SetNewPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void updateProfPassword(final String pass) {
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(phonenum);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String userid = datas.getKey();
                        String systemPassword = datas.child("password").getValue().toString();

                        if(systemPassword.equals(pass)){
                            Toast.makeText(SetNewPassword.this, "Old password cannot be new Password", Toast.LENGTH_SHORT).show();
                        }else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Professionals");
                            reference.child(userid).child("password").setValue(pass);

                            Intent intent = new Intent(getApplicationContext(),PasswordChangeSuccess.class);
                            intent.putExtra("usertype", usertype);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SetNewPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateNewPassword() {
        String val   = newpassword.getEditText().getText().toString().trim();
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
            newpassword.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            newpassword.setError("Password should contain 4 characters!");
            return false;
        }
        newpassword.setError(null);
        newpassword.setErrorEnabled(false);
        return true;
    }

    private boolean validateNewConfirmPassword() {
        String val       = confirmpassword.getEditText().getText().toString().trim();
        String newpass   = newpassword.getEditText().getText().toString().trim();
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
            confirmpassword.setError("Field can not be empty");
            return false;
        } else if(!val.equals(newpass)){
            confirmpassword.setError("Confirm password does not match with new password!");
            return false;
        } else if (!val.matches(checkPassword)) {
            confirmpassword.setError("Password should contain 4 characters!");
            return false;
        }
        confirmpassword.setError(null);
        confirmpassword.setErrorEnabled(false);
        return true;
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