package com.magarex.moviemania.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.magarex.moviemania.Models.Result;

@Database(entities = {Result.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "MovieDatabase";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
