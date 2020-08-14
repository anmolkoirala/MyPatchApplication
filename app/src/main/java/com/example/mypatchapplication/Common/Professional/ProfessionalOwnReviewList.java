package com.example.mypatchapplication.Common.Professional;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.Helperclass.ProfessionalOwnReviewListAdapter;
import com.example.mypatchapplication.Helperclass.ReviewHistoryAdapter;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserReviewHistory;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfessionalOwnReviewList extends AppCompatActivity {

    private View decorView;
    String current_ID,current_name;
    RecyclerView historyRecycler;
    RelativeLayout progressHistory;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allReviewsRef;
    private List<ReviewModel> reviewHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_own_review_list);
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

        historyRecycler = findViewById(R.id.recycler_user_reviewlist);
        progressHistory = findViewById(R.id.user_reviewlist_progressbar);

        progressHistory.setVisibility(View.VISIBLE);

        allReviewsRef = database.getReference("Reviews");
        allReviewsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    progressHistory.setVisibility(View.GONE);
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {

                        if(datas.child("professionalID").getValue().toString().equals(current_ID)) {
                            String innerID = datas.getKey();
                            String reviewTexts = datas.child("reviewText").getValue().toString();
                            String professionalname = datas.child("professionalname").getValue().toString();
                            String professionalID = datas.child("professionalID").getValue().toString();
                            String professionalcat = datas.child("professionalcategory").getValue().toString();
                            String reviewrating = datas.child("reviewRating").getValue().toString();
                            String userissue = datas.child("userIssue").getValue().toString();
                            String username = datas.child("username").getValue().toString();
                            int finarate = Integer.parseInt(reviewrating);

                            reviewHistoryList.add(new ReviewModel(username, userissue, professionalname, professionalID, professionalcat, reviewTexts, finarate));
                            ProfessionalOwnReviewListAdapter professionalOwnReviewListAdapter = new ProfessionalOwnReviewListAdapter(reviewHistoryList, ProfessionalOwnReviewList.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfessionalOwnReviewList.this);
                            historyRecycler.setLayoutManager(layoutManager);
                            historyRecycler.setHasFixedSize(true);
                            historyRecycler.setAdapter(professionalOwnReviewListAdapter);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void reviewList_goback(View view) {
        ProfessionalOwnReviewList.super.onBackPressed();
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