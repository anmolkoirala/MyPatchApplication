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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypatchapplication.Database.SessionHolder;
import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.R;
import com.example.mypatchapplication.User.UserReviewHistory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ReviewHistoryAdapter extends RecyclerView.Adapter<ReviewHistoryAdapter.ReviewHistoryHolder>{
    private List<ReviewModel> historyList;
    Context context;
    LayoutInflater dialoginflater;
    TextView popuptitle, maintitle,poptitledes,popupDescp, popchargedpp;
    TextInputLayout updateReview, userPrice;
    Button sendUpdate;
    int average,currentrating,dbpersonCount;
    RatingBar ratingBar;
    String current_ID;
    FirebaseDatabase rootNode         = FirebaseDatabase.getInstance();
    DatabaseReference professionalref = rootNode.getReference("Professionals");
    DatabaseReference reviewsRef      = rootNode.getReference("Reviews");


    public ReviewHistoryAdapter(List<ReviewModel> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_history_adapterlayout, parent, false);
        ReviewHistoryAdapter.ReviewHistoryHolder reviewHistoryHolder = new ReviewHistoryAdapter.ReviewHistoryHolder(view);
        return reviewHistoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHistoryHolder holder, int position) {
        final ReviewModel reviewModel = historyList.get(position);
        holder.profname.setText(reviewModel.getProfessionalname());
        holder.profcategory.setText(reviewModel.getProfessionalcategory());
        holder.userissue.setText("Your Issue: " +reviewModel.getUserIssue());
        holder.userreview.setText("Your Review: " +reviewModel.getReviewText());
        holder.chargedPrice.setText("Charged Rs. " +reviewModel.getChargedprice());
        holder.userrating.setRating(reviewModel.getReviewRating());

        final int oldRate = reviewModel.getReviewRating();

        SessionHolder sessionHolder = new SessionHolder(context);
        HashMap<String, String> userdetails = sessionHolder.getUserSessionDetails();
        current_ID     = userdetails.get(SessionHolder.KEY_USERID);

        //getting the AVG rating
        final Query profrefer =  FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("id").equalTo(reviewModel.getProfessionalID());
        profrefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot datas : dataSnapshot.getChildren()) {
                        String rate        = datas.child("avgrating").getValue().toString();
                        String personcount = datas.child("ratingcount").getValue().toString();
                        average            = Integer.parseInt(rate);
                        dbpersonCount      = Integer.parseInt(personcount);
                        holder.profrating.setRating(average);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.editReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                View dialogView = dialoginflater.inflate(R.layout.popup_review, null);
                View titleView  = dialoginflater.inflate(R.layout.customtitle_alertdialouge, null);
                maintitle       = (TextView) titleView.findViewById(R.id.main_popup_header);
                updateReview    = (TextInputLayout) dialogView.findViewById(R.id.popup_usertext);
                userPrice       = (TextInputLayout) dialogView.findViewById(R.id.popup_userprice);
                popuptitle      = (TextView) dialogView.findViewById(R.id.popup_title);
                popchargedpp    = (TextView) dialogView.findViewById(R.id.popup_chargedpp);
                poptitledes     = (TextView) dialogView.findViewById(R.id.popedit_descp);
                sendUpdate      = (Button) dialogView.findViewById(R.id.popup_sendReview);
                popupDescp      = (TextView) dialogView.findViewById(R.id.popup_descp);

                popupDescp.setVisibility(View.GONE);
                userPrice.setVisibility(View.GONE);
                poptitledes.setVisibility(View.VISIBLE);

                popchargedpp.setVisibility(View.VISIBLE);
                popchargedpp.setText( "You were Charged Rs. " + String.valueOf(reviewModel.getChargedprice()));
                //setting up the title
                maintitle.setText(R.string.updateyour_reviews);
                popuptitle.setText("Review: " + reviewModel.getProfessionalname());
                //setting up the rating bar
                ratingBar   = (RatingBar) dialogView.findViewById(R.id.popup_rating);
                //user rating and review text from the database.
                int rateHistory = reviewModel.getReviewRating();
                ratingBar.setRating(rateHistory);
                currentrating = rateHistory;
                updateReview.getEditText().setText(reviewModel.getReviewText());

                if(rateHistory == 1){
                    popupDescp.setVisibility(View.VISIBLE);
                    popupDescp.setText(R.string.rate_one);
                }
                else if(rateHistory == 2){
                    popupDescp.setVisibility(View.VISIBLE);
                    popupDescp.setText(R.string.rate_two);
                }
                else if(rateHistory == 3){
                    popupDescp.setVisibility(View.VISIBLE);
                    popupDescp.setText(R.string.rate_three);
                }
                else if(rateHistory == 4){
                    popupDescp.setVisibility(View.VISIBLE);
                    popupDescp.setText(R.string.rate_four);
                }
                else if(rateHistory == 5){
                    popupDescp.setVisibility(View.VISIBLE);
                    popupDescp.setText(R.string.rate_five);
                }

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCustomTitle(titleView);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                //updating the user text and rating here
                sendUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentrating == 0){
                            Toast.makeText(context, "Please rate the Professional first.", Toast.LENGTH_SHORT).show();
                        }else {
                            //saving the average rating and number of times professional has been rated in professional reference
                            //updating the user review text and rating in reviews reference.
                            String userreview = updateReview.getEditText().getText().toString();
                            reviewsRef.child(current_ID).child(reviewModel.getInnerID()).child("reviewText").setValue(userreview);
                            //reviewsRef.child(current_ID).child(reviewModel.getInnerID()).child("reviewRating").setValue(currentrating);

                            Toast.makeText(context, "Your review has been Updated. Thank you!", Toast.LENGTH_SHORT).show();

                            //reloding the activity
                            Intent intent = new Intent(context, UserReviewHistory.class);
                            context.startActivity(intent);
                            //this line will prevent going back to same activity where duplicate date is displayed
                            ((UserReviewHistory)context).finish();
                            dialog.dismiss();
                        }
                    }
                });

            }
        });



    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public static class ReviewHistoryHolder extends RecyclerView.ViewHolder {

        TextView profname, profcategory, userissue, userreview, chargedPrice;
        RatingBar profrating, userrating;
        Button editReview;

        public ReviewHistoryHolder(@NonNull View itemView) {
            super(itemView);
            profname = itemView.findViewById(R.id.historyItem_fullname);
            profcategory = itemView.findViewById(R.id.historyItem_category);
            userissue = itemView.findViewById(R.id.historyItem_userissue);
            userreview = itemView.findViewById(R.id.historyItem_yourreivew);
            chargedPrice = itemView.findViewById(R.id.historyItem_chargedPrice);
            profrating = itemView.findViewById(R.id.historyItem_profratingbar);
            userrating = itemView.findViewById(R.id.historyItem_userratingbar);
            editReview = itemView.findViewById(R.id.historyItem_buttonEdit);

        }
    }
}
