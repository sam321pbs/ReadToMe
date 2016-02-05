package com.example.sammengistu.readtome;

import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.GetBookInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
                values.put(LibraryHelper.COLUMN_TITLE, GetBookInfo.getBookTitle(book.getEPubFile()));
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
}
