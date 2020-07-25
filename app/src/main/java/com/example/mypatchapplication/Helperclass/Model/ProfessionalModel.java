package com.example.mypatchapplication.Helperclass.Model;

public class ProfessionalModel {

    private String fullname,username,email,password,address,gender,dob,phonenumber,usertype,category, bio;
    private double latitude, longitude;
    private String id;
    int avgrating, ratingcount;

    public ProfessionalModel(String id,String fullname, String category, String address, String phonenumber) {
        this.id      = id;
        this.fullname = fullname;
        this.category = category;
        this.address = address;
        this.phonenumber = phonenumber;
    }


    public ProfessionalModel(String id,String fullname, String category, String address, String phonenumber,int avgrating) {
        this.id      = id;
        this.fullname = fullname;
        this.category = category;
        this.address = address;
        this.phonenumber = phonenumber;
        this.avgrating = avgrating;

    }

    public ProfessionalModel() {
    }

    public ProfessionalModel(String fullname, String username, String email, String password, String address, String gender, String dob, String phonenumber, String usertype, String category, double latitude, double longitude) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.phonenumber = phonenumber;
        this.usertype = usertype;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public ProfessionalModel(String id,String fullname, String username, String email, String password, String address, String gender, String dob, String phonenumber, String usertype, String category, double latitude, double longitude, int avgrating, int ratingcount ,String bio) {
        this.id       = id;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.phonenumber = phonenumber;
        this.usertype = usertype;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avgrating = avgrating;
        this.ratingcount = ratingcount;
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public int getRatingcount() {
        return ratingcount;
    }

    public void setRatingcount(int ratingcount) {
        this.ratingcount = ratingcount;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(int avgrating) {
        this.avgrating = avgrating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
