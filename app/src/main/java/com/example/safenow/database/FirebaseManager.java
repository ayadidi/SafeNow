package com.example.safenow.database;

import com.example.safenow.models.ContactUrgence;
import com.example.safenow.models.SOS;
import com.example.safenow.models.Utilisateur;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 1. Créer/Mettre à jour un Utilisateur
    public void saveUser(Utilisateur user) {
        db.collection("users").document(user.getEmail()).set(user);
    }

    // 2. Ajouter un Contact d'urgence
    public void addContact(ContactUrgence contact) {
        db.collection("contacts").add(contact);
    }

    // 3. Envoyer un SOS (Crée l'historique)
    public void sendSOS(SOS sos) {
        db.collection("alerts").add(sos);
    }
}