package com.example.safenow.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "alertes")
public class AlertEvent {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date timestamp; // Contient Date + Heure
    private String position;

    public AlertEvent() {} // Obligatoire pour Firebase

    public AlertEvent(Date timestamp, String position) {
        this.timestamp = timestamp;
        this.position = position;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}