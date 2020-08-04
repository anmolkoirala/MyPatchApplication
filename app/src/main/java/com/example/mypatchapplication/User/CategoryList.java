package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mypatchapplication.Helperclass.ListViewAdapter;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CategoryList extends AppCompatActivity {
    private View decorView;
    ExpandableListView categoryWorkerList;
    ImageView goBack;
    RelativeLayout progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        categoryWorkerList = findViewById(R.id.category_prof_listview);
        goBack             = findViewById(R.id.btn_activity_back);
        progressbar        = findViewById(R.id.category_progressbar);

        progressbar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profRef = database.getReference("Professionals");

        // Create the groups
        profRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final List<String> categoryGroups = new ArrayList<>();
                    categoryGroups.add("Cleaners");
                    categoryGroups.add("Electrician");
                    categoryGroups.add("Plumbers");
                    categoryGroups.add("Painters");
                    categoryGroups.add("Roofers");
                    categoryGroups.add("Pest Control");
                    categoryGroups.add("Tank Cleaners");
                    progressbar.setVisibility(View.GONE);

                    final List<ProfessionalModel> cleaners = new ArrayList<>();
                    List<ProfessionalModel> electrician = new ArrayList<>();
                    List<ProfessionalModel> plumbers = new ArrayList<>();
                    List<ProfessionalModel> painters = new ArrayList<>();
                    List<ProfessionalModel> roofers = new ArrayList<>();
                    List<ProfessionalModel> pestcontrol = new ArrayList<>();
                    List<ProfessionalModel> tankcleaners = new ArrayList<>();

                    String fullname;
                    String address;
                    String phone;
                    int avgrating;

                    // Create items of each group
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String category = datas.child("category").getValue().toString();

                        if(category.equals("Cleaners")){
                            String idone  = datas.child("id").getValue().toString();
                            fullname      = datas.child("fullname").getValue().toString();
                            address       = datas.child("address").getValue().toString();
                            phone         = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            cleaners.add(new ProfessionalModel(idone,fullname,category, address,phone,avgrating));
                        } else if(category.equals("Electrician")){
                            String idtwo   = datas.child("id").getValue().toString();
                            fullname       = datas.child("fullname").getValue().toString();
                            address        = datas.child("address").getValue().toString();
                            phone          = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            electrician.add(new ProfessionalModel(idtwo,fullname,category, address,phone,avgrating));
                        } else if(category.equals("Plumbers")){
                            String idthree        = datas.child("id").getValue().toString();
                            fullname = datas.child("fullname").getValue().toString();
                            address  = datas.child("address").getValue().toString();
                            phone    = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            plumbers.add(new ProfessionalModel(idthree,fullname,category, address,phone,avgrating));
                        } else if(category.equals("Painters")){
                            String idfour        = datas.child("id").getValue().toString();
                            fullname = datas.child("fullname").getValue().toString();
                            address  = datas.child("address").getValue().toString();
                            phone    = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            painters.add(new ProfessionalModel(idfour,fullname,category, address,phone,avgrating));
                        } else if(category.equals("Roofers")){
                            String idfive        = datas.child("id").getValue().toString();
                            fullname = datas.child("fullname").getValue().toString();
                            address  = datas.child("address").getValue().toString();
                            phone    = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            roofers.add(new ProfessionalModel(idfive,fullname,category, address,phone,avgrating));
                        } else if(category.equals("Pest Control")){
                            String idsix       = datas.child("id").getValue().toString();
                            fullname = datas.child("fullname").getValue().toString();
                            address  = datas.child("address").getValue().toString();
                            phone    = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            pestcontrol.add(new ProfessionalModel(idsix,fullname,category, address,phone,avgrating));
                        }
                        else if(category.equals("Tank Cleaners")){
                            String idsev       = datas.child("id").getValue().toString();
                            fullname = datas.child("fullname").getValue().toString();
                            address  = datas.child("address").getValue().toString();
                            phone    = datas.child("phonenumber").getValue().toString();
                            avgrating      = datas.child("avgrating").getValue(int.class);
                            tankcleaners.add(new ProfessionalModel(idsev,fullname,category, address,phone,avgrating));
                        }

                    }
                    // Create the relationship of groups and your items
                    final HashMap<String, List<ProfessionalModel>> categoryItemsGroup = new HashMap<>();
                    categoryItemsGroup.put(categoryGroups.get(0), cleaners);
                    categoryItemsGroup.put(categoryGroups.get(1), electrician);
                    categoryItemsGroup.put(categoryGroups.get(2), plumbers);
                    categoryItemsGroup.put(categoryGroups.get(3), painters);
                    categoryItemsGroup.put(categoryGroups.get(4), roofers);
                    categoryItemsGroup.put(categoryGroups.get(5), pestcontrol);
                    categoryItemsGroup.put(categoryGroups.get(6), tankcleaners);

                    // Create an adapter (BaseExpandableListAdapter) with the data above
                    ListViewAdapter listViewAdapter = new ListViewAdapter(CategoryList.this, categoryGroups, categoryItemsGroup);
                    // defines the ExpandableListView adapter
                    categoryWorkerList.setAdapter(listViewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //checks if the group has a child or not and displays the message if child is empty
        categoryWorkerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int childCount = parent.getExpandableListAdapter().getChildrenCount(groupPosition);
                if (childCount == 0) {
                    Toast.makeText(getApplicationContext(), "There is no Professional assigned to this category yet!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CategoryList.super.onBackPressed();
            }
        });

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