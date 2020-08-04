package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Common.All_InternalReview;
import com.example.mypatchapplication.Common.InternalReview;
import com.example.mypatchapplication.Common.LoginSignup.ProLogin;
import com.example.mypatchapplication.Common.LoginSignup.ProSignUp;
import com.example.mypatchapplication.Common.LoginSignup.StartUpScreen;
import com.example.mypatchapplication.Common.Professional.ProfessionalList;
import com.example.mypatchapplication.Common.Professional.ProfessionalOnGoingWorkList;
import com.example.mypatchapplication.Common.Professional.ProfessionalOwnReviewList;
import com.example.mypatchapplication.Common.Professional.ProfessionalRequestList;
import com.example.mypatchapplication.Common.Professional.ProfessionalWorkHistoryList;
import com.example.mypatchapplication.Common.Search.SearchCategoryActivity;
import com.example.mypatchapplication.Common.YourLocation;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.DashboardNotification;
import com.example.mypatchapplication.Helperclass.Homeadapter.CategoriesHelperClass;
import com.example.mypatchapplication.Helperclass.Homeadapter.CategoryAdapter;
import com.example.mypatchapplication.Helperclass.Homeadapter.FeaturedAdapater;
import com.example.mypatchapplication.Helperclass.Homeadapter.FeaturedRecHelperClass;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewAdapter;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewHelperCLass;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.InternalReviewModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.ProfessionalRVAdapter;
import com.example.mypatchapplication.Helperclass.ProfessionalWorkHistoryRVAdapter;
import com.example.mypatchapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private View decorView;
    RecyclerView featuredRecycler, reviewRecycler, categoryRecycler;
    RecyclerView.Adapter adapterFeatured, adapterReview, adapterCategory;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    //for side drawer navitaion menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon, loginSignUp;
    static final float END_SCALE = 0.7f;
    LinearLayout dashboardContent;
    String usertype;
    TextView tvcurentuser;

    String current_ID,current_name,current_address;
    final FirebaseDatabase hdatabase = FirebaseDatabase.getInstance();
    DatabaseReference allWorkHistoryRef,adminresponseRef;
    int count,requestcount,admincount;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaydate;
    Marker addressMaker;

    NotificationManager mNotificationManager;

    NotificationManagerCompat notificationManagerCompat;

    GoogleMap mMap;
    View mMapView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    double userlat, userlon;
    private LatLng userLocation;
    ArrayList<FeaturedRecHelperClass> featuredProfessionals = new ArrayList<>();
    ArrayList<ReviewHelperCLass> clientsReviews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        menuIcon         = findViewById(R.id.menu_icon);
        dashboardContent = findViewById(R.id.dashboard_content);
        loginSignUp      = findViewById(R.id.login_signup);

        tvcurentuser = findViewById(R.id.currentusername);

        featuredRecycler = findViewById(R.id.featured_recycler);
        reviewRecycler   = findViewById(R.id.reviews_recycler);
        categoryRecycler = findViewById(R.id.category_recycler);

        drawerLayout     = findViewById(R.id.drawer_navigation);
        navigationView   = findViewById(R.id.navigaton_menu_view);



        notificationManagerCompat= NotificationManagerCompat.from(this);
        DashboardNotification channel = new DashboardNotification(this);
        channel.createNotificationChannel();



        //getting value from the session
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String,String> userSessionDetails = sessionHolder.getUserSessionDetails();

        String sessionFullname = userSessionDetails.get(SessionHolder.KEY_FULLNAME);
        usertype               = userSessionDetails.get(SessionHolder.KEY_USERTYPE);
        current_ID = userSessionDetails.get(SessionHolder.KEY_USERID);
        current_name = userSessionDetails.get(SessionHolder.KEY_FULLNAME);
        current_address = userSessionDetails.get(SessionHolder.KEY_ADDRESS);

        tvcurentuser.setText("Welcome, "+current_name);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.currentlocation);

        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMapView = mapFragment.getView();

        if(usertype != null && usertype.equals("professional")){

            DatabaseReference currentuserDetail = FirebaseDatabase.getInstance().getReference().child("Professionals").child(current_ID);
            currentuserDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userlat = dataSnapshot.child("latitude").getValue(Double.class);
                    userlon = dataSnapshot.child("longitude").getValue(Double.class);
                    userLocation = new LatLng(userlat, userlon);

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(userLocation.latitude,
                            userLocation.longitude));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                    addressMaker= mMap.addMarker(new MarkerOptions()
                            .title(current_address)
                            .snippet("Your Set Location")
                            .position(userLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    addressMaker.showInfoWindow();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            DatabaseReference currentuserDetail = FirebaseDatabase.getInstance().getReference().child("Users").child(current_ID);
            currentuserDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userlat = dataSnapshot.child("latitude").getValue(Double.class);
                    userlon = dataSnapshot.child("longitude").getValue(Double.class);
                    userLocation = new LatLng(userlat, userlon);

                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(userLocation.latitude,
                            userLocation.longitude));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);

                    addressMaker= mMap.addMarker(new MarkerOptions()
                            .title(current_address)
                            .snippet("Your Set Location")
                            .position(userLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    addressMaker.showInfoWindow();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("d/M/yyyy");
        todaydate = dateFormat.format(calendar.getTime());
        Log.d("today",todaydate+"");



        if(usertype != null && usertype.equals("customer")){
            adminresponseRef = hdatabase.getReference("HireList");
            adminresponseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {

                            if(datas.child("userID").getValue().toString().equals(current_ID) && datas.child("responsestatus").getValue().toString().equals("adminresponse")) {



                                admincount=admincount+1;





                            }
                        }

                        if(admincount!=0) {
                            Intent notificationIntent = new Intent(UserDashboard.this, MyWorks.class);
                            notificationIntent.putExtra(getString(R.string.address), true);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(UserDashboard.this);
                            stackBuilder.addParentStack(UserDashboard.this);
                            stackBuilder.addNextIntent(notificationIntent);
                            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(1,
                                    PendingIntent.FLAG_UPDATE_CURRENT);


                            Notification notification = new NotificationCompat
                                    .Builder(UserDashboard.this, DashboardNotification.Response_Channel)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("Hired Works")
                                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                                    .setContentText("You have " + admincount + " responses from professionals. Click here !")
                                    .setContentIntent(notificationPendingIntent)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManagerCompat.notify(1, notification);
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

        //check the usertype inorder to change the menu in dashboard.
        if(usertype != null && usertype.equals("professional")){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.pro_menu);


            allWorkHistoryRef = hdatabase.getReference("HireList");
            allWorkHistoryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {

                            if(datas.child("professionalID").getValue().toString().equals(current_ID) && datas.child("responsestatus").getValue().toString().equals("adminresponse")) {

                                String requesteddate = datas.child("requesteddate").getValue().toString();
                                if(todaydate.equals(requesteddate)){
                                    Log.d("today",requesteddate+" mm");

                                    count=count+1;

                                    Log.d("today",count+" mm");


                                }

                            }
                        }

                        if(count!=0) {
                            Intent notificationIntent = new Intent(UserDashboard.this, ProfessionalOnGoingWorkList.class);
                            notificationIntent.putExtra(getString(R.string.address), true);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(UserDashboard.this);
                            stackBuilder.addParentStack(UserDashboard.this);
                            stackBuilder.addNextIntent(notificationIntent);
                            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(1,
                                    PendingIntent.FLAG_UPDATE_CURRENT);


                            Notification notification = new NotificationCompat
                                    .Builder(UserDashboard.this, DashboardNotification.Response_Channel)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("On Going Task")
                                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                                    .setContentText("You have " + count + " tasks set for today ")
                                    .setContentIntent(notificationPendingIntent)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManagerCompat.notify(1, notification);
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



            allWorkHistoryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {

                            if(datas.child("professionalID").getValue().toString().equals(current_ID) && datas.child("responsestatus").getValue().toString().equals("requested")) {

                                requestcount=requestcount+1;
                            }
                        }

                        if(requestcount!=0) {
                            Intent notificationIntent = new Intent(UserDashboard.this, ProfessionalRequestList.class);
//                            notificationIntent.putExtra(getString(R.string.address), true);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(UserDashboard.this);
                            stackBuilder.addParentStack(UserDashboard.this);
                            stackBuilder.addNextIntent(notificationIntent);
                            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(1,
                                    PendingIntent.FLAG_UPDATE_CURRENT);


                            Notification notification = new NotificationCompat
                                    .Builder(UserDashboard.this, DashboardNotification.Response_Channel)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("Work Request")
                                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                                    .setContentText("You have " + requestcount + " work request. Send a response!")
                                    .setContentIntent(notificationPendingIntent)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();
                            notificationManagerCompat.notify(1, notification);
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




        navigationDrawer();
        featuredRecycler();
        reviewRecycler();
        categoryRecycler();
    }

//    public void loginSignupScreen(View view){
//        Intent intent = new Intent(UserDashboard.this, StartUpScreen.class);
//        startActivity(intent);
//    }

    //navigation drawer functions
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        //removing default navigation and status bar shadow from side drawer navigation
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        //drawerLayout.setScrimColor(getResources().getColor(R.color.home_background));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //scale the view as per the current offset
                final float diffScaledOffSet = slideOffset * (1 - END_SCALE);
                final float offSetScale = 1 - diffScaledOffSet;
                dashboardContent.setScaleX(offSetScale);
                dashboardContent.setScaleY(offSetScale);
                //translate the view as per scaled width
                final float xOffSet = drawerView.getWidth() * slideOffset;
                final float xOffSetDiff = dashboardContent.getWidth() * diffScaledOffSet / 2;
                final float xTranslation = xOffSet - xOffSetDiff;
                dashboardContent.setTranslationX(xTranslation);
            }

        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_search) {
            Intent intent = new Intent(getApplicationContext(), SearchCategoryActivity.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_prosearch) {
            Intent intent = new Intent(getApplicationContext(), UserList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_allprofessionals) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_allcategories) {
            Intent intent = new Intent(getApplicationContext(), CategoryList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_prohome) {
            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_proprofile) {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_addreview) {
            Intent intent = new Intent(getApplicationContext(), AddUserReviews.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_myreview) {
            Intent intent = new Intent(getApplicationContext(), UserReviewHistory.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_promyreview) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalOwnReviewList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_allreview) {
            Intent intent = new Intent(getApplicationContext(), AllUserReviews.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_works) {
            Intent intent = new Intent(getApplicationContext(), MyWorks.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_yourlocation) {
            Intent intent = new Intent(getApplicationContext(), YourLocation.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_professional_nearyou) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalNearYou.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_prolocation) {
            Intent intent = new Intent(getApplicationContext(), YourLocation.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_pro_requestlist) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalRequestList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }   else if (item.getItemId() == R.id.nav_pro_ongoing_work) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalOnGoingWorkList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_pro_workhistory) {
            Intent intent = new Intent(getApplicationContext(), ProfessionalWorkHistoryList.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_rateus) {
            Intent intent = new Intent(getApplicationContext(), InternalReview.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_pro_rateus) {
            Intent intent = new Intent(getApplicationContext(), InternalReview.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.nav_logout) {
            SessionHolder sessionHolder = new SessionHolder(UserDashboard.this);
            sessionHolder.logoutSession();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), ProLogin.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        } else if(item.getItemId() == R.id.nav_pro_logout){
            SessionHolder sessionHolder = new SessionHolder(UserDashboard.this);
            sessionHolder.logoutSession();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), ProLogin.class);
            intent.putExtra("usertype", usertype);
            startActivity(intent);
        }
        return true;
    }
    //end of navigation drawer functions

    //dashboard recycler view functions
    private void categoryRecycler() {
        categoryColors();
        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<CategoriesHelperClass> serviceCategory = new ArrayList<>();
        serviceCategory.add(new CategoriesHelperClass(R.drawable.cleaners_cat, "Cleaners", gradient1));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.roofers_cat, "Roofers", gradient2));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.pests_cat, "Pest Control", gradient3));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.poo_clearnes_cat, "Tank Cleaners", gradient4));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.plumbers_cat, "Plumbers", gradient1));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.painters_cat, "Painting", gradient2));
        serviceCategory.add(new CategoriesHelperClass(R.drawable.electrician_cat, "Electrician", gradient3));

        adapterCategory = new CategoryAdapter(serviceCategory);
        categoryRecycler.setAdapter(adapterCategory);
    }

    private void categoryColors() {
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});
    }

    private void reviewRecycler() {

        final DatabaseReference app_review= FirebaseDatabase.getInstance().getReference("ApplicationReview");
        app_review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {


                                clientsReviews.add(new ReviewHelperCLass(npsnapshot.child("userID").getValue(String.class), npsnapshot.child("fullname").getValue(String.class),
                                        npsnapshot.child("reivewdescription").getValue(String.class), npsnapshot.child("apprating").getValue(int.class)));
//                    clientsReviews.add(new ReviewHelperCLass(R.drawable.dummy3, "Sirish Upreti", "Boudha, Chabel", "Has popular home furnishing services including painting, remodelling, fixing tiles etc."));
                    }
                    reviewRecycler.setLayoutManager(new LinearLayoutManager(UserDashboard.this, LinearLayoutManager.HORIZONTAL, false));
                    reviewRecycler.setHasFixedSize(true);

                    adapterReview = new ReviewAdapter(clientsReviews,UserDashboard.this);
                    reviewRecycler.setAdapter(adapterReview);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserDashboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void featuredRecycler() {

        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Professionals");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        ProfessionalModel l = npsnapshot.getValue(ProfessionalModel.class);
                        if(4 <= l.getAvgrating()){
                            featuredProfessionals.add(new FeaturedRecHelperClass(l.getId(),l.getFullname(),l.getBio(),l.getAvgrating()));

                        }
                    }
//                 featuredProfessionals.add(new FeaturedRecHelperClass(R.drawable.dummy, "Sikhar subedi", "Working as skilled electrician for ten years. Handy with variety of works."));

                    featuredRecycler.setLayoutManager(new LinearLayoutManager(UserDashboard.this, LinearLayoutManager.HORIZONTAL, false));
                    featuredRecycler.setHasFixedSize(true);
                    adapterFeatured = new FeaturedAdapater(featuredProfessionals,UserDashboard.this);
                    featuredRecycler.setAdapter(adapterFeatured);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserDashboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void categoryPlumber(View view){
        if(usertype.equals("Customer")) {
            Intent intent_plumber = new Intent(this, SearchCategoryActivity.class);
            intent_plumber.putExtra("Category", "Plumbers");
            startActivity(intent_plumber);
        }
    }

    public void categoryElectrican(View view){
        if(usertype.equals("Customer")) {
            Intent intent_electric = new Intent(this, SearchCategoryActivity.class);
            intent_electric.putExtra("Category", "Electrician");
            startActivity(intent_electric);
        }
    }

    public void categoryPainter(View view){
        if(usertype.equals("Customer")) {
            Intent intent_painter = new Intent(this, SearchCategoryActivity.class);
            intent_painter.putExtra("Category", "Painters");
            startActivity(intent_painter);
        }
    }

    public void categoryCleaner(View view){
        if(usertype.equals("Customer")) {
            Intent intent_clean = new Intent(this, SearchCategoryActivity.class);
            intent_clean.putExtra("Category", "Cleaners");
            startActivity(intent_clean);
        }
    }

    //end dashboard of recycler view functions
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
    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, UserDashboard.this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    //end of removing status bar and default navigation at first
    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void callsearchProfessional(View view) {
        Intent intents = new Intent(getApplicationContext(), ProfessionalList.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(loginSignUp, "transition_innerprofessionals");

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(intents,activityOptions.toBundle());
    }

    public void callfullCategory(View view) {
        Intent catIntent = new Intent(getApplicationContext(), CategoryList.class);
        startActivity(catIntent);
    }

    public void callLocationChanger(View view) {
        Intent addIntent = new Intent(getApplicationContext(), YourLocation.class);
        addIntent.putExtra("usertype", usertype);
        startActivity(addIntent);
    }


    public void viewAllApplicationReview(View view) {
        Intent viewall_App_review = new Intent(getApplicationContext(), All_InternalReview.class);
//        viewall_App_review.putExtra("usertype", usertype);
        startActivity(viewall_App_review);
    }
}