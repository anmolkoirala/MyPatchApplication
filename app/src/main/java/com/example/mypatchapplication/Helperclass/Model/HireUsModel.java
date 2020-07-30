package com.example.mypatchapplication.Helperclass.Model;

public class HireUsModel {

    private String username, userID, useraddress, professionalname, professionalID, professionalcategory, usernumber, professionalnumber, responsestatus, issuedescription, requesteddate, responsemessage, reviewed;
    private String innerId;


    public HireUsModel() {
    }

    public HireUsModel(String innerId,String userID, String professionalname, String professionalID, String professionalcategory, String responsestatus, String issuedescription, String requesteddate, String reviewed) {
        this.innerId              = innerId;
        this.userID               = userID;
        this.professionalname     = professionalname;
        this.professionalID       = professionalID;
        this.professionalcategory = professionalcategory;
        this.responsestatus       = responsestatus;
        this.issuedescription     = issuedescription;
        this.requesteddate        = requesteddate;
        this.reviewed             = reviewed;
    }

    public HireUsModel(String username, String userID, String useraddress, String professionalname, String professionalcategory, String professionalID, String usernumber, String professionalnumber, String responsestatus, String issuedescription, String requesteddate, String responsemessage, String reviewed) {
        this.username             = username;
        this.userID               = userID;
        this.useraddress          = useraddress;
        this.professionalname     = professionalname;
        this.professionalcategory = professionalcategory;
        this.professionalID = professionalID;
        this.usernumber           = usernumber;
        this.professionalnumber   = professionalnumber;
        this.responsestatus       = responsestatus;
        this.issuedescription     = issuedescription;
        this.requesteddate        = requesteddate;
        this.responsemessage      = responsemessage;
        this.reviewed             = reviewed;
    }

    public HireUsModel(String innerId,String username, String userID, String useraddress, String professionalname, String professionalcategory, String professionalID, String usernumber, String professionalnumber, String responsestatus, String issuedescription, String requesteddate, String responsemessage, String reviewed) {
        this.innerId              = innerId;
        this.username             = username;
        this.userID               = userID;
        this.useraddress          = useraddress;
        this.professionalname     = professionalname;
        this.professionalcategory = professionalcategory;
        this.professionalID = professionalID;
        this.usernumber           = usernumber;
        this.professionalnumber   = professionalnumber;
        this.responsestatus       = responsestatus;
        this.issuedescription     = issuedescription;
        this.requesteddate        = requesteddate;
        this.responsemessage      = responsemessage;
        this.reviewed             = reviewed;
    }


    public HireUsModel(String professionalname, String professionalID, String professionalcategory, String issuedescription, String responsemessage, String innerId) {
        this.professionalname = professionalname;
        this.professionalID = professionalID;
        this.professionalcategory = professionalcategory;
        this.issuedescription = issuedescription;
        this.responsemessage = responsemessage;
        this.innerId = innerId;

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

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
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

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getProfessionalnumber() {
        return professionalnumber;
    }

    public void setProfessionalnumber(String professionalnumber) {
        this.professionalnumber = professionalnumber;
    }

    public String getResponsestatus() {
        return responsestatus;
    }

    public void setResponsestatus(String responsestatus) {
        this.responsestatus = responsestatus;
    }

    public String getIssuedescription() {
        return issuedescription;
    }

    public void setIssuedescription(String issuedescription) {
        this.issuedescription = issuedescription;
    }

    public String getRequesteddate() {
        return requesteddate;
    }

    public void setRequesteddate(String requesteddate) {
        this.requesteddate = requesteddate;
    }

    public String getResponsemessage() {
        return responsemessage;
    }

    public void setResponsemessage(String responsemessage) {
        this.responsemessage = responsemessage;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public String getProfessionalcategory() {
        return professionalcategory;
    }

    public void setProfessionalcategory(String professionalcategory) {
        this.professionalcategory = professionalcategory;
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

}
