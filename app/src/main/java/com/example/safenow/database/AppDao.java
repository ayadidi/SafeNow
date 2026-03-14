package com.example.safenow.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.safenow.models.AlertEvent;
import java.util.List;

@Dao
public interface AppDao {

    @Insert
    void insertAlerte(AlertEvent alerte);

    @Query("SELECT * FROM alertes ORDER BY id DESC")
    List<AlertEvent> getAllAlertes();

    @Query("DELETE FROM alertes")
    void deleteAll();
}