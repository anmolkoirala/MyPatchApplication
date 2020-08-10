package com.example.mypatchapplication.Common.Professional;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.ProfessionalRequestRVAdapter;
import com.example.mypatchapplication.Helperclass.ProfessionalWorkHistoryRVAdapter;
import com.example.mypatchapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfessionalWorkHistoryList extends AppCompatActivity {

    private View decorView;
    String current_ID,current_name;
    RecyclerView historyRecycler;
    RelativeLayout progressHistory;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allWorkHistoryRef;
    private List<HireUsModel> hireUsModelList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_work_history_list);

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

        historyRecycler = findViewById(R.id.recycler_pro_work_history_list);
        progressHistory = findViewById(R.id.pro_work_history_list_progressbar);

        progressHistory.setVisibility(View.VISIBLE);
        allWorkHistoryRef = database.getReference("HireList");
        allWorkHistoryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    progressHistory.setVisibility(View.GONE);
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {

                        if(datas.child("professionalID").getValue().toString().equals(current_ID) && datas.child("responsestatus").getValue().toString().equals("completed")) {
                            String innerID = datas.getKey();
                            String issusedesc = datas.child("issuedescription").getValue().toString();
                            String professionalname = datas.child("professionalname").getValue().toString();
                            String professionalID = datas.child("professionalID").getValue().toString();
                            String professionalcat = datas.child("professionalcategory").getValue().toString();
                            String useraddress = datas.child("useraddress").getValue().toString();
                            String username = datas.child("username").getValue().toString();
                            String usernumber = datas.child("usernumber").getValue().toString();
                            String userId = datas.child("userID").getValue().toString();
                            String requesteddate = datas.child("requesteddate").getValue().toString();
                            String professionalnumber = datas.child("professionalnumber").getValue().toString();
                            String responsestatus = datas.child("responsestatus").getValue().toString();
                            String responsemessage = datas.child("responsemessage").getValue().toString();
                            String reviewed = datas.child("reviewed").getValue().toString();

                            hireUsModelList.add(new HireUsModel(innerID,username,userId,useraddress,professionalname,
                                    professionalcat,professionalID,usernumber,
                                    professionalnumber,responsestatus,issusedesc,requesteddate,responsemessage,reviewed));
                            ProfessionalWorkHistoryRVAdapter professionalWorkHistoryRVAdapter = new ProfessionalWorkHistoryRVAdapter(hireUsModelList, ProfessionalWorkHistoryList.this);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfessionalWorkHistoryList.this);
                            historyRecycler.setLayoutManager(layoutManager);
                            historyRecycler.setHasFixedSize(true);
                            historyRecycler.setAdapter(professionalWorkHistoryRVAdapter);
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


    public void proworkHistoryList_goback(View view) {
        ProfessionalWorkHistoryList.super.onBackPressed();
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