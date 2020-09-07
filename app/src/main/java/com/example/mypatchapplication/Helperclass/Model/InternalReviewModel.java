package com.example.mypatchapplication.Helperclass.Model;

import com.example.mypatchapplication.Database.SessionHolder;

public class InternalReviewModel {

    private String fullname, userID, usercontact, usergender, useremail, usertype, reivewdescription;
    private int apprating;

    public InternalReviewModel(String fullname, String userID, String usercontact, String usergender, String useremail, String usertype, String reivewdescription, int apprating) {
        this.fullname = fullname;
        this.userID = userID;
        this.usercontact = usercontact;
        this.usergender = usergender;
        this.useremail = useremail;
        this.usertype = usertype;
        this.reivewdescription = reivewdescription;
        this.apprating = apprating;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsercontact() {
        return usercontact;
    }

    public void setUsercontact(String usercontact) {
        this.usercontact = usercontact;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getReivewdescription() {
        return reivewdescription;
    }

    public void setReivewdescription(String reivewdescription) {
        this.reivewdescription = reivewdescription;
    }

    public int getApprating() {
        return apprating;
    }

    public void setApprating(int apprating) {
        this.apprating = apprating;
    }
}
