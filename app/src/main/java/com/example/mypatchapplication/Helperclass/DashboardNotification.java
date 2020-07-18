package com.example.mypatchapplication.Helperclass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class DashboardNotification {
    Context context;
    public final static String Response_Channel ="Response";


    public DashboardNotification(Context context) {
        this.context = context;
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chResponse = new NotificationChannel(Response_Channel,"Response", NotificationManager.IMPORTANCE_HIGH);
            chResponse.setDescription("You have 2 tasks set for today");
            chResponse.enableVibration(true);
            chResponse.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            chResponse.setShowBadge(false);



            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chResponse);



        }
    }
}
