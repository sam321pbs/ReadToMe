package com.example.sammengistu.readtome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LibraryHelper extends SQLiteOpenHelper {

    /*
     * Table and column information
     */
    public static final String TABLE_BOOKS = "BOOKS";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_TITLE = "TITLE";

    /*
    * Database information
    */
    private static final String DB_NAME = "books.db";
    private static final int DB_VERSION = 2; // Must increment to trigger an upgrade
    private static final String DB_CREATE =
        "CREATE TABLE " + TABLE_BOOKS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " REAL)";

    private static final String DB_ALTER =
        "ALTER TABLE " + TABLE_BOOKS;


    public LibraryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    /*
     * This is triggered by incrementing DB_VERSION
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_ALTER);
    }
}
