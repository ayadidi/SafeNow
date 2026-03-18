package com.example.safenow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safenow.database.DatabaseHelper;


public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnLogin;
    TextView tvRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assure-toi que le nom du XML est correct

        db = new DatabaseHelper(this);

        // Initialisation des composants (IDs de ton XML rouge)
        etEmail = findViewById(R.id.etEmailLogin);
        etPass = findViewById(R.id.etPassLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvGoToRegister);

        // Action quand on clique sur "SE CONNECTER"
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Utilisation de la méthode checkUser de ton DatabaseHelper
                if (db.checkUser(email, pass)) {
                    Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                    // Aller vers la MainActivity (partie d'Aya)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lien vers l'inscription si l'utilisateur n'a pas de compte
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}