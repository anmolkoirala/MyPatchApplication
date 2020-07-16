package com.example.mypatchapplication.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Common.InternetConnection;
import com.example.mypatchapplication.Common.LoginSignup.ProSignupSecond;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity  {

    private View decorView;
    TextView sessionname, sessionmail, sessioncontact, sessiongender;
    CircleImageView profile_image;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference, profileRef;
    FirebaseUser user;
    String userid, userType,profCategory;
    TextInputLayout updateFullname, updateUsername, updateEmail, updateAddress, updateBio;
    Spinner spinner;
    List<String> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        decorView         = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        storage          = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userType         = getIntent().getStringExtra("usertype");
        sessionname    = findViewById(R.id.profile_name);
        sessionmail    = findViewById(R.id.profile_mail);
        sessioncontact = findViewById(R.id.profile_contact);
        sessiongender  = findViewById(R.id.profile_gender);
        profile_image  = findViewById(R.id.profile_image);

        updateFullname = findViewById(R.id.profileupdate_fullname);
        updateEmail    = findViewById(R.id.profileupdate_email);
        updateUsername = findViewById(R.id.profileupdate_username);
        updateBio      = findViewById(R.id.profileupdate_profbio);
        spinner        = findViewById(R.id.profileupdate_spinner);

        //getting needed values form the session
        SessionHolder sessionHolder = new SessionHolder(this);
        HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
        String scontact  = userdetails.get(SessionHolder.KEY_PHONENUMBER);
        String sgender   = userdetails.get(SessionHolder.KEY_GENDER);
        String sname   = userdetails.get(SessionHolder.KEY_FULLNAME);
        String semail   = userdetails.get(SessionHolder.KEY_EMAIL);
        userid           = userdetails.get(SessionHolder.KEY_USERID);
        updateBio.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        categories = new ArrayList<String>();
        categories.add("Cleaners");
        categories.add("Electrician");
        categories.add("Painters");
        categories.add("Tank Cleaners");
        categories.add("Plumbers");
        categories.add("Pest Control");
        categories.add("Roofers");

        //getting and setting values in the textfield
        sessioncontact.setText(scontact);
        sessiongender.setText(sgender);
        sessionname.setText(sname);
        sessionmail.setText(semail);
        if(userType != null && userType.equals("customer")){
            updateBio.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phonenumber").equalTo(scontact);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String email = dataSnapshot.child(userid).child("email").getValue(String.class);
                        String fullname = dataSnapshot.child(userid).child("fullname").getValue(String.class);
                        String username = dataSnapshot.child(userid).child("username").getValue(String.class);

                        sessionname.setText(fullname);
                        sessionmail.setText(email);
                        updateFullname.getEditText().setText(fullname);
                        updateUsername.getEditText().setText(username);
                        updateEmail.getEditText().setText(email);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            updateBio.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            final Query checkProfUser = FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("phonenumber").equalTo(scontact);
            checkProfUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String email = dataSnapshot.child(userid).child("email").getValue(String.class);
                        String fullname = dataSnapshot.child(userid).child("fullname").getValue(String.class);
                        String username = dataSnapshot.child(userid).child("username").getValue(String.class);
                        String bio      = dataSnapshot.child(userid).child("bio").getValue(String.class);
                        final String category = dataSnapshot.child(userid).child("category").getValue(String.class);

                        sessionname.setText(fullname);
                        sessionmail.setText(email);
                        updateFullname.getEditText().setText(fullname);
                        updateUsername.getEditText().setText(username);
                        updateEmail.getEditText().setText(email);
                        updateBio.getEditText().setText(bio);

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                UserProfile.this,
                                R.layout.dropdown_menulist,
                                categories
                        );
                        spinner.setAdapter(adapter);
                        int spinnerPosition = adapter.getPosition(category);
                        spinner.setSelection(spinnerPosition);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    profCategory = parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



        StorageReference profileref = storageReference.child(userid);
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(profile_image);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

    }


    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK&& data !=null && data.getData()!=null){
            imageUri = data.getData();
            Glide.with(getApplicationContext())
                    .load(imageUri)
                    .into(profile_image);
            uploadProfilePic();
        }
    }

    private void uploadProfilePic() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading profile Image");
        progressDialog.show();
        final StorageReference riversRef = storageReference.child(userid);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        progressDialog.dismiss();
                        Toast.makeText(UserProfile.this, "Profile Image set", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(UserProfile.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progresspercent = (100.00 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Percentage: " + (int) progresspercent + "%");
                    }
                });
    }

    public void updateProfiledata(View view) {
        InternetConnection internetConnection = new InternetConnection();
        internetConnection.isConnectedWifi(this);

        if (!validateFullname() | !validateUsername() | !validateEmail() ) {
            return;
        }


        String fname = updateFullname.getEditText().getText().toString();
        String uname = updateUsername.getEditText().getText().toString();
        String umail = updateEmail.getEditText().getText().toString();


        if(userType != null && userType.equals("customer")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userid).child("fullname").setValue(fname);
            reference.child(userid).child("username").setValue(uname);
            reference.child(userid).child("email").setValue(umail);
        }else{
            String ubio = updateBio.getEditText().getText().toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Professionals");
            reference.child(userid).child("fullname").setValue(fname);
            reference.child(userid).child("username").setValue(uname);
            reference.child(userid).child("email").setValue(umail);
            reference.child(userid).child("bio").setValue(ubio);
            reference.child(userid).child("category").setValue(profCategory);

        }

        SessionHolder sessionHolder = new SessionHolder(this);
        sessionHolder.profileUpdateSession(fname,uname,umail);

        Toast.makeText(this, "Your profile is Updated", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    private boolean validateFullname() {
        String val = updateFullname.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            updateFullname.setError("Field cannot be empty");
            return false;
        }
        updateFullname.setError(null);
        updateFullname.setErrorEnabled(false);
        return true;
    }

    private boolean validateUsername() {
        String val = updateUsername.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            updateUsername.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            updateUsername.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            updateUsername.setError("No White spaces are allowed!");
            return false;
        }
        updateUsername.setError(null);
        updateUsername.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmail() {
        String val   = updateEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            updateEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            updateEmail.setError("Invalid Email!");
            return false;
        }
        updateEmail.setError(null);
        updateEmail.setErrorEnabled(false);
        return true;
    }

    public void profile_goback(View view) {
        UserProfile.super.onBackPressed();
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