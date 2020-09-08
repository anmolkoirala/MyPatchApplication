package com.example.mypatchapplication.Helperclass.Homeadapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    ArrayList<ReviewHelperCLass> clientReviews;
    StorageReference userImageRef;
    Context context;

    public ReviewAdapter(ArrayList<ReviewHelperCLass> clientReviews,Context context) {
        this.clientReviews = clientReviews;
        this.context = context;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_reviews_carddesign, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, int position) {
        ReviewHelperCLass reviewHelperCLass = clientReviews.get(position);
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
        return clientReviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, desc;
        RatingBar rating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            image   = itemView.findViewById(R.id.review_image);
            title   = itemView.findViewById(R.id.review_title);
            rating = itemView.findViewById(R.id.review_rating);
            desc    = itemView.findViewById(R.id.review_descp);
        }
    }


}
