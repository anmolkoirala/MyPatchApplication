package com.example.mypatchapplication.Helperclass.Homeadapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypatchapplication.R;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<CategoriesHelperClass> categoryList;

    public CategoryAdapter(ArrayList<CategoriesHelperClass> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_carddesign, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoriesHelperClass categoryHelperClass = categoryList.get(position);
        holder.image.setImageResource(categoryHelperClass.getCategoryImage());
        holder.title.setText(categoryHelperClass.getCategoryTitle());
        holder.relativeLayout.setBackground(categoryHelperClass.getGradienBackground());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        RelativeLayout relativeLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.category_title);
            relativeLayout = itemView.findViewById(R.id.categories_layout);
        }
    }
}
