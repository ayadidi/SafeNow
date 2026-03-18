package com.example.safenow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.safenow.database.AppDatabase;
import com.example.safenow.databinding.ActivityMainBinding;
import com.example.safenow.models.ContactUrgence;
import com.example.safenow.models.SOS;
import com.example.safenow.models.Utilisateur;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore dbCloud;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialisation des services
        dbCloud = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Demande des permissions dès l'ouverture
        requestPermissions();

        // Automatisation : Création des tables au démarrage (Firebase)
        synchroniserProfilEtContacts();

        // --- ACTIONS DES BOUTONS ---

        // Clic sur le gros bouton SOS
        binding.btnSOS.setOnClickListener(v -> startLocationUpdates());

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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
        }, 1);
    }

    /**
     * Étape 1 : Récupérer la localisation exacte
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Récupération de votre position...", Toast.LENGTH_SHORT).show();

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            String mapLink = "https://www.google.com/maps?q=" + lat + "," + lon;
                            String message = "URGENT ! J'ai besoin d'aide. Ma position actuelle : " + mapLink;

                            // --- ÉTAPE : RÉCUPÉRER LES CONTACTS ET ENVOYER ---
                            envoyerSmsATousLesContacts(message);

                            // Sauvegarder l'alerte dans ton historique
                            sauvegarderAlerte(lat, lon, mapLink);
                        } else {
                            Toast.makeText(this, "Impossible d'obtenir la position. Activez le GPS.", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private void envoyerSmsATousLesContacts(String message) {
        // On récupère les contacts depuis Firestore (Table 'contacts' de tes collègues)
        dbCloud.collection("ContactUrgence")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (com.google.firebase.firestore.DocumentSnapshot doc : queryDocumentSnapshots) {
                            // On récupère le numéro de chaque contact
                            String phone = doc.getString("numero_contact"); // Vérifie si le champ s'appelle 'telephone' ou 'phone'

                            if (phone != null && !phone.isEmpty()) {
                                sendSMS(phone, message);
                                Log.d("SOS_DEBUG", "SMS envoyé à : " + phone);
                            }
                        }
                        Toast.makeText(this, "Alerte envoyée à tous vos contacts !", Toast.LENGTH_SHORT).show();
                    } else {
                        // Si la liste est vide, on envoie quand même à un numéro d'urgence par défaut
                        sendSMS("0611460802", message);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("SOS_DEBUG", "Erreur récupération contacts: " + e.getMessage());
                    // En cas d'erreur réseau, envoyer au numéro de secours par défaut
                    sendSMS("0611460802", message);
                });
    }

    /**
     * Étape 2 : Envoyer le message
     */
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS de secours envoyé !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SOS_DEBUG", "Échec SMS : " + e.getMessage());
            Toast.makeText(this, "Erreur d'envoi SMS", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Étape 3 : Sauvegarde historique (Local + Cloud)
     */
    private void sauvegarderAlerte(double lat, double lon, String link) {
        Date momentActuel = new Date();
        // On utilise bien le lien ou les coordonnées ici
        String maLocalisation = "Lat: " + lat + ", Lon: " + lon;

        // Utilisation du constructeur (Type, Localisation, Timestamp)
        SOS alerte = new SOS("Urgence", maLocalisation, momentActuel);

        // Sauvegarde Firebase
        dbCloud.collection("alerts").add(alerte)
                .addOnSuccessListener(doc -> Log.d("SafeNow", "Alerte synchronisée !"))
                .addOnFailureListener(e -> Log.e("SafeNow", "Erreur : " + e.getMessage()));
    }

    private void synchroniserProfilEtContacts() {
        Utilisateur userActuel = new Utilisateur("Aya Didi", "aya.didi@gmail.com", "password123");
        dbCloud.collection("users").document(userActuel.getEmail()).set(userActuel);

        ContactUrgence contact1 = new ContactUrgence("Aziza", "0600000000", "Amie", "user_1");
        dbCloud.collection("contacts").document("contact_aziza").set(contact1);
    }
}