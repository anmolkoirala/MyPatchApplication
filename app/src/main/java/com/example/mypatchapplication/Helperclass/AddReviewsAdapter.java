package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.HireUsModel;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.AddUserReviews;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddReviewsAdapter extends RecyclerView.Adapter<AddReviewsAdapter.ReviewHolder>{
    private List<HireUsModel> reviewlist;
    List<HireUsModel> myReveiewFull;
    LayoutInflater dialoginflater;
    Context context;
    TextView popuptitle, maintitle,popupDescp;
    TextInputLayout userReview, chargedPrice;
    Button  RemainingsSendReview;
    int currentrating = 0;
    RatingBar ratingBar;
    ReviewModel reviewModel;
    SessionHolder sessionHolder;
    FirebaseDatabase rootNode   = FirebaseDatabase.getInstance();
    DatabaseReference reference = rootNode.getReference("HireList");
    DatabaseReference ratingRef = rootNode.getReference("Reviews");
    DatabaseReference professionalref = rootNode.getReference("Professionals");
    int avgRate, dbpersonCount;

    public AddReviewsAdapter(List<HireUsModel> reviewlist, Context context) {
        this.reviewlist = reviewlist;
        this.context = context;
        myReveiewFull = new ArrayList<>(reviewlist);
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler_layout, parent, false);
        AddReviewsAdapter.ReviewHolder reviewHolder = new AddReviewsAdapter.ReviewHolder(view);
        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, int position) {
         final HireUsModel usModel = reviewlist.get(position);
        holder.fullname.setText(usModel.getProfessionalname());
        holder.category.setText(usModel.getProfessionalcategory());
        holder.issue.setText("Your Issue: " + usModel.getIssuedescription());
        holder.response.setText("Response: " + usModel.getResponsemessage());

        //getting the AVG rating
        final Query profrefer =  FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("id").equalTo(usModel.getProfessionalID());
        profrefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot datas : dataSnapshot.getChildren()) {
                        String rate        = datas.child("avgrating").getValue().toString();
                        int average = Integer.parseInt(rate);
                        holder.dispRatingbar.setRating(average);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View dialogView = dialoginflater.inflate(R.layout.popup_review, null);
                View titleView  = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                maintitle       = (TextView) titleView.findViewById(R.id.main_popup_header);

                //creating session instance to get the current user data
                sessionHolder = new SessionHolder(context);
                HashMap<String,String> userdetails = sessionHolder.getUserSessionDetails();
                final String currentID       = userdetails.get(SessionHolder.KEY_USERID);
                final String currentname     = userdetails.get(SessionHolder.KEY_FULLNAME);

                userReview           = (TextInputLayout) dialogView.findViewById(R.id.popup_usertext);
                chargedPrice         = (TextInputLayout) dialogView.findViewById(R.id.popup_userprice);
                popuptitle           = (TextView) dialogView.findViewById(R.id.popup_title);
                RemainingsSendReview = (Button) dialogView.findViewById(R.id.popup_sendReview);
                popupDescp           = (TextView) dialogView.findViewById(R.id.popup_descp);
                popupDescp.setVisibility(View.GONE);
                //setting up the title
                maintitle.setText(R.string.yourreviews);
                popuptitle.setText("Review: " + usModel.getProfessionalname());

                final Query profrefer = rootNode.getReference("Professionals").orderByChild("id").equalTo(usModel.getProfessionalID());
                profrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for(DataSnapshot datas : dataSnapshot.getChildren()) {
                                String rate        = datas.child("avgrating").getValue().toString();
                                String personcount = datas.child("ratingcount").getValue().toString();
                                avgRate      = Integer.parseInt(rate);
                                dbpersonCount = Integer.parseInt(personcount);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //setting up the rating bar
                ratingBar   = (RatingBar) dialogView.findViewById(R.id.popup_rating);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        int userrates = (int) rating;
                        currentrating = (int) ratingBar.getRating();

                        if(userrates == 1){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_one);
                        }
                        else if(userrates == 2){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_two);
                        }
                        else if(userrates == 3){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_three);
                        }
                        else if(userrates == 4){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_four);
                        }
                        else if(userrates == 5){
                            popupDescp.setVisibility(View.VISIBLE);
                            popupDescp.setText(R.string.rate_five);
                        }
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCustomTitle(titleView);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                RemainingsSendReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int inputCount = dbpersonCount + 1;
                        int inputRate = (avgRate + currentrating)/inputCount;
                        if(currentrating == 0){
                            Toast.makeText(context, "Please rate the Professional first.", Toast.LENGTH_SHORT).show();
                        }else{
                            //saving the average rating and number of times professional has been rated in professional reference
                            professionalref.child(usModel.getProfessionalID()).child("ratingcount").setValue(inputCount);
                            professionalref.child(usModel.getProfessionalID()).child("avgrating").setValue(inputRate);

                            String userreview = userReview.getEditText().getText().toString();
                            String chargedpp  = chargedPrice.getEditText().getText().toString();
                            int finalpp        = Integer.parseInt(chargedpp);
                            reviewModel = new ReviewModel(currentname,currentID,usModel.getIssuedescription(),usModel.getProfessionalname(),usModel.getProfessionalID(),usModel.getProfessionalcategory(),userreview,currentrating, finalpp);
                            ratingRef.child(currentID).push().setValue(reviewModel);

                            Toast.makeText(context, "Your review has been noted. Thank you!", Toast.LENGTH_SHORT).show();
                            reference.child(currentID).child(usModel.getInnerId()).child("reviewed").setValue("reviewed");
                            Intent intent = new Intent(context, AddUserReviews.class);
                            context.startActivity(intent);
                            //this line will prevent going back to same activity where duplicate date is displayed
                            ((AddUserReviews)context).finish();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewlist.size();
    }


    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView fullname, category, issue, response;
        AppCompatButton btnReview;
        RatingBar dispRatingbar;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            fullname      = itemView.findViewById(R.id.review_main_fullname);
            category      = itemView.findViewById(R.id.review_main_category);
            issue         = itemView.findViewById(R.id.review_main_userissue);
            response      = itemView.findViewById(R.id.review_main_feedback);
            dispRatingbar = itemView.findViewById(R.id.review_main_ratingbar);
            btnReview     = itemView.findViewById(R.id.review_main_buttonsend);
        }
    }
}
