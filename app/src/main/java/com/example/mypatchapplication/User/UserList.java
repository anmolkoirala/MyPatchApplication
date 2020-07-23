package com.example.mypatchapplication.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mypatchapplication.Common.Professional.ProfessionalList;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.Model.UserHelperClass;
import com.example.mypatchapplication.Helperclass.ProfessionalRVAdapter;
import com.example.mypatchapplication.Helperclass.UserRVAdapter;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    private List<UserHelperClass> userData = new ArrayList<>();;
    private RecyclerView recyclerView;
    private View decorView;
    UserRVAdapter userRVAdapter;
    RelativeLayout progressbar;
    EditText search;

    private SearchView searchView ;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        recyclerView  = findViewById(R.id.recycler_user);
        searchView    = findViewById(R.id.user_search);

        //serching for entire user list by name
        if (searchView != null) {
            SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setQueryHint("Search by Name");

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    userRVAdapter.getFilter().filter(newText);
                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        progressbar  = findViewById(R.id.userlist_progressbar);
        progressbar.setVisibility(View.VISIBLE);
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Users");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        UserHelperClass l = npsnapshot.getValue(UserHelperClass.class);
                        userData.add(l);
                    }
                    progressbar.setVisibility(View.GONE);
                    userRVAdapter=new UserRVAdapter(userData, UserList.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserList.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(userRVAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserList.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goBackProf(View view) {
        UserList.super.onBackPressed();
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
