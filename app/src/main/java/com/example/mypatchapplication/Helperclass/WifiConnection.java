package com.example.mypatchapplication.Helperclass;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class WifiConnection extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean noConnectivity;
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
              // Toast.makeText(context, "No Internet Connection. Check your status.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("No Internet Connection. Check your status");

            } else {
                Toast.makeText(context, "Internet Connection established.", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
