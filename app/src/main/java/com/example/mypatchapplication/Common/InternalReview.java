package com.example.mypatchapplication.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.InternalReviewModel;
import com.example.mypatchapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InternalReview extends AppCompatActivity {

    private View decorView;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextInputLayout customer_review ;
    TextView customer_review_desc;
    RatingBar customer_rating;
    String userid, userType;
    Button btnsubmit_review;
    int currentrating = 0;
    String reviewtxt;
    String scontact ,sgender , sname  ,semail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_review);


        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });


        userType = getIntent().getStringExtra("usertype");

        customer_review    = findViewById(R.id.apprate_usertext);
        customer_review_desc    = findViewById(R.id.apprate_descp);

        customer_rating = findViewById(R.id.app_rating);
        btnsubmit_review    = findViewById(R.id.apprate_sendreview);

        customer_review_desc.setVisibility(View.GONE);


        customer_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int userrates = (int) rating;
                currentrating = (int) ratingBar.getRating();

                if(userrates == 1){
                    customer_review_desc.setVisibility(View.VISIBLE);
                    customer_review_desc.setText(R.string.app_first_review);
                }
                else if(userrates == 2){
                    customer_review_desc.setVisibility(View.VISIBLE);
                    customer_review_desc.setText(R.string.app_second_review);
                }
                else if(userrates == 3){
                    customer_review_desc.setVisibility(View.VISIBLE);
                    customer_review_desc.setText(R.string.rate_three);
                }
                else if(userrates == 4){
                    customer_review_desc.setVisibility(View.VISIBLE);
                    customer_review_desc.setText(R.string.rate_four);
                }
                else if(userrates == 5){
                    customer_review_desc.setVisibility(View.VISIBLE);
                    customer_review_desc.setText(R.string.rate_five);
                }
            }
        });

        //getting needed values form the session
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
        scontact  = userdetails.get(SessionHolder.KEY_PHONENUMBER);
        sgender   = userdetails.get(SessionHolder.KEY_GENDER);
        sname     = userdetails.get(SessionHolder.KEY_FULLNAME);
        semail    = userdetails.get(SessionHolder.KEY_EMAIL);
        userid    = userdetails.get(SessionHolder.KEY_USERID);



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("ApplicationReview");

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("userID").getValue().equals(userid)) {
                        customer_rating.setRating(keyId.child("apprating").getValue(int.class));
                        customer_review.getEditText().setText(keyId.child("reivewdescription").getValue(String.class));
                        btnsubmit_review.setText("Update Review");
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("s", "Failed to read value.", error.toException());
            }
        });

    }

    public void appreview_Goback(View view) {
        InternalReview.super.onBackPressed();
    }

    public void addAppReview(View view) {

        if (!validateReview()) {
            return;
        }

        if(currentrating == 0){
            Toast.makeText(InternalReview.this, "Please rate us first.", Toast.LENGTH_SHORT).show();
        }else{
            reviewtxt = customer_review.getEditText().getText().toString().trim();
            DatabaseReference rateus_db = FirebaseDatabase.getInstance().getReference().child("ApplicationReview");
            InternalReviewModel internalReviewModel = new InternalReviewModel(sname,userid,scontact,sgender,semail,userType,reviewtxt,currentrating);
            rateus_db.child(userid).setValue(internalReviewModel); //adding review info to database
            Toast.makeText(this, "Your review was added successfully", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean validateReview() {
        String val = customer_review.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            customer_review.setError("Field cannot be empty");
            return false;
        } else {
            customer_review.setError(null);
            customer_review.setErrorEnabled(false);
            return true;
        }
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