package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.AllReviewsApdater;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.Helperclass.ReviewHistoryAdapter;
import com.example.mypatchapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllUserReviews extends AppCompatActivity {
    private View decorView;
    TextView title,descp;
    RelativeLayout progressBar;
    RecyclerView allUserRecycler;
    String current_ID,current_name;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference historyRef;
    private List<ReviewModel> allUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_reviews);
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

        title           = findViewById(R.id.allreview_title);
        descp           = findViewById(R.id.allreview_descp);
        progressBar     = findViewById(R.id.allreview_progressbar);
        allUserRecycler = findViewById(R.id.recycler_allreview);

        progressBar.setVisibility(View.VISIBLE);
        descp.setVisibility(View.GONE);

        historyRef = database.getReference("Reviews");
        historyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    descp.setVisibility(View.VISIBLE);
                    for (DataSnapshot datas: dataSnapshot.getChildren()){
                        String reviewTexts       = datas.child("reviewText").getValue().toString();
                        String professionalname  = datas.child("professionalname").getValue().toString();
                        String professionalID    = datas.child("professionalID").getValue().toString();
                        String professionalcat   = datas.child("professionalcategory").getValue().toString();
                        String reviewrating      = datas.child("reviewRating").getValue().toString();
                        String userissue         = datas.child("userIssue").getValue().toString();
                        String username          = datas.child("username").getValue().toString();
                        int finarate             = Integer.parseInt(reviewrating);

                        allUserList.add(new ReviewModel(username,userissue,professionalname,professionalID,professionalcat,reviewTexts,finarate));
                        AllReviewsApdater allReviewsApdater = new AllReviewsApdater(allUserList, AllUserReviews.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AllUserReviews.this);
                        allUserRecycler.setLayoutManager(layoutManager);
                        allUserRecycler.setHasFixedSize(true);
                        allUserRecycler.setAdapter(allReviewsApdater);
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

    public void allreviews_goback(View view) {
        AllUserReviews.super.onBackPressed();
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