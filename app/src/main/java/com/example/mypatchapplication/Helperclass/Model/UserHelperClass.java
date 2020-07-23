package com.example.mypatchapplication.Helperclass.Model;

public class UserHelperClass {
    private String fullname,username,email,password,address,gender,dob,phonenumber,usertype;
    private double latitude, longitude;
    private String id;


    public UserHelperClass() {
    }

    public UserHelperClass(String fullname, String username, String email, String password, String address, String gender, String dob, String phonenumber, String usertype,double latitude, double longitude) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.phonenumber = phonenumber;
        this.usertype = usertype;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserHelperClass(String id,String fullname, String username, String email, String password, String address, String gender, String dob, String phonenumber, String usertype,double latitude, double longitude) {
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
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
