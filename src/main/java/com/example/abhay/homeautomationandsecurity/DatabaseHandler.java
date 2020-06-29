package com.example.abhay.homeautomationandsecurity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HomeAutomation";
    private static final String TABLE_SWITCH = "Switch";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SWITCH_TABLE = "CREATE TABLE " + TABLE_SWITCH + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_SWITCH_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SWITCH);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addSwitch(Switch contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name

        // Inserting Row
        db.insert(TABLE_SWITCH, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Switch getSwitch(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SWITCH + " WHERE "
                + KEY_ID + " = " + id;

        /*Cursor cursor = db.query(TABLE_SWITCH, new String[] { KEY_ID,
                        KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null)
            cursor.moveToFirst();

        Switch contact = new Switch(cursor.getInt(0),
                cursor.getString(1));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Switch> getAllSwitch() {
        List<Switch> contactList = new ArrayList<Switch>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SWITCH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Switch contact = new Switch();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateSwitch(Switch contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        //values.put(KEY_NAME, newName);
        // updating row
        return db.update(TABLE_SWITCH, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteSwitch(Switch contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SWITCH, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getSwitchCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SWITCH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
