package com.example.safenow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safenow.database.DatabaseHelper;


public class RegisterActivity extends AppCompatActivity {

    // Déclaration des variables
    EditText etNom, etEmail, etPassword;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assure-toi que le nom correspond à ton fichier XML (ex: activity_register2)
        setContentView(R.layout.activity_register2);

        // Initialisation
        db = new DatabaseHelper(this);
        etNom = findViewById(R.id.etNom);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Action du bouton S'inscrire
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = etNom.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();

                // Vérification si les champs sont vides
                if (nom.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertion dans la base de données
                    boolean isInserted = db.insertUser(nom, email, pass);

                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();

                        // REDIRECTION : On ferme cette activité pour revenir au Login
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Erreur : cet email existe peut-être déjà", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}