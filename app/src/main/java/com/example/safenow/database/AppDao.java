package com.example.safenow.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.safenow.models.AlertEvent;
import com.example.safenow.models.SOS;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertSOS(SOS sos);

    @Query("SELECT * FROM sos_table ORDER BY id DESC")
    List<SOS> getAllSOS();
}