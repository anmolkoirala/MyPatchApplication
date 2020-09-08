package com.example.mypatchapplication.Helperclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypatchapplication.Helperclass.Model.ReviewModel;
import com.example.mypatchapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllReviewsApdater extends RecyclerView.Adapter<AllReviewsApdater.AllReviewHolder> {
    private List<ReviewModel> allreviewList;
    Context context;
    int average,dbpersonCount;

    public AllReviewsApdater(List<ReviewModel> allreviewList, Context context) {
        this.allreviewList = allreviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allreviews_adapter_layout, parent, false);
        AllReviewsApdater.AllReviewHolder allReviewHolder = new AllReviewsApdater.AllReviewHolder (view);
        return allReviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllReviewHolder holder, int position) {
        final ReviewModel allreviewModel = allreviewList.get(position);
        holder.profname.setText(allreviewModel.getProfessionalname());
        holder.profcategory.setText(allreviewModel.getProfessionalcategory());
        holder.userissue.setText("User Issue: " +allreviewModel.getUserIssue());
        holder.username.setText("Reviewed By: " +allreviewModel.getUsername());
        holder.userreview.setText("User Review: " +allreviewModel.getReviewText());
        holder.userrating.setRating(allreviewModel.getReviewRating());


        //getting the AVG rating
        final Query profrefer =  FirebaseDatabase.getInstance().getReference("Professionals").orderByChild("id").equalTo(allreviewModel.getProfessionalID());
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
    }

    @Override
    public int getItemCount() {
        return allreviewList.size();
    }

    public static class AllReviewHolder extends RecyclerView.ViewHolder {
        TextView profname, profcategory, userissue, userreview,username;
        RatingBar profrating, userrating;

        public AllReviewHolder(@NonNull View itemView) {
            super(itemView);

            profname = itemView.findViewById(R.id.allreview_item_fullname);
            username = itemView.findViewById(R.id.allreview_item_username);
            profcategory = itemView.findViewById(R.id.allreview_item_category);
            userissue = itemView.findViewById(R.id.allreview_item_userissue);
            userreview = itemView.findViewById(R.id.allreview_item_userreivew);
            profrating = itemView.findViewById(R.id.allreview_item_profratingbar);
            userrating = itemView.findViewById(R.id.allreview_item_userratingbar);
        }
    }
}
