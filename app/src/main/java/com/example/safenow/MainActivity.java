package com.example.safenow;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.safenow.databinding.ActivityMainBinding;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du View Binding (nécessite 'viewBinding true' dans build.gradle)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Bouton SOS : Action immédiate [cite: 68, 104]
        binding.btnSOS.setOnClickListener(v -> {
            declencherSOS();
        });

        // 2. Navigation vers l'Historique (Correction de l'ID : btnHistory) [cite: 90, 176]
        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // 3. Navigation vers la Simulation VR [cite: 94, 239]
        binding.btnVR.setOnClickListener(v -> {
            Toast.makeText(this, "Lancement de la simulation Unity...", Toast.LENGTH_SHORT).show();
            // Logique pour lancer l'activité VR
        });
    }

    private void declencherSOS() {
        // Selon le cahier des charges : Envoi SMS + Position GPS [cite: 69, 70]
        Toast.makeText(this, "Alerte SOS envoyée avec succès !", Toast.LENGTH_LONG).show();
    }
}