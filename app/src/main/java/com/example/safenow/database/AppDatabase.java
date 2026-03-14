package com.example.safenow.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.safenow.models.AlertEvent;

@Database(entities = {AlertEvent.class}, version = 1)
@TypeConverters({Converters.class}) // <--- AJOUTE CETTE LIGNE
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "safenow_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Permet de tester rapidement sans Thread complexe
                    .build();
        }
        return instance;
    }
}