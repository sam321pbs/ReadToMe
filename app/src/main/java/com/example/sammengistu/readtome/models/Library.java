package com.example.sammengistu.readtome.models;

import android.content.Context;

import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.bookpages.CharlottesWebPages;
import com.example.sammengistu.readtome.bookpages.ThingsFallApartBook;

import java.util.ArrayList;
import java.util.UUID;

public class Library {

    private static Library sLibrary;

    private ArrayList<Book> mMyLibrary;
    private Context mAppContext;

    public Library(Context appContext) {
        mAppContext = appContext;
        mMyLibrary = new ArrayList<>();


        Book curiousGeorge = new Book("Things Fall Apart", "Chinua Achebe",
                R.drawable.things_fall_apart_book_cover,
                new ThingsFallApartBook(mAppContext).getPagesOfTheBook());

        Book charlottesWeb = new Book("Charlottes Web", "E. B. White",
                R.drawable.charlottes_web_book_cover, new CharlottesWebPages(mAppContext).getPagesOfTheBook(), "Chapter 1");


        mMyLibrary.add(curiousGeorge);
        mMyLibrary.add(charlottesWeb);
    }

    public static Library get(Context c) {
        if (sLibrary == null) {
            sLibrary = new Library(c.getApplicationContext());
        }
        return sLibrary;
    }

    public Book getBook(UUID bookId) {
        for (Book book : mMyLibrary) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public void addBook(Book book) {
        mMyLibrary.add(book);
    }

    public void deleteBook(Book book) {
        mMyLibrary.remove(book);
    }

    public ArrayList<Book> getMyLibrary() {
        return mMyLibrary;
    }

    public void setMyLibrary(ArrayList<Book> myLibrary) {
        mMyLibrary = myLibrary;
    }
}
