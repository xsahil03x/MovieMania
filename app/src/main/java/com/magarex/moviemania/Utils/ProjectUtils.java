package com.magarex.moviemania.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static final String API_KEY = "78b9f63937763a206bff26c070b94158";

    public static final String FILTER_POPULAR = "popular";

    public static final String FILTER_TOP_RATED = "top_rated";

    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/";

    public static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/";

    private static final String PREF_FILE = "preference";

    public static final String PREF_FILTER = "pref_filter";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static class SharedPreferenceHelper {

        public static void setSharedPreferenceString(Context context, String key, String value) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public static String getSharedPreferenceString(Context context, String key, String defValue) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            return settings.getString(key, defValue);
        }

        public static void setSharedPreferenceLong(Context context,String key, long value) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.apply();
        }

        public static long getSharedPreferenceLong(Context context,String key, long defValue) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            return settings.getLong(key, defValue);
        }

        public static boolean contains(Context context,String key) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            return settings.contains(key);
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = Objects.requireNonNull(cn).getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }
}
