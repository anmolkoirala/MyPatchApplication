package com.example.mypatchapplication.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mypatchapplication.Common.Professional.ProfessionalList;
import com.example.mypatchapplication.Helperclass.ApplicationReviewRVAdapter;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewAdapter;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewHelperCLass;
import com.example.mypatchapplication.Helperclass.Model.InternalReviewModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.ProfessionalRVAdapter;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class All_InternalReview extends AppCompatActivity {

    private View decorView;
    String userid, userType;
    private ArrayList<ReviewHelperCLass> appReviewData = new ArrayList<>();;
    RecyclerView.Adapter  adapterReview, applicationadapterReview;
    private RecyclerView recyclerReview;
    ApplicationReviewRVAdapter applicationReviewRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__internal_review);

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
        recyclerReview  = findViewById(R.id.recycler_all_application_review);


        DatabaseReference appreview_Ref= FirebaseDatabase.getInstance().getReference("ApplicationReview");
        appreview_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        appReviewData.add(new ReviewHelperCLass(snapshot.child("userID").getValue(String.class), snapshot.child("fullname").getValue(String.class),
                                snapshot.child("reivewdescription").getValue(String.class), snapshot.child("apprating").getValue(int.class)));
//                    clientsReviews.add(new ReviewHelperCLass(R.drawable.dummy3, "Sirish Upreti", "Boudha, Chabel", "Has popular home furnishing services including painting, remodelling, fixing tiles etc."));
                    }
                    recyclerReview.setLayoutManager(new LinearLayoutManager(All_InternalReview.this, LinearLayoutManager.VERTICAL, false));
                    recyclerReview.setHasFixedSize(true);

                    applicationadapterReview = new ApplicationReviewRVAdapter(appReviewData,All_InternalReview.this);
                    recyclerReview.setAdapter(applicationadapterReview);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void goBackDashboard(View view) {
        All_InternalReview.super.onBackPressed();
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