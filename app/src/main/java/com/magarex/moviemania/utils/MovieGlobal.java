package com.magarex.moviemania.utils;

import android.app.Application;
import android.content.Context;

public class MovieGlobal extends Application {

    private static MovieGlobal mGlobalInstance;

    public static Context getInstance() {
        return mGlobalInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalInstance = (MovieGlobal) getApplicationContext();
    }
}

