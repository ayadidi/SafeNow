package com.example.safenow.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users_table")
public class Utilisateur {

    @PrimaryKey
    @NonNull // Une clé primaire ne peut pas être nulle en SQLite
    private String email;

    private String nom;
    private String motDePasse;

    public Utilisateur() {} // Pour Firebase

    @Ignore
    public Utilisateur(String nom, @NonNull String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters et Setters...
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    @NonNull public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}