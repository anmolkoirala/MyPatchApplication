package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.Model.UserHelperClass;
import com.example.mypatchapplication.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhonenum extends AppCompatActivity {

    private View decorView;
    ImageView goBack;
    Button verifyBtn;
    TextView titleone, titletwo, descp;
    Animation animation;
    PinView userPin;
    String systemCode;
    String fullname, username, email, password, gender, dob, address, phonenum,usertype, whattodo, cat;
    double lat,longi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phonenum);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        goBack = findViewById(R.id.verify_goBack);
        verifyBtn = findViewById(R.id.code_verification_btn);
        titleone = findViewById(R.id.verify_title);
        titletwo = findViewById(R.id.verify_title_small);
        descp = findViewById(R.id.verify_descp);
        userPin = findViewById(R.id.verify_pincode);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_anim);
        goBack.setAnimation(animation);
        verifyBtn.setAnimation(animation);
        titleone.setAnimation(animation);
        titletwo.setAnimation(animation);
        descp.setAnimation(animation);
        userPin.setAnimation(animation);

        //all the user data from signup page
        phonenum = getIntent().getStringExtra("phone");
        fullname = getIntent().getStringExtra("fullname");
        username = getIntent().getStringExtra("username");
        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender   = getIntent().getStringExtra("gender");
        dob      = getIntent().getStringExtra("dob");
        usertype = getIntent().getStringExtra("usertype");
        whattodo = getIntent().getStringExtra("whattodo");
        cat      = getIntent().getStringExtra("category");
        address = getIntent().getStringExtra("address");
        lat = getIntent().getDoubleExtra("lat",0.0);
        longi = getIntent().getDoubleExtra("long",0.0);

        descp.setText("Enter the pass code sent on" + phonenum);
        sendPhoneVerificationCode(phonenum);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProSignupThird.class);
                startActivity(intent);
            }
        });
    }

    private void sendPhoneVerificationCode(String phonenum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenum,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    systemCode = s;
                    //this function will generate the system code and store it in String s and it will be set in global variable systemCode for overall use.
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    //this function will set the code received in the same device with app automatically to pinview field
                    //it will call upon verifyCode function inorder to validate it with system code as well.
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        userPin.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    //this function will deal with a verification failed scenario and pop up the message in toast accordingly,
                    Toast.makeText(VerifyPhonenum.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        //this function will receive the code either manually inserted by the user or when it is automatically set
        //when received in the same phone and app so that they both can be verified
            try {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(systemCode, code);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }catch(Exception e){
                    Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
            }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        //this function will check the credential passed on by verifycode function.
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //when the validation is successfully done, it will incvoke function storeNewUserData to store additional data in database.

                            if (whattodo != null && whattodo.equals("updatedata")) {
                                updateUserdata();
                            } else if (whattodo != null && whattodo.equals("updateprofessionaldata")){
                                updateProfdata();
                            } else if (whattodo != null && whattodo.equals("insertuserdata")) {
                                storeNewUsersData();
                            } else {
                                storeProfUserData();
                            }


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyPhonenum.this, "Verification not completed", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void storeProfUserData() {
        FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth          = FirebaseAuth.getInstance();
        DatabaseReference reference = rootNode.getReference("Professionals");
        String user_id              = mAuth.getCurrentUser().getUid();
        int avgrate                 = 0;
        int ratingcount             = 0;
        String bio                  = "";

        ProfessionalModel newProfUser = new ProfessionalModel(user_id,fullname,username,email,password,address,gender,dob,phonenum,usertype,cat,lat,longi,avgrate, ratingcount, bio);
        reference.child(user_id).setValue(newProfUser);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LatLng latLng = new LatLng(lat,longi);
        DatabaseReference locationsReference = FirebaseDatabase.getInstance().getReference().child("ProfessionalAddress");
        GeoFire geoFire = new GeoFire(locationsReference);
        geoFire.setLocation(uid, new GeoLocation(latLng.latitude, latLng.longitude));

        Toast.makeText(VerifyPhonenum.this, "Code Verified. User Added as service Provider", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), ProLogin.class);
        intent.putExtra("usertype",usertype);
        startActivity(intent);
        finish();
    }

    private void updateUserdata() {
        Intent intent = new Intent(getApplicationContext(),SetNewPassword.class);
        intent.putExtra("phone",phonenum);
        intent.putExtra("usertype",usertype);
        intent.putExtra("whattodo",whattodo);
        startActivity(intent);
        finish();
    }

    private void updateProfdata() {
        Intent intent = new Intent(getApplicationContext(),SetNewPassword.class);
        intent.putExtra("phone",phonenum);
        intent.putExtra("usertype",usertype);
        intent.putExtra("whattodo",whattodo);
        startActivity(intent);
        finish();
    }
    private void storeNewUsersData() {
        //initial point is pointing our application towards database by creating firebase db instance
        //second point will help to create a reference(commonly understood as table) in firebase DB to store all data i.e in our case under Users.
        FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth          = FirebaseAuth.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
        String user_id              = mAuth.getCurrentUser().getUid();

        UserHelperClass newUser = new UserHelperClass(user_id,fullname,username,email,password,address,gender,dob,phonenum,usertype,lat,longi);
        reference.child(user_id).setValue(newUser);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LatLng latLng = new LatLng(lat,longi);
        DatabaseReference locationsReference = FirebaseDatabase.getInstance().getReference().child("UserAddress");
        GeoFire geoFire = new GeoFire(locationsReference);
        geoFire.setLocation(uid, new GeoLocation(latLng.latitude, latLng.longitude));

        Toast.makeText(VerifyPhonenum.this, "Code Verified. User Added", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ProLogin.class);
        intent.putExtra("usertype",usertype);
        startActivity(intent);
        finish();
    }

    public void callNextScreen(View view) {
        //when user manually enters the code, the code is extracted and sent to
        //verify code function to match it with system code.
        String getuserInputCode = userPin.getText().toString();
        if (!getuserInputCode.isEmpty()) {
            verifyCode(getuserInputCode);
        }
        String Phonenum = getIntent().getStringExtra("phone");
//        Intent intent2 = new Intent(getApplicationContext(), SetNewPassword.class);
//        startActivity(intent2);
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