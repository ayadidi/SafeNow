package com.example.safenow;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safenow.database.AppDatabase;
import com.example.safenow.models.ContactUrgence;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEditContactActivity extends AppCompatActivity {

    private TextView tvAddEditTitle;
    private TextView tvAddEditSubtitle;
    private EditText etName, etPhone, etRelationship;
    private MaterialButton btnSave;

    private final String currentUserId = "user_1";

    private int contactId = -1;
    private ContactUrgence existingContact = null;

    private FirebaseFirestore dbCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        tvAddEditTitle = findViewById(R.id.tvAddEditTitle);
        tvAddEditSubtitle = findViewById(R.id.tvAddEditSubtitle);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etRelationship = findViewById(R.id.etRelationship);
        btnSave = findViewById(R.id.btnSave);

        dbCloud = FirebaseFirestore.getInstance();

        contactId = getIntent().getIntExtra(ContactsActivity.EXTRA_CONTACT_ID, -1);

        if (contactId != -1) {
            new Thread(() -> {
                existingContact = AppDatabase.getInstance(this).appDao().getContactById(contactId);

                runOnUiThread(() -> {
                    if (existingContact != null) {
                        fillForm(existingContact);
                        tvAddEditTitle.setText("Modifier le contact");
                        tvAddEditSubtitle.setText("Mettez à jour les informations du contact d'urgence");
                        btnSave.setText("Mettre à jour");
                    }
                });
            }).start();
        }

        btnSave.setOnClickListener(v -> saveContact());
    }

    private void fillForm(ContactUrgence contact) {
        etName.setText(contact.getNom_contact());
        etPhone.setText(contact.getNumero_contact());
        etRelationship.setText(contact.getRelation_contact());
    }

    private void saveContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String relationship = etRelationship.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Le nom est obligatoire");
            etName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Le téléphone est obligatoire");
            etPhone.requestFocus();
            return;
        }

        if (relationship.isEmpty()) {
            etRelationship.setError("La relation est obligatoire");
            etRelationship.requestFocus();
            return;
        }

        new Thread(() -> {
            try {
                if (existingContact == null) {
                    ContactUrgence newContact = new ContactUrgence(name, phone, relationship, currentUserId);

                    // 1) Insertion Room
                    long generatedId = AppDatabase.getInstance(this).appDao().insertContact(newContact);
                    newContact.setId((int) generatedId);

                    // 2) Sauvegarde Firestore avec le MÊME ID
                    dbCloud.collection("contacts")
                            .document(String.valueOf(newContact.getId()))
                            .set(newContact);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Contact ajouté avec succès", Toast.LENGTH_SHORT).show();
                        finish();
                    });

                } else {
                    existingContact.setNom_contact(name);
                    existingContact.setNumero_contact(phone);
                    existingContact.setRelation_contact(relationship);
                    existingContact.setUserId(currentUserId);

                    // 1) Mise à jour Room
                    AppDatabase.getInstance(this).appDao().updateContact(existingContact);

                    // 2) Mise à jour Firestore avec le MÊME ID
                    dbCloud.collection("contacts")
                            .document(String.valueOf(existingContact.getId()))
                            .set(existingContact);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Contact modifié avec succès", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}