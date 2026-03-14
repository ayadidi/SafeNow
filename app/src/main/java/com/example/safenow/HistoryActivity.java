package com.example.safenow;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.safenow.databinding.ActivityHistoryBinding;
import com.example.safenow.models.AlertEvent;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du View Binding pour activity_history.xml
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuration du RecyclerView (Tâche : Afficher la liste des alertes)
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Simulation de données conforme au cahier des charges [cite: 192]
        List<AlertEvent> alertList = new ArrayList<>();
        alertList.add(new AlertEvent("14/03/2026", "10:30", "Lat: 30.41, Long: -8.87"));
        alertList.add(new AlertEvent("12/03/2026", "15:45", "Lat: 30.42, Long: -8.85"));

        // Liaison de l'adaptateur
        adapter = new HistoryAdapter(alertList);
        binding.rvHistory.setAdapter(adapter);
    }
}