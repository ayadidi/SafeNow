package com.example.safenow.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.safenow.models.ContactUrgence;
import com.example.safenow.models.SOS;

import java.util.List;

@Dao
public interface AppDao {

    @Insert
    void insertSOS(SOS sos);

    @Query("SELECT * FROM sos_table ORDER BY id DESC")
    List<SOS> getAllSOS();

    @Insert
    void insertContact(ContactUrgence contact);

    @Update
    void updateContact(ContactUrgence contact);

    @Delete
    void deleteContact(ContactUrgence contact);

    @Query("SELECT * FROM contacts_table WHERE userId = :userId ORDER BY id DESC")
    List<ContactUrgence> getContactsByUser(String userId);

    @Query("SELECT * FROM contacts_table WHERE id = :id LIMIT 1")
    ContactUrgence getContactById(int id);
}