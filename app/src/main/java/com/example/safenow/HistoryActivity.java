package com.example.safenow;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.safenow.databinding.ActivityHistoryBinding;
import com.example.safenow.models.AlertEvent;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import androidx.annotation.Nullable;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private HistoryAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Écouter la "table" Firebase en temps réel
        db.collection("alerts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Plus récent en haut
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        List<AlertEvent> alertList = value.toObjects(AlertEvent.class);
                        adapter = new HistoryAdapter(alertList);
                        binding.rvHistory.setAdapter(adapter);
                    }
                });
    }
}