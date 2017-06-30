package com.xinye.app;

import android.app.Application;

import com.droid.library.app.LibraryConfig;


public class AppAppliaction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LibraryConfig.getInstance()
                .setDebug(true)
                .setAppContext(getApplicationContext())
                .config();
    }
}
