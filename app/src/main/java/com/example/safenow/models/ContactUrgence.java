package com.example.safenow.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts_table")
public class ContactUrgence {

    @PrimaryKey(autoGenerate = true)
    private int id; // On ajoute un ID auto-incrémenté pour la base locale

    private String nom_contact;
    private String numero_contact;

    public ContactUrgence() {}

    public ContactUrgence(String nom_contact, String numero_contact) {
        this.nom_contact = nom_contact;
        this.numero_contact = numero_contact;
    }

    // Getters et Setters (inclure getId et setId)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom_contact() { return nom_contact; }
    public void setNom_contact(String nom_contact) { this.nom_contact = nom_contact; }
    public String getNumero_contact() { return numero_contact; }
    public void setNumero_contact(String numero_contact) { this.numero_contact = numero_contact; }
}