package com.example.mypatchapplication.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionHolder {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    //public static final String SESSION_USERSESSION = "userLoginSession";

    private static final String IS_LOGIN       = "IsLoggedIn";
    public static final String KEY_FULLNAME    = "fullname";
    public static final String KEY_USERID      = "userid";
    public static final String KEY_USERNAME    = "username";
    public static final String KEY_EMAIL       = "email";
    public static final String KEY_DOB         = "dob";
    public static final String KEY_ADDRESS     = "address";
    public static final String KEY_GENDER      = "gender";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_PASSWORD    = "password";
    public static final String KEY_USERTYPE    = "usertype";

    public SessionHolder(Context _context) {
        context     = _context;
        userSession = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor      = userSession.edit();
    }

    public void createLoginSession(Boolean sessionPass,String userid, String fullname, String username, String email, String password, String address, String gender, String dob, String phonenumber, String usertype){
        editor.putBoolean(IS_LOGIN,sessionPass);
        editor.putString(KEY_USERID,userid);
        editor.putString(KEY_FULLNAME,fullname);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_DOB,dob);
        editor.putString(KEY_ADDRESS,address);
        editor.putString(KEY_GENDER,gender);
        editor.putString(KEY_PHONENUMBER,phonenumber);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_USERTYPE,usertype);

        editor.commit();
    }

    public void profileUpdateSession(String fullname, String username, String email){
            editor.putString(KEY_FULLNAME, fullname);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_EMAIL, email);
            editor.commit();
    }

    public void addressUpdateSession(String address){
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public HashMap<String,String> getUserSessionDetails(){
        HashMap<String,String> userData = new HashMap<String,String>();
        userData.put(KEY_USERID, userSession.getString(KEY_USERID, null));
        userData.put(KEY_FULLNAME, userSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_DOB, userSession.getString(KEY_DOB, null));
        userData.put(KEY_ADDRESS, userSession.getString(KEY_ADDRESS, null));
        userData.put(KEY_GENDER, userSession.getString(KEY_GENDER, null));
        userData.put(KEY_PHONENUMBER, userSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_USERTYPE, userSession.getString(KEY_USERTYPE, null));
        return userData;
    }

    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN, false)){
            return true;
        }else {
            return false;
        }
    }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }
}
