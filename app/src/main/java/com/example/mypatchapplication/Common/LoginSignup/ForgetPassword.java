package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mypatchapplication.Common.InternetConnection;
import com.example.mypatchapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    CountryCodePicker codepicker_forgetPsw;
    TextInputLayout forgetPhonenumLayout;
    private View decorView;
    Button forgotPswNext;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        codepicker_forgetPsw = findViewById(R.id.forgetPsw_codepicker);
        forgetPhonenumLayout = findViewById(R.id.forgetPsw_phonenum);
        forgotPswNext        = findViewById(R.id.forgetPsw_next);

        usertype = getIntent().getStringExtra("usertype");

    }

    public void nextRecOptions(View view) {
        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);
        if( !validatePhoneNumber() ){
            return;
        }

        String phnumber = forgetPhonenumLayout.getEditText().getText().toString();
        final String completePhnum = "+"+codepicker_forgetPsw.getSelectedCountryCode()+phnumber;

        if(usertype != null && usertype.equals("professional")){
            professionalUserCheck(completePhnum);
        }else{
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(completePhnum);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        forgetPhonenumLayout.setError(null);
                        forgetPhonenumLayout.setErrorEnabled(false);

                        Intent intent = new Intent(getApplicationContext(), VerifyPhonenum.class);
                        intent.putExtra("phone", completePhnum);
                        intent.putExtra("whattodo","updatedata");
                        intent.putExtra("usertype",usertype);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ForgetPassword.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ForgetPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void professionalUserCheck(final String phonenum) {
        Query checkprofUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(phonenum);
        checkprofUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    forgetPhonenumLayout.setError(null);
                    forgetPhonenumLayout.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyPhonenum.class);
                    intent.putExtra("phone", phonenum);
                    intent.putExtra("whattodo","update"+usertype+"data");
                    intent.putExtra("usertype",usertype);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(ForgetPassword.this, "Professional User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgetPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forget_GoBack(View view) {
        finish();
    }

    private boolean validatePhoneNumber() {
        String val  = forgetPhonenumLayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            forgetPhonenumLayout.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            forgetPhonenumLayout.setError("No White spaces are allowed!");
            return false;
        }
        forgetPhonenumLayout.setError(null);
        forgetPhonenumLayout.setErrorEnabled(false);
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