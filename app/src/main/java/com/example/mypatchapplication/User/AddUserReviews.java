package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.AddReviewsAdapter;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUserReviews extends AppCompatActivity {

    private View decorView;
    TextView descp;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference workRef = database.getReference("HireList");
    String current_ID;
    RecyclerView reviewRecycler;
    private List<HireUsModel> hireArraylist = new ArrayList<>();
    RelativeLayout progressBar;

    String innerid, profname,profId,category,issue,response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        descp          = (TextView) findViewById(R.id.review_title_descp);
        reviewRecycler  = (RecyclerView) findViewById(R.id.recycler_notreviewedlist);
        progressBar     = (RelativeLayout) findViewById(R.id.addreviews_progressbar);


        progressBar.setVisibility(View.VISIBLE);
        descp.setVisibility(View.GONE);

        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String, String> userdetails = sessionHolder.getUserSessionDetails();
        current_ID   = userdetails.get(SessionHolder.KEY_USERID);


        Query reviews  = workRef.child(current_ID).orderByChild("reviewed").equalTo("notreviewed");
        reviews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    descp.setVisibility(View.VISIBLE);
                    descp.setText(R.string.hired_prof_reviews);
                   for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String respStat    = datas.child("responsestatus").getValue().toString();
                       if(respStat.equals("completed")){
                          // descp.setText(R.string.hired_prof_reviews);
                           innerid     = datas.getRef().getKey();
                           profname    = datas.child("professionalname").getValue().toString();
                           profId      = datas.child("professionalID").getValue().toString();
                           category    = datas.child("professionalcategory").getValue().toString();
                           issue       = datas.child("issuedescription").getValue().toString();
                           response    = datas.child("responsemessage").getValue().toString();

                           hireArraylist.add(new HireUsModel(profname,profId,category,issue,response,innerid));
                           AddReviewsAdapter addReviewsAdapter = new AddReviewsAdapter(hireArraylist, AddUserReviews.this);
                           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddUserReviews.this);
                           reviewRecycler.setLayoutManager(layoutManager);
                           reviewRecycler.setHasFixedSize(true);
                           reviewRecycler.setAdapter(addReviewsAdapter);
                       }
                   }

                } else{
                    progressBar.setVisibility(View.GONE);
                    descp.setVisibility(View.VISIBLE);
                    descp.setText(R.string.all_reviewed);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddUserReviews.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void addReview_goback(View view) {
        AddUserReviews.super.onBackPressed();
    }
}