package com.example.mypatchapplication.Helperclass.Homeadapter;

import android.graphics.drawable.GradientDrawable;

public class CategoriesHelperClass {
    int categoryImage;
    String categoryTitle;
    GradientDrawable gradienBackground;

    public CategoriesHelperClass(int categoryImage, String categoryTitle, GradientDrawable gradienBackground) {
        this.categoryImage = categoryImage;
        this.categoryTitle = categoryTitle;
        this.gradienBackground = gradienBackground;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public GradientDrawable getGradienBackground() {
        return gradienBackground;
    }
}
