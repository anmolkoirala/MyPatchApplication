package com.example.mypatchapplication.Common.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Common.Professional.ProfessionalList;
import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SearchCategoryActivity extends AppCompatActivity {

    private View decorView;
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private DatabaseReference mProfessionalDatabase;
    TextInputLayout name,address,contact, notes;
    LayoutInflater dialoginflater;
    TextView searchAdvise;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    HireUsModel hireUsModel;
    String responsestat, responsemsg,reqdate,review;
    ImageView searchBack;
    DatePicker reservedDate;
    SessionHolder sessionHolder;

    static StorageReference searchImagee;
    AppCompatButton buttonaddhire;
    NestedScrollView nestedSV;
    String searchtxt;
    FirebaseRecyclerAdapter<ProfessionalModel, ProfessionalsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        mProfessionalDatabase = FirebaseDatabase.getInstance().getReference("Professionals");
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        String searchtext = getIntent().getStringExtra("Category");
        searchAdvise = findViewById(R.id.search_advise);
        searchAdvise.setVisibility(View.VISIBLE);

        if(searchtext!=null){
            mSearchField.setText(getIntent().getStringExtra("Category"));
            mSearchBtn.performClick();
            firebaseCategorySearch(mSearchField.getText().toString());
        }



        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseCategorySearch(searchText);



            }
        });
    }

    private void firebaseCategorySearch(String searchText) {
        Query firebaseSearchQuery = mProfessionalDatabase.orderByChild("category").startAt(searchText).endAt(searchText + "\uf8ff");
         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProfessionalModel, ProfessionalsViewHolder>(
                ProfessionalModel.class,
                R.layout.professional_list,
                ProfessionalsViewHolder.class,
                firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ProfessionalsViewHolder viewHolder, final ProfessionalModel model, int position) {
                viewHolder.setDetails(getApplicationContext(),model.getId(),model.getFullname(),
                        model.getAddress(), model.getEmail(), model.getGender(), model.getPhonenumber(), model.getCategory(),model.getAvgrating());
                //pop up
                       viewHolder.btnhire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View mview) {
                        dialoginflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchCategoryActivity.this);

                        View titleView = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                        View dialogView = dialoginflater.inflate(R.layout.hire_professional, null);
                        dialogBuilder.setView(dialogView);

                        name    = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourname);
                        contact = (TextInputLayout) dialogView.findViewById(R.id.hire_us_yourcontact);
                        address = (TextInputLayout) dialogView.findViewById(R.id.hire_us_youraddress);
                        notes   = (TextInputLayout) dialogView.findViewById(R.id.hire_us_tellmore);
                        searchBack    = (ImageView) dialogView.findViewById(R.id.dialog_back);
                        reservedDate = (DatePicker) dialogView.findViewById(R.id.hireus_date);
                        reservedDate.setMinDate(System.currentTimeMillis() - 1000);

                        buttonaddhire = (AppCompatButton) dialogView.findViewById(R.id.hire_us_sendbutton);
                        nestedSV = (NestedScrollView) dialogView.findViewById(R.id.nestedScrollView);

                        sessionHolder = new SessionHolder(getApplicationContext());
                        HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
                        final String currentID        = userdetails.get(SessionHolder.KEY_USERID);
                        final String currentname      = userdetails.get(SessionHolder.KEY_FULLNAME);
                        String currentaddress   = userdetails.get(SessionHolder.KEY_ADDRESS);
                        String currentcontacts  = userdetails.get(SessionHolder.KEY_PHONENUMBER);

                        dialogBuilder.setCustomTitle(titleView);

                        name.getEditText().setText(currentname);
                        address.getEditText().setText(currentaddress);
                        contact.getEditText().setText(currentcontacts);
                        final AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                        buttonaddhire.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String issuedesc    = notes.getEditText().getText().toString().trim();
                                String sendname     = name.getEditText().getText().toString().trim();
                                String sendaddress  = address.getEditText().getText().toString().trim();
                                String sendphone    = contact.getEditText().getText().toString().trim();
                                int day             = reservedDate.getDayOfMonth();
                                int month           = reservedDate.getMonth();
                                int year            = reservedDate.getYear();
                                reqdate      = day +"/"+month+"/"+year;
                                responsestat = "requested";
                                responsemsg  = "";
                                review  = "notreviewed";

                                hireUsModel = new HireUsModel(sendname,currentID,sendaddress,model.getFullname(),model.getCategory(),model.getId(),sendphone,model.getPhonenumber(),responsestat,issuedesc,reqdate,responsemsg,review);
                                reference.child(currentID).push().setValue(hireUsModel);

                                Toast.makeText(getApplicationContext(), "Your Request has been sent. We will contact you soon.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        searchBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(firebaseRecyclerAdapter.getItemCount()==0){
                    Toast.makeText(SearchCategoryActivity.this, "No such category/professional found", Toast.LENGTH_SHORT).show();
                    //You can show toast or choose any other option
                }
            }

        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        mResultList.setAdapter(firebaseRecyclerAdapter);
        searchAdvise.setVisibility(View.GONE);
    }

    public void searchGoBack(View view) {
        SearchCategoryActivity.super.onBackPressed();
    }


    // View Holder Class
    public static class ProfessionalsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        AppCompatButton btnhire;

        public ProfessionalsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnhire = mView.findViewById(R.id.cat_hireme);
        }

        public void setDetails(final Context ctx, String ID, String fullName, String address, String email, String gender, String phone, String category,int avgrating) {
            TextView pfullname  = mView.findViewById(R.id.tv_fullname);
            TextView pcategory  = mView.findViewById(R.id.tv_category);
            TextView paddress   = mView.findViewById(R.id.tv_address);
            RatingBar prating =  mView.findViewById(R.id.proflist_ratingbar);

            final ImageView imageView = mView.findViewById(R.id.search_image);

            pfullname.setText(fullName);
            pcategory.setText(category);
            paddress.setText(address);

            prating.setRating(avgrating);


            searchImagee = FirebaseStorage.getInstance().getReference(ID);
            searchImagee.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ctx).load(uri).into(imageView);
                }
            });
        }
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
