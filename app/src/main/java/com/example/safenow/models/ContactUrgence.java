package com.example.safenow.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts_table")
public class ContactUrgence {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom_contact;
    private String numero_contact;
    private String relation_contact;
    private String userId;

    public ContactUrgence() {}

    public ContactUrgence(String nom_contact, String numero_contact, String relation_contact, String userId) {
        this.nom_contact = nom_contact;
        this.numero_contact = numero_contact;
        this.relation_contact = relation_contact;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_contact() {
        return nom_contact;
    }

    public void setNom_contact(String nom_contact) {
        this.nom_contact = nom_contact;
    }

    public String getNumero_contact() {
        return numero_contact;
    }

    public void setNumero_contact(String numero_contact) {
        this.numero_contact = numero_contact;
    }

    public String getRelation_contact() {
        return relation_contact;
    }

    public void setRelation_contact(String relation_contact) {
        this.relation_contact = relation_contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}