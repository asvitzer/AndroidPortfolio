package com.alvinsvitzer.flixbook;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by alvin.svitzer on 10/04/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
