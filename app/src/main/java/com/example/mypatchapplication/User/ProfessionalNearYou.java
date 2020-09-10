package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.Helperclass.ProfessionalNearYouAdapter;
import com.example.mypatchapplication.Helperclass.ProfessionalRVAdapter;
import com.example.mypatchapplication.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfessionalNearYou extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleMap mMap;
    View mMapView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private double radiusProfessionalRequest = 0.7;
    List<String> professionalNearby = new ArrayList<>();
    public static List<ProfessionalModel> allprofessionalNearby = new ArrayList<>();
    private String professionalKey = "";
    private LatLng userLocation;
    private Geocoder geocoders;
    private List<Address> addresses;
    private String address, city, state, substate, country, postalCode, feature, subcity, fare, subfare, premise;
    private Marker mProfessionalMarker;
    String userid, useraddress;
    double userlat, userlon;
    Button searchNear;
    int counter = 1;
    private View decorView;

    private RecyclerView recyclerView;
    private ProfessionalNearYouAdapter professionalNearYouAdapter;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_near_you);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        searchNear = findViewById(R.id.btn_pro_near);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.near_you);

        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMapView = mapFragment.getView();


        //getting value from the session
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String, String> userSessionDetails = sessionHolder.getUserSessionDetails();

        userid = userSessionDetails.get(SessionHolder.KEY_USERID);
        useraddress = userSessionDetails.get(SessionHolder.KEY_ADDRESS);
        recyclerView = findViewById(R.id.recycler_professional_near_you);

        DatabaseReference currentuserDetail = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
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

                mMap.addMarker(new MarkerOptions()
                        .title(useraddress)
                        .snippet("Your Set Address")
                        .position(userLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        searchNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                if (counter == 1) {
                    counter++;
                    getNearestProfessional(userlat, userlon);
                } else {
                    if (professionalNearby.size() == 0) {
                        Toast.makeText(ProfessionalNearYou.this, "No Professional Near You", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfessionalNearYou.this, "Nearest Number Of Professionals: " + String.valueOf(professionalNearby.size()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

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

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private void getNearestProfessional(double userl, double userlo) {
        DatabaseReference nearestProfessional = FirebaseDatabase.getInstance().getReference().child("ProfessionalAddress");
        GeoFire geoFire = new GeoFire(nearestProfessional);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(userl, userlo), radiusProfessionalRequest);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                professionalKey = key;
                professionalNearby.add(key);
                Toast.makeText(ProfessionalNearYou.this, "Nearest Number Of Professionals: " + String.valueOf(professionalNearby.size()), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onKeyExited(String key) {
                professionalNearby.remove(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }


            @Override
            public void onGeoQueryReady() {

                for (int i = 0; i < professionalNearby.size(); i++) {
                    DatabaseReference professionalDetails = FirebaseDatabase.getInstance().getReference().child("Professionals").child(professionalNearby.get(i));
                    professionalDetails.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot sdataSnapshot) {

                            if (sdataSnapshot.exists()) {
                                double prolat = sdataSnapshot.child("latitude").getValue(Double.class);
                                double prolon = sdataSnapshot.child("longitude").getValue(Double.class);
                                String proaddress = sdataSnapshot.child("address").getValue(String.class);
                                String profullname = sdataSnapshot.child("fullname").getValue(String.class);
                                LatLng professionalLatLng = new LatLng(prolat, prolon);

                                ProfessionalModel allpro = sdataSnapshot.getValue(ProfessionalModel.class);
                                allprofessionalNearby.add(allpro);
                                professionalNearYouAdapter = new ProfessionalNearYouAdapter(allprofessionalNearby, ProfessionalNearYou.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfessionalNearYou.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(professionalNearYouAdapter);

                                mProfessionalMarker = mMap.addMarker(new MarkerOptions().position(professionalLatLng)
                                        .title(proaddress)
                                        .snippet("Nearest Professional Name: " + profullname));
                                mProfessionalMarker.showInfoWindow();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ProfessionalNearYou.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    public void professional_goback(View view) {
        allprofessionalNearby.clear();
        ProfessionalNearYou.super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        allprofessionalNearby.clear();
        super.onBackPressed();
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