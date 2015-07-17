package com.example.sammengistu.readtome;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by SamMengistu on 7/10/15.
 */
public class Library {

    private static Library sLibrary;

    private ArrayList<Book> mMyLibrary;
    private Context mAppContext;

    public Library (Context appContext){
        mAppContext = appContext;
        mMyLibrary = new ArrayList<Book>();



        Book curiousGeorge = new Book("Curious George", "H.A.Rey",
                R.drawable.curious_george_book_cover, new CuriousGeorgePages().getPagesOfTheBook());

        Book charlottesWeb = new Book("Charlottes Web", "E. B. White",
                R.drawable.charlottes_web_book_cover, new CharlottesWebPages().getPagesOfTheBook());


        mMyLibrary.add(curiousGeorge);
        mMyLibrary.add(charlottesWeb);
    }

    public static Library get(Context c) {
        if (sLibrary == null){
            sLibrary = new Library(c.getApplicationContext());
        }
        return sLibrary;
    }

    public Book getBook(UUID bookId){
        for (Book book : mMyLibrary){
            if (book.getBookId().equals(bookId)){
                return book;
            }
        }
        return null;
    }

    public void addBook(Book book){
        mMyLibrary.add(book);
    }

    public void deleteBook(Book book){
        mMyLibrary.remove(book);
    }

    public ArrayList<Book> getMyLibrary() {
        return mMyLibrary;
    }

    public void setMyLibrary(ArrayList<Book> myLibrary) {
        mMyLibrary = myLibrary;
    }
}
