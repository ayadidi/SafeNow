package com.example.safenow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
// Pour getMessage(), assure-toi d'avoir importé l'exception dans le listener
import com.example.safenow.database.AppDatabase;
import com.example.safenow.databinding.ActivityMainBinding;
import com.example.safenow.models.AlertEvent;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore dbCloud; // Instance Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialisation de Firebase
        dbCloud = FirebaseFirestore.getInstance();

        binding.btnSOS.setOnClickListener(v -> declencherSOS());

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        binding.btnVR.setOnClickListener(v -> {
            Toast.makeText(this, "Lancement de la simulation Unity...", Toast.LENGTH_SHORT).show();
        });
    }

    private void declencherSOS() {
        // Création de la date précise à l'instant T
        Date momentActuel = new Date();
        String currentPos = "Lat: 30.42, Long: -8.84"; // Position test

        AlertEvent alerte = new AlertEvent(momentActuel, currentPos);

        // 1. Sauvegarde SQLite (Local)
        new Thread(() -> {
            AppDatabase.getInstance(this).appDao().insertAlerte(alerte);
        }).start();

        // 2. Sauvegarde Firebase (Partage groupe)
        dbCloud.collection("alerts")
                .add(alerte)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "SOS partagé au groupe !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("SafeNow", "Erreur: " + e.getMessage()));
    }
}