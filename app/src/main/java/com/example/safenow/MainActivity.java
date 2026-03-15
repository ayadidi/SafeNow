package com.example.safenow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safenow.database.AppDatabase;
import com.example.safenow.databinding.ActivityMainBinding;
import com.example.safenow.models.ContactUrgence;
import com.example.safenow.models.SOS;
import com.example.safenow.models.Utilisateur;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore dbCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbCloud = FirebaseFirestore.getInstance();

        // --- AUTOMATISATION : Création des tables au démarrage ---
        synchroniserProfilEtContacts();

        binding.btnSOS.setOnClickListener(v -> declencherSOS());

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        binding.btnVR.setOnClickListener(v -> {
            Toast.makeText(this, "Lancement de la simulation Unity...", Toast.LENGTH_SHORT).show();
        });
        binding.btnContacts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Cette méthode crée automatiquement les tables 'users' et 'contacts'
     * sur Firebase si elles n'existent pas encore.
     */
    private void synchroniserProfilEtContacts() {
        // 1. Table Utilisateur (UML)
        Utilisateur userActuel = new Utilisateur("Aya Didi", "aya.didi@gmail.com", "password123");
        dbCloud.collection("users").document(userActuel.getEmail()).set(userActuel)
                .addOnSuccessListener(aVoid -> Log.d("SafeNow", "Table 'users' synchronisée"));

        // 2. Table Contact d'urgence (UML)
        ContactUrgence contact1 = new ContactUrgence("Aziza", "0600000000", "Amie", "user_1");
        dbCloud.collection("contacts").document("contact_aziza").set(contact1)
                .addOnSuccessListener(aVoid -> Log.d("SafeNow", "Table 'contacts' synchronisée"));
    }
    private void declencherSOS() {
        Date momentActuel = new Date();
        String currentPos = "Lat: 30.42, Long: -8.84"; // Sera remplacé par le GPS

        // Création de l'objet SOS selon ton UML
        SOS alerte = new SOS("Message", currentPos, momentActuel);

        // 1. Sauvegarde SQLite (Local)
        new Thread(() -> {
            AppDatabase.getInstance(this).appDao().insertSOS(alerte);
        }).start();

        // 2. Sauvegarde Firebase (Table 'alerts' partagée automatiquement)
        dbCloud.collection("alerts")
                .add(alerte)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "SOS partagé au groupe !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("SafeNow", "Erreur Firebase: " + e.getMessage()));
    }
}