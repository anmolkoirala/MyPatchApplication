package com.example.mypatchapplication.Helperclass;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewAdapter;
import com.example.mypatchapplication.Helperclass.Homeadapter.ReviewHelperCLass;
import com.example.mypatchapplication.Helperclass.Model.InternalReviewModel;
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ApplicationReviewRVAdapter extends RecyclerView.Adapter<ApplicationReviewRVAdapter.ApplicationReviewHolder> {

    ArrayList<ReviewHelperCLass>  listData;
    Context context;
    StorageReference userImageRef;

    public ApplicationReviewRVAdapter(ArrayList<ReviewHelperCLass> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_review_list, parent, false);
        ApplicationReviewRVAdapter.ApplicationReviewHolder applicationReviewHolder = new ApplicationReviewRVAdapter.ApplicationReviewHolder(view);
        return applicationReviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationReviewHolder holder, int position) {
        ReviewHelperCLass reviewHelperCLass = listData.get(position);
        userImageRef = FirebaseStorage.getInstance().getReference(reviewHelperCLass.getReviewimage());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.image);
            }
        });
        holder.rating.setRating(reviewHelperCLass.getRating());

        holder.title.setText(reviewHelperCLass.getReviewtitle());
        holder.desc.setText(reviewHelperCLass.getReviewdescription());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ApplicationReviewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, desc;
        RatingBar rating;

        public ApplicationReviewHolder(@NonNull View itemView) {
            super(itemView);

            image   = itemView.findViewById(R.id.review_image);
            title   = itemView.findViewById(R.id.review_title);
            rating = itemView.findViewById(R.id.review_rating);
            desc    = itemView.findViewById(R.id.review_descp);
        }
    }

}
