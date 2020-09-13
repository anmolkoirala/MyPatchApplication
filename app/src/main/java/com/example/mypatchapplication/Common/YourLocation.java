package com.example.mypatchapplication.Common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserProfile;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class YourLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerDragListener  {

    private View decorView;
    TextInputLayout location_layout;
    double userlat,userlon;
    String checklat ,checklon;

    private GoogleMap mMap;
    private View mMapView;
    private double lat,lng;
    private String newaddress;
    private LocationRequest mLocationRequest;
    //    TextView addresstextview;
    private GoogleApiClient mGoogleApiClient;
    private boolean needsInit=false;
    String userid, userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.yourlocation_update_googlemap);

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


        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        location_layout = findViewById(R.id.yourlocation_address);


        //getting needed values form the session
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
        userid           = userdetails.get(SessionHolder.KEY_USERID);
        String scontact  = userdetails.get(SessionHolder.KEY_PHONENUMBER);
        userType         = getIntent().getStringExtra("usertype");



        if(userType != null && userType.equals("customer")){
            final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(scontact);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        String address = dataSnapshot.child(userid).child("address").getValue(String.class);

                        userlat = dataSnapshot.child(userid).child("latitude").getValue(Double.class);
                        userlon= dataSnapshot.child(userid).child("longitude").getValue(Double.class);

                        LatLng lokasi = new LatLng(userlat,userlon);
                        mMap.addMarker(new MarkerOptions()
                                .position(lokasi)
                                .draggable(true));
                        mMap.setOnMarkerDragListener(YourLocation.this);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



                        location_layout.getEditText().setText(address);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(YourLocation.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            final Query checkProfUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(scontact);
            checkProfUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        String address = dataSnapshot.child(userid).child("address").getValue(String.class);


                        userlat = dataSnapshot.child(userid).child("latitude").getValue(Double.class);
                        userlon= dataSnapshot.child(userid).child("longitude").getValue(Double.class);

                        LatLng lokasi = new LatLng(userlat,userlon);
                        mMap.addMarker(new MarkerOptions()
                                .position(lokasi)
                                .draggable(true));
                        mMap.setOnMarkerDragListener(YourLocation.this);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



                        location_layout.getEditText().setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(YourLocation.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    public void yourlocation_goback(View view) {
        YourLocation.super.onBackPressed();
    }

    public void updateLocationData(View view) {
        //update location button


        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);


        String uaddress = location_layout.getEditText().getText().toString();

        if(userType != null && userType.equals("customer")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
              reference.child(userid).child("address").setValue(uaddress);
            if(checklon !=null) {
                reference.child(userid).child("latitude").setValue(lat);
                reference.child(userid).child("longitude").setValue(lng);

                LatLng userlatLng = new LatLng(lat,lng);
                DatabaseReference locationReference = FirebaseDatabase.getInstance().getReference().child("UserAddress");
                GeoFire geoFire = new GeoFire(locationReference);
                geoFire.setLocation(userid, new GeoLocation(userlatLng.latitude, userlatLng.longitude));
            }
        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Professionals");
                  reference.child(userid).child("address").setValue(uaddress);
            if(checklon !=null) {
                reference.child(userid).child("latitude").setValue(lat);
                reference.child(userid).child("longitude").setValue(lng);

                LatLng prolatLng = new LatLng(lat,lng);
                DatabaseReference locationReference = FirebaseDatabase.getInstance().getReference().child("ProfessionalAddress");
                GeoFire geoFire = new GeoFire(locationReference);
                geoFire.setLocation(userid, new GeoLocation(prolatLng.latitude, prolatLng.longitude));
            }
        }

        SessionHolder sessionHolder = new SessionHolder(this);
        sessionHolder.addressUpdateSession(uaddress);

        Toast.makeText(this, "Your location is updated", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                checklat = Double.toString(lat);
                checklon = Double.toString(lng);
                newaddress = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String subcity = addresses.get(0).getSubLocality();
                if(subcity!=null){
                    Toast.makeText(YourLocation.this, "Address: " +
                            newaddress, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(YourLocation.this, "Address: " +
                            newaddress, Toast.LENGTH_LONG).show();
                }
                location_layout.getEditText().setText(newaddress);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }
}