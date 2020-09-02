package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.AddReviewsAdapter;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.Helperclass.ReviewHistoryAdapter;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserReviewHistory extends AppCompatActivity {

    private View decorView;
    String current_ID,current_name;
    RecyclerView historyRecycler;
    RelativeLayout progressHistory;
    TextView historyDescp;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference historyRef;
    private List<ReviewModel> reviewHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review_history);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String, String> userdetails = sessionHolder.getUserSessionDetails();
        current_ID     = userdetails.get(SessionHolder.KEY_USERID);
        current_name   = userdetails.get(SessionHolder.KEY_FULLNAME);

        historyRecycler = findViewById(R.id.recycler_reviewHistory);
        historyDescp = findViewById(R.id.reviewHistory_descp);
        progressHistory = findViewById(R.id.reviewHistory_progressbar);

        progressHistory.setVisibility(View.VISIBLE);
        historyDescp.setVisibility(View.GONE);

        historyRef = database.getReference("Reviews").child(current_ID);
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressHistory.setVisibility(View.GONE);
                    historyDescp.setVisibility(View.VISIBLE);
                    for (DataSnapshot datas: dataSnapshot.getChildren()){
                        String innerID           = datas.getKey();
                        String reviewTexts       = datas.child("reviewText").getValue().toString();
                        String professionalname  = datas.child("professionalname").getValue().toString();
                        String professionalID    = datas.child("professionalID").getValue().toString();
                        String professionalcat   = datas.child("professionalcategory").getValue().toString();
                        String reviewrating      = datas.child("reviewRating").getValue().toString();
                        String chargedpp         = datas.child("chargedprice").getValue().toString();
                        String userissue         = datas.child("userIssue").getValue().toString();
                        int finalrate             = Integer.parseInt(reviewrating);
                        int finalPP               = Integer.parseInt(chargedpp);

                        reviewHistoryList.add(new ReviewModel(innerID,current_name,current_ID,userissue,professionalname,professionalID,professionalcat,reviewTexts,finalrate, finalPP));
                        ReviewHistoryAdapter reviewHistoryAdapter = new ReviewHistoryAdapter(reviewHistoryList, UserReviewHistory.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserReviewHistory.this);
                        historyRecycler.setLayoutManager(layoutManager);
                        historyRecycler.setHasFixedSize(true);
                        historyRecycler.setAdapter(reviewHistoryAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reviewHistory_goback(View view) {
        UserReviewHistory.super.onBackPressed();
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