package com.example.sammengistu.readtome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.List;

/**
 * Created by SamMengistu on 2/2/16.
 */
public class LibraryDataSource {
    private SQLiteDatabase mDatabase;       // The actual DB!
    private LibraryHelper mLibraryHelper; // Helper class for creating and opening the DB
    private Context mContext;

    public LibraryDataSource(Context context) {
        mContext = context;
        mLibraryHelper = new LibraryHelper(mContext);
    }

    /*
     * Open the db. Will create if it doesn't exist
     */
    public void open() throws SQLException {
        mDatabase = mLibraryHelper.getWritableDatabase();
    }

    /*
     * We always need to close our db connections
     */
    public void close() {
        mDatabase.close();
    }

     /*
     * CRUD operations!
     */

    /*
     * INSERT
     */
    public void insertBook(List<Book> books) {
        mDatabase.beginTransaction();

        try {
            for (Book book : books) {
                ContentValues values = new ContentValues();
                values.put(LibraryHelper.COLUMN_TITLE, book.getTitle());
                mDatabase.insert(LibraryHelper.TABLE_BOOKS, null, values);
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    /*
     * SELECT ALL
     */
    public Cursor selectAllBooks() {
        Cursor cursor = mDatabase.query(
            LibraryHelper.TABLE_BOOKS, // table
            new String[]{LibraryHelper.COLUMN_TITLE}, // column names
            null, // where clause
            null, // where params
            null, // groupby
            null, // having
            null  // orderby
        );

        return cursor;
    }

    /*
     * DELETE
     */
    public void deleteAll() {
        mDatabase.delete(
            LibraryHelper.TABLE_BOOKS, // table
            null, // where clause
            null  // where params
        );
    }

    public boolean CheckIsDataAlreadyInDBorNot(
//        String TableName,
//                                                      String dbfield,
        String fieldValue) {

        String Query = "Select * from " +
            LibraryHelper.TABLE_BOOKS + " where " +
            fieldValue + " = " +
            LibraryHelper.COLUMN_TITLE;

        Cursor cursor = mDatabase.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean findTitle(String pidValue) {
        Cursor cursor = null;
        String sql = "SELECT TITLE FROM " + LibraryHelper.TABLE_BOOKS + " WHERE TITLE=" + pidValue;
        cursor = mDatabase.rawQuery(sql, null);
        Log.i("DB", "Cursor Count : " + cursor.getCount());

        return cursor.getCount() > 0;
    }

    public Cursor findTitle2 (String title){
        String whereClause = LibraryHelper.COLUMN_TITLE + " = ?";

        Cursor cursor = mDatabase.query(
            LibraryHelper.TABLE_BOOKS, // table
            new String[] { LibraryHelper.COLUMN_TITLE }, // column names
            whereClause, // where clause
            new String[] { title }, // where params
            null, // groupby
            null, // having
            null  // orderby
        );

        return cursor;
    }

    public String searchKeyString(String key){
        StringBuilder rtn = new StringBuilder();
//        Log.d("searchKeyString",table);

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LibraryHelper.TABLE_BOOKS
            + " WHERE " + LibraryHelper.COLUMN_TITLE+ "=?";

        Cursor cursor = mDatabase.rawQuery(selectQuery,  new String[] {key});
        // you can change it to
        // db.rawQuery("SELECT * FROM "+table+" WHERE KEY_KEY LIKE ?", new String[] {key+"%"});
        // if you want to get everything starting with that key value

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.d("searchKeyString", "searching");

//                Log.i("Search", cursor.getString(2));
                String[] titleSeprated = cursor.getString(1).split(" ");
                for (String title: titleSeprated) {

                }
                Log.i("Search", cursor.getString(1));
//                Log.i("Search", cursor.getString(0));

                rtn.append(",").append(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.d("searchKeyString", "finish search");

        return rtn.toString();
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(LibraryHelper.TABLE_BOOKS);

        Cursor cursor = builder.query(mLibraryHelper.getReadableDatabase(),
            columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

}
