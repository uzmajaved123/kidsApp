package com.mts.kidsapp.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mts.kidsapp.AppController;


public class Utiles {

    public static boolean internetChack() {
        ConnectivityManager ConnectionManager = (ConnectivityManager) AppController
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
