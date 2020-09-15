package com.example.mypatchapplication.Helperclass.Homeadapter;

public class FeaturedRecHelperClass {

    String image;
    String title, description;
    int rating;



    public FeaturedRecHelperClass(String image, String title, String description,int rating) {
        this.rating = rating;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
