package com.pabloliborra.uaplant.Utils;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;

@Database(version = 1, entities = {Route.class, Activity.class, Plant.class, Question.class})
@TypeConverters({ StateConverter.class })
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME ="UAPlant-Database";
    public abstract DaoApp daoApp();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 1;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabaseMain(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}