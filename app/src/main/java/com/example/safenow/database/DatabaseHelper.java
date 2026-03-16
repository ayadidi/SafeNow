package com.example.safenow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "SafeNow.db";
    private static final int DATABASE_VERSION = 1;

    // Table Utilisateur
    public static final String TABLE_USER = "utilisateur";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NOM = "nom";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";

    // Table Contact_urgence
    public static final String TABLE_CONTACT = "contact_urgence";
    public static final String COL_CONTACT_ID = "id";
    public static final String COL_CONTACT_NOM = "nom_contact";
    public static final String COL_CONTACT_NUM = "numero_contact";
    public static final String COL_CONTACT_USER_REF = "user_id";

    // Table SOS
    public static final String TABLE_SOS = "sos";
    public static final String COL_SOS_ID = "id";
    public static final String COL_SOS_TYPE = "type";
    public static final String COL_SOS_LOC = "localisation";
    public static final String COL_SOS_USER_REF = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NOM + " TEXT, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CONTACT + " (" +
                COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONTACT_NOM + " TEXT, " +
                COL_CONTACT_NUM + " TEXT, " +
                COL_CONTACT_USER_REF + " INTEGER, " +
                "FOREIGN KEY(" + COL_CONTACT_USER_REF + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_SOS + " (" +
                COL_SOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SOS_TYPE + " TEXT, " +
                COL_SOS_LOC + " TEXT, " +
                COL_SOS_USER_REF + " INTEGER, " +
                "FOREIGN KEY(" + COL_SOS_USER_REF + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOS);
        onCreate(db);
    }

    // --- LOGIQUE AUTHENTIFICATION ---

    public boolean insertUser(String nom, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NOM, nom);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        return db.insert(TABLE_USER, null, values) != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //

    public boolean insertContact(String nom, String num, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONTACT_NOM, nom);
        values.put(COL_CONTACT_NUM, num);
        values.put(COL_CONTACT_USER_REF, userId);
        return db.insert(TABLE_CONTACT, null, values) != -1;
    }



    public boolean insertSOS(String type, String loc, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SOS_TYPE, type);
        values.put(COL_SOS_LOC, loc);
        values.put(COL_SOS_USER_REF, userId);
        return db.insert(TABLE_SOS, null, values) != -1;
    }
}