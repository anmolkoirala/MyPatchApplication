package com.example.mypatchapplication.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypatchapplication.Common.InternetConnection;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProAddressSignup extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerDragListener {

    private View decorView;
    ImageView goBackbtn;
    Button nextBtn, loginBtn;
    TextView titletext, pagenumaddress;
    GoogleMap mMap;
    View mMapView;
    String address;
    double lat,lng;
    private LocationRequest mLocationRequest;
//    TextView addresstextview;
    private GoogleApiClient mGoogleApiClient;

    private boolean needsInit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_address_signup);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.address_googlemap);

        if (savedInstanceState == null) {
            needsInit=true;
        }

        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMapView = mapFragment.getView();


        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        goBackbtn       = findViewById(R.id.address_goback);
        nextBtn         = findViewById(R.id.signup_addressnext);
        loginBtn        = findViewById(R.id.signup_loginaddress);
        titletext       = findViewById(R.id.signup_titleaddress);
        pagenumaddress  = findViewById(R.id.signup_addressnum);
        goBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pagenumaddress.setText("3 / 4");
    }


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



    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(needsInit) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
                return;
            }
            mMap.setMyLocationEnabled(true);
            LatLng defaultadd = new LatLng(27.7172, 85.3240);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(27.7172,
                    85.3240));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            mMap.addMarker(new MarkerOptions()
                    .position(defaultadd)
                     .draggable(true));
            mMap.setOnMarkerDragListener(this);

        }
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

        @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position=marker.getPosition();

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position=marker.getPosition();

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position=marker.getPosition();
        CameraUpdate centr = CameraUpdateFactory.newLatLng(position);
        mMap.moveCamera(centr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
                lat       = position.latitude;
                lng       = position.longitude;
                address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String subcity = addresses.get(0).getSubLocality();
                if(subcity!=null){
                    Toast.makeText(ProAddressSignup.this, "Address: " +
                            address, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ProAddressSignup.this, "Address: " +
                             address, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void nextAddressScreen(View view) {
        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);

        if(!validateAddress()){
            return;
        }
        final String fullname  = getIntent().getStringExtra("fullname");
        final String username  = getIntent().getStringExtra("username");
        final String email     = getIntent().getStringExtra("email");
        final String password  = getIntent().getStringExtra("password");
        final String gender    = getIntent().getStringExtra("gender");
        final String dob       = getIntent().getStringExtra("dob");
        final String usertype  = getIntent().getStringExtra("usertype");
        final String category  = getIntent().getStringExtra("category");

        Intent intent = new Intent(getApplicationContext(),ProSignupThird.class);
        //transition here
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View,String>(goBackbtn,"transition_backarrow_btn");
        pairs[1] = new Pair<View,String>(nextBtn,"transition_next_button");
        pairs[2] = new Pair<View,String>(loginBtn,"transition_login_button");
        pairs[3] = new Pair<View,String>(titletext,"transition_title_text");
        pairs[4] = new Pair<View,String>(pagenumaddress,"transition_title_pagenum");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProAddressSignup.this,pairs);
        intent.putExtra("fullname", fullname);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("gender", gender);
        intent.putExtra("dob", dob);
        intent.putExtra("address", address);
        intent.putExtra("lat", lat);
        intent.putExtra("long", lng);
        intent.putExtra("usertype", usertype);
        intent.putExtra("category", category);
        startActivity(intent,options.toBundle());
    }

    private boolean validateAddress(){
        if(address != null){
            return true;
        }else {
            Toast.makeText(this, "Please select an address to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}