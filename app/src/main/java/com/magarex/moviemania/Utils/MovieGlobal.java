package com.magarex.moviemania.Utils;

import android.app.Application;
import android.content.Context;

public class MovieGlobal extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
