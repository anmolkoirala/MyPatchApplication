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
import com.example.mypatchapplication.Helperclass.Model.ProfessionalModel;
import com.example.mypatchapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapater extends RecyclerView.Adapter<FeaturedAdapater.FeaturedViewHolder> {

    ArrayList<FeaturedRecHelperClass> featuredprof;
    StorageReference userImageRef;
    Context context;

    public FeaturedAdapater(ArrayList<FeaturedRecHelperClass> featuredClass, Context context) {
        this.featuredprof = featuredClass;
        this.context = context;

    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_prof_carddesign, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeaturedViewHolder holder, int position) {
        FeaturedRecHelperClass featuredRecHelperClass = featuredprof.get(position);
//        holder.image.setImageResource(featuredRecHelperClass.getImage());

        userImageRef = FirebaseStorage.getInstance().getReference(featuredRecHelperClass.getImage());
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.image);
            }
        });

        holder.rating.setRating(featuredRecHelperClass.getRating());

        holder.title.setText(featuredRecHelperClass.getTitle());
        holder.desc.setText(featuredRecHelperClass.getDescription());
    }

    @Override
    public int getItemCount() {
        return featuredprof.size();
    }


    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, desc;
        RatingBar rating;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.featured_rating);
            image = itemView.findViewById(R.id.featured_rec_image);
            title = itemView.findViewById(R.id.featured_rec_title);
            desc = itemView.findViewById(R.id.featured_rec_descp);
        }
    }
}
