package com.app.session.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.session.model.StoryDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(StoryDb.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + StoryDb.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(StoryDb.COLUMN_NOTE, note);

        // insert row
        long id = db.insert(StoryDb.TABLE_NAME, null, values);

        // close db connection
        db.close();
        System.out.println("id "+id);
        // return newly inserted row id
        return id;
    }

    public StoryDb getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(StoryDb.TABLE_NAME,
                new String[]{StoryDb.COLUMN_ID, StoryDb.COLUMN_NOTE, StoryDb.COLUMN_TIMESTAMP},
                StoryDb.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        StoryDb note = new StoryDb(
                cursor.getInt(cursor.getColumnIndex(StoryDb.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(StoryDb.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(StoryDb.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<StoryDb> getAllNotes() {
        List<StoryDb> notes = new ArrayList<>();

        // Select All Query
//        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
//                Note.COLUMN_TIMESTAMP + " DESC";

        String selectQuery = "SELECT  * FROM " + StoryDb.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StoryDb note = new StoryDb();
                note.setId(cursor.getInt(cursor.getColumnIndex(StoryDb.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(StoryDb.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(StoryDb.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount()
    {
        String countQuery = "SELECT  * FROM " + StoryDb.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        System.out.println("db count "+count);
        return count;
    }

    public int updateNote(StoryDb note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StoryDb.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(StoryDb.TABLE_NAME, values, StoryDb.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(StoryDb.TABLE_NAME, StoryDb.COLUMN_ID + " = ?",
                new String[]{String.valueOf(1)});
        db.close();
    }
}
