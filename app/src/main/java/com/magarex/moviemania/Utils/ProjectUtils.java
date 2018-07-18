package com.magarex.moviemania.Utils;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.magarex.moviemania.Database.MovieDatabase;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static final String API_KEY = "78b9f63937763a206bff26c070b94158";

    public static final String FILTER_POPULAR = "popular";

    public static final String FILTER_TOP_RATED = "top_rated";

    public static final String FILTER_FAVOURITE = "favourite";

    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/";

    public static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/";

    private static final String PREF_FILE = "preference";

    public static final String PREF_FILTER = "pref_filter";

    public static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";

    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    private static final String DF1 = "yyyy-MM-dd";

    private static final String DF2 = "MMM dd, yyyy";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static MovieDatabase getDbInstance() {
        return MovieDatabase.getInstance(MovieGlobal.getInstance());
    }

    public static String convertDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat(DF1, Locale.US);
        Date newDate = null;
        try {
            newDate = df.parse(date);
            df = new SimpleDateFormat(DF2, Locale.US);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(newDate);
    }

    public static class IntegerListConvertor {

        @TypeConverter
        public String fromIntegerList(List<Integer> genreIdList) {
            if (genreIdList == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            return gson.toJson(genreIdList, type);
        }

        @TypeConverter
        public List<Integer> toIntegerList(String integerJson) {
            if (integerJson == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            return gson.fromJson(integerJson, type);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MovieGlobal.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static int dpToPx() {
        Resources r = MovieGlobal.getInstance().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }

    public static class SharedPreferenceHelper {

        public static void setSharedPreferenceString(String key, String value) {
            SharedPreferences settings = MovieGlobal.getInstance().getSharedPreferences(PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public static String getSharedPreferenceString(String key, String defValue) {
            SharedPreferences settings = MovieGlobal.getInstance().getSharedPreferences(PREF_FILE, 0);
            return settings.getString(key, defValue);
        }

        public static void setSharedPreferenceLong(String key, long value) {
            SharedPreferences settings = MovieGlobal.getInstance().getSharedPreferences(PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.apply();
        }

        public static long getSharedPreferenceLong(String key, long defValue) {
            SharedPreferences settings = MovieGlobal.getInstance().getSharedPreferences(PREF_FILE, 0);
            return settings.getLong(key, defValue);
        }

        public static boolean contains(String key) {
            SharedPreferences settings = MovieGlobal.getInstance().getSharedPreferences(PREF_FILE, 0);
            return settings.contains(key);
        }

    }

    public static boolean showOrHide(String review) {
        char[] array = review.toCharArray();
        return array.length > 200;
    }
}
