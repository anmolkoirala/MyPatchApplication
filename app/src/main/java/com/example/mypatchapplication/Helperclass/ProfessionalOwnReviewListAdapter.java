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

public class ProfessionalOwnReviewListAdapter extends RecyclerView.Adapter<ProfessionalOwnReviewListAdapter.OwnReviewHistoryHolder>{
    private List<ReviewModel> historyList;
    Context context;
    LayoutInflater dialoginflater;
    TextView popuptitle, maintitle,popupDescp;
    TextInputLayout updateReview;
    Button sendUpdate;
    int average,currentrating,dbpersonCount;
    RatingBar ratingBar;
    String current_ID;
    FirebaseDatabase rootNode         = FirebaseDatabase.getInstance();
    DatabaseReference professionalref = rootNode.getReference("Professionals");
    DatabaseReference reviewsRef      = rootNode.getReference("Reviews");


    public ProfessionalOwnReviewListAdapter(List<ReviewModel> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfessionalOwnReviewListAdapter.OwnReviewHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professional_own_review_adapterlayout, parent, false);
        ProfessionalOwnReviewListAdapter.OwnReviewHistoryHolder ownReviewHistoryHolder = new ProfessionalOwnReviewListAdapter.OwnReviewHistoryHolder(view);
        return ownReviewHistoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfessionalOwnReviewListAdapter.OwnReviewHistoryHolder holder, int position) {
        final ReviewModel reviewModel = historyList.get(position);
        holder.username.setText(reviewModel.getUsername());
        holder.userissue.setText("User Issue: " +reviewModel.getUserIssue());
        holder.userreview.setText("User Review: " +reviewModel.getReviewText());
        holder.userrating.setRating(reviewModel.getReviewRating());

        //getting the AVG rating
        final Query profrefer =  FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("id").equalTo(reviewModel.getProfessionalID());
        profrefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot datas : dataSnapshot.getChildren()) {
                        String rate        = datas.child("avgrating").getValue().toString();
                        average            = Integer.parseInt(rate);
                        holder.profrating.setRating(average);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public static class OwnReviewHistoryHolder extends RecyclerView.ViewHolder {

        TextView username, profcategory, userissue, userreview;
        RatingBar profrating, userrating;

        public OwnReviewHistoryHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.historyItem_fullname);
            userissue = itemView.findViewById(R.id.historyItem_userissue);
            userreview = itemView.findViewById(R.id.historyItem_yourreivew);
            profrating = itemView.findViewById(R.id.historyItem_profratingbar);
            userrating = itemView.findViewById(R.id.historyItem_userratingbar);

        }
    }
}
