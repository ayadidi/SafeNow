package com.example.safenow.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.safenow.models.SOS; // 1. On importe le nouveau modèle
import com.example.safenow.models.Utilisateur;
import com.example.safenow.models.ContactUrgence;

// 2. On met à jour la liste des entités avec SOS.class
// 3. On augmente la version à 2 car le schéma a changé
@Database(entities = {SOS.class, Utilisateur.class, ContactUrgence.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "safenow_db")
                    .fallbackToDestructiveMigration() // 4. Supprime l'ancienne table 'AlertEvent' et crée 'sos_table'
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}