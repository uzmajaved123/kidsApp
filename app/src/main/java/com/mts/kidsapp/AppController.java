package com.mts.kidsapp;

import android.app.Application;
import android.content.Context;

public class AppController extends Application {

    public static AppController mInstance;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
