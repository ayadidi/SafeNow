package com.example.safenow.database;

import com.example.safenow.models.ContactUrgence;
import com.example.safenow.models.SOS;
import com.example.safenow.models.Utilisateur;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Crée la table 'users'
    public void setupUser(Utilisateur user) {
        db.collection("users").document(user.getEmail()).set(user);
    }

    // Crée la table 'contacts'
    public void setupContact(ContactUrgence contact) {
        db.collection("contacts").add(contact);
    }

    // Crée la table 'alerts' (SOS)
    public void setupSOS(SOS sos) {
        db.collection("alerts").add(sos);
    }
}