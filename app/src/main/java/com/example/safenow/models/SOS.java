package com.example.safenow.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "sos_table")
public class SOS {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type; // "Message" ou "Appel" selon ton UML
    private String localisation;
    private Date timestamp;

    public SOS() {} // Obligatoire pour Firebase

    @Ignore
    public SOS(String type, String localisation, Date timestamp) {
        this.type = type;
        this.localisation = localisation;
        this.timestamp = timestamp;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}