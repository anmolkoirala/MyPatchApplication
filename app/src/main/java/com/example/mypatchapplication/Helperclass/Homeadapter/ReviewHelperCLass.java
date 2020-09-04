package com.example.mypatchapplication.Helperclass.Homeadapter;

public class ReviewHelperCLass {
    String reviewimage;
    String reviewtitle, reviewdescription;
    int rating;


    public ReviewHelperCLass(String reviewimage, String reviewtitle, String reviewdescription,int rating) {
        this.reviewimage = reviewimage;
        this.reviewtitle = reviewtitle;
        this.rating = rating;

        this.reviewdescription = reviewdescription;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getReviewimage() {
        return reviewimage;
    }

    public String getReviewtitle() {
        return reviewtitle;
    }


    public String getReviewdescription() {
        return reviewdescription;
    }
}
