package com.twilio.video.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkOperator {

    // Method To Check Network Status
    public boolean checknetConnection(Context context)
    {
        ConnectivityManager manager;
        manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet = manager.getActiveNetworkInfo();
        if (activeNet == null)
            return false;
        else
            return true;
    }

}
