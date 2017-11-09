package com.appsplor.cryptomogul.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appsplor.cryptomogul.models.Altfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shabab on 5/20/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "altfolio_manager";

    // Altfolios table name
    private static final String TABLE_CONTACTS = "altfolios";

    // Altfolios Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "coin_name";
    private static final String KEY_PH_NO = "number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addAltfolio(Altfolio contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_NAME + "=? AND " + KEY_PH_NO + "=?",
                new String[] { String.valueOf(contact.getCoinName().toUpperCase()), String.valueOf(contact.getNumber()) }, null, null, null, null);
        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getCoinName()); // Altfolio Name
            values.put(KEY_PH_NO, contact.getNumber()); // Altfolio Phone

            // Inserting Row
            db.insert(TABLE_CONTACTS, null, values);
            db.close(); // Closing database connection
        }
    }

    // Getting single contact
    public List<Altfolio> getAltfolio(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Altfolio> contactList = new ArrayList<Altfolio>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_PH_NO + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Altfolio contact = new Altfolio();
                Log.e("cursor", Arrays.toString(cursor.getColumnNames()) + "");
                contact.setCoinName(cursor.getString(1));
                contact.setNumber(cursor.getInt(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
            // return contact
        return contactList;
    }

    // Getting All Altfolios
    public List<Altfolio> getAllAltfolios() {
        List<Altfolio> contactList = new ArrayList<Altfolio>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Altfolio contact = new Altfolio();
                contact.setCoinName(cursor.getString(1));
                contact.setNumber(cursor.getInt(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    // Deleting single contact
    public void deleteAltfolio(Altfolio contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                new String[] { String.valueOf(contact.getCoinName()) });
        db.close();
    }


    // Getting contacts Count
    public int getAltfoliosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
