package com.example.safenow.models;

public class AlertEvent {
    private String date;
    private String heure;
    private String position;

    public AlertEvent(String date, String heure, String position) {
        this.date = date;
        this.heure = heure;
        this.position = position;
    }

    public String getDate() { return date; }
    public String getHeure() { return heure; }
    public String getPosition() { return position; }
}