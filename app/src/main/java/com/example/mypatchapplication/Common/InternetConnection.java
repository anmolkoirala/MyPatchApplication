package com.example.mypatchapplication.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class InternetConnection {

    public void isConnectedWifi(final Context context) {
        //this function will check weather the wifi is connected or not
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        AlertDialog.Builder a_builder = new AlertDialog.Builder(context) ;
        if(nInfo != null && nInfo.isConnected()){
            AlertDialog alert = a_builder.create();
            alert.cancel();
        }
        else {
            a_builder.setMessage("Please Connect to the internet to proceed further in app!")
                    .setCancelable(false)
                    .setPositiveButton("Wifi Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in = new Intent(Settings.ACTION_WIFI_SETTINGS  );
                            context.startActivity(in);
                        }
                    } )
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("No Internet Connection");
            alert.show();
        }
    }
}
