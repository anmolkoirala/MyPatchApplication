package com.example.mypatchapplication.Helperclass.Model;

public class ReviewModel {
    String username, userID, professionalname, professionalID, professionalcategory, reviewText,userIssue,innerID;
    int reviewRating, chargedprice ;

    public ReviewModel() {
    }

    public ReviewModel(String username, String userID,String userIssue, String professionalname, String professionalID, String professionalcategory, String reviewText, int reviewRating, int chargedprice) {
        this.username         = username;
        this.userID           = userID;
        this.userIssue        = userIssue;
        this.professionalname = professionalname;
        this.professionalID   = professionalID;
        this.professionalcategory = professionalcategory;
        this.reviewText       = reviewText;
        this.reviewRating     = reviewRating;
        this.chargedprice     = chargedprice;
    }
    public ReviewModel(String username,String userIssue, String professionalname, String professionalID, String professionalcategory, String reviewText, int reviewRating) {
        this.username         = username;
        this.userIssue        = userIssue;
        this.professionalname = professionalname;
        this.professionalID   = professionalID;
        this.professionalcategory = professionalcategory;
        this.reviewText       = reviewText;
        this.reviewRating     = reviewRating;
    }


    public ReviewModel(String innerID,String username, String userID,String userIssue, String professionalname, String professionalID, String professionalcategory, String reviewText, int reviewRating, int chargedprice) {
        this.innerID          = innerID;
        this.username         = username;
        this.userID           = userID;
        this.userIssue        = userIssue;
        this.professionalname = professionalname;
        this.professionalID   = professionalID;
        this.professionalcategory = professionalcategory;
        this.reviewText       = reviewText;
        this.reviewRating     = reviewRating;
        this.chargedprice     = chargedprice;
    }

    public int getChargedprice() {
        return chargedprice;
    }

    public void setChargedprice(int chargedprice) {
        this.chargedprice = chargedprice;
    }

    public String getInnerID() {
        return innerID;
    }

    public void setInnerID(String innerID) {
        this.innerID = innerID;
    }

    public String getUserIssue() {
        return userIssue;
    }

    public void setUserIssue(String userIssue) {
        this.userIssue = userIssue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfessionalname() {
        return professionalname;
    }

    public void setProfessionalname(String professionalname) {
        this.professionalname = professionalname;
    }

    public String getProfessionalID() {
        return professionalID;
    }

    public void setProfessionalID(String professionalID) {
        this.professionalID = professionalID;
    }

    public String getProfessionalcategory() {
        return professionalcategory;
    }

    public void setProfessionalcategory(String professionalcategory) {
        this.professionalcategory = professionalcategory;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }
}
