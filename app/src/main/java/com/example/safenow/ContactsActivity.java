package com.example.safenow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safenow.database.AppDatabase;
import com.example.safenow.models.ContactUrgence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "extra_contact_id";

    private RecyclerView recyclerContacts;
    private FloatingActionButton fabAddContact;
    private TextView tvContactCount;

    private EmergencyContactAdapter adapter;
    private FirebaseFirestore dbCloud;

    private final String currentUserId = "user_1";

    private final ActivityResultLauncher<Intent> contactFormLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> loadContacts()
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerContacts = findViewById(R.id.recyclerContacts);
        fabAddContact = findViewById(R.id.fabAddContact);
        tvContactCount = findViewById(R.id.tvContactCount);

        dbCloud = FirebaseFirestore.getInstance();

        recyclerContacts.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmergencyContactAdapter(new ArrayList<>(), new EmergencyContactAdapter.OnContactActionListener() {
            @Override
            public void onEdit(ContactUrgence contact) {
                Intent intent = new Intent(ContactsActivity.this, AddEditContactActivity.class);
                intent.putExtra(EXTRA_CONTACT_ID, contact.getId());
                contactFormLauncher.launch(intent);
            }

            @Override
            public void onDelete(ContactUrgence contact) {
                showDeleteDialog(contact);
            }
        });

        recyclerContacts.setAdapter(adapter);

        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsActivity.this, AddEditContactActivity.class);
            contactFormLauncher.launch(intent);
        });

        loadContacts();
    }

    private void loadContacts() {
        new Thread(() -> {
            List<ContactUrgence> userContacts =
                    AppDatabase.getInstance(this).appDao().getContactsByUser(currentUserId);

            runOnUiThread(() -> {
                adapter.updateData(userContacts);
                if (tvContactCount != null) {
                    tvContactCount.setText(String.valueOf(userContacts.size()));
                }
            });
        }).start();
    }

    private void showDeleteDialog(ContactUrgence contact) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le contact")
                .setMessage("Voulez-vous vraiment supprimer " + contact.getNom_contact() + " ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteContact(contact))
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteContact(ContactUrgence contact) {
        new Thread(() -> {
            try {
                // 1. Supprimer en local avec Room
                AppDatabase.getInstance(this).appDao().deleteContact(contact);

                // 2. Supprimer dans Firestore avec le même ID
                dbCloud.collection("contacts")
                        .document(String.valueOf(contact.getId()))
                        .delete()
                        .addOnSuccessListener(unused -> runOnUiThread(() -> {
                            Toast.makeText(this, "Contact supprimé avec succès", Toast.LENGTH_SHORT).show();
                            loadContacts();
                        }))
                        .addOnFailureListener(e -> runOnUiThread(() -> {
                            Toast.makeText(this, "Supprimé localement, mais erreur Firestore : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            loadContacts();
                        }));

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur lors de la suppression : " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}