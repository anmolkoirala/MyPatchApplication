package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.ListViewAdapter;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.MyworksAdapter;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyWorks extends AppCompatActivity {
    private View decorView;
    ExpandableListView myWorkList;
    ImageView workgoBack;
    RelativeLayout progressbar;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference workRef;
    String current_ID;
    TextView titleDescp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_works);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        myWorkList   = findViewById(R.id.myworks_listview);
        workgoBack   = findViewById(R.id.myworks_back);
        progressbar  = findViewById(R.id.myworks_progressbar);
        titleDescp   = findViewById(R.id.myworks_heading_descp);

        titleDescp.setVisibility(View.GONE);

        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String, String> userdetails = sessionHolder.getUserSessionDetails();
        current_ID   = userdetails.get(SessionHolder.KEY_USERID);

        progressbar.setVisibility(View.VISIBLE);
        workRef = database.getReference("HireList").child(current_ID);

        settingAdapter();
        //checks if the group has a child or not and displays the message if child is empty
        myWorkList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int childCount = parent.getExpandableListAdapter().getChildrenCount(groupPosition);
                if (childCount == 0) {
                    Toast.makeText(getApplicationContext(), "Your work will be shifted here when Professional addresses them!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        workgoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWorks.super.onBackPressed();
            }
        });
    }

    public void settingAdapter(){
        //creating the group
        final List<String> workGroup = new ArrayList<>();
        workGroup.add("Requested Work");
        workGroup.add("UnderProcess Work");
        workGroup.add("Finished Work");

        final List<HireUsModel> requested    = new ArrayList<>();
        final List<HireUsModel> underprocess = new ArrayList<>();
        final List<HireUsModel> finished     = new ArrayList<>();
        workRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressbar.setVisibility(View.GONE);
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String innerid           = datas.getRef().getKey();
                        String responsestatus    = datas.child("responsestatus").getValue().toString();
                        String professionalname  = datas.child("professionalname").getValue().toString();
                        String professionalid    = datas.child("professionalID").getValue().toString();
                        String issuedescription  = datas.child("issuedescription").getValue().toString();
                        String requesteddate     = datas.child("requesteddate").getValue().toString();
                        String responsemessage   = datas.child("responsemessage").getValue().toString();
                        String reviewedResp      = datas.child("reviewed").getValue().toString();
                        String responsecat       = datas.child("professionalcategory").getValue().toString();
                        if(responsestatus.equals("requested")){
                            requested.add(new HireUsModel(innerid,current_ID,professionalname,professionalid,responsecat, "requested",issuedescription,requesteddate,reviewedResp));
                        }else if(responsestatus.equals("adminresponse")){
                            underprocess.add(new HireUsModel(innerid,current_ID,professionalname,professionalid,responsecat, "Processing",responsemessage,requesteddate,reviewedResp));
                        }else{
                            finished.add(new HireUsModel(innerid,current_ID,professionalname,professionalid,responsecat, "Completed",issuedescription,requesteddate,reviewedResp));
                        }
                    }
                    HashMap<String, List<HireUsModel>> workItemsGroup = new HashMap<>();
                    workItemsGroup.put(workGroup.get(0), requested);
                    workItemsGroup.put(workGroup.get(1), underprocess);
                    workItemsGroup.put(workGroup.get(2), finished);

                    MyworksAdapter myworksAdapter = new MyworksAdapter(MyWorks.this, workGroup, workItemsGroup);
                    myWorkList.setAdapter(myworksAdapter);
                } else{
                    progressbar.setVisibility(View.GONE);
                    titleDescp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyWorks.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyWorks.super.onBackPressed();
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