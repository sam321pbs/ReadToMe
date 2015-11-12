package com.example.sammengistu.readtome.models;

import android.content.Context;

import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.bookpages.ThingsFallApartBook;

import java.util.ArrayList;
import java.util.UUID;

public class Library {

    private static Library sLibrary;

    private ArrayList<Book> mMyLibrary;

    public Library(Context appContext) {
        mMyLibrary = new ArrayList<>();


        Book thingsFallApart = new Book("Things Fall Apart", "Chinua Achebe",
                R.drawable.things_fall_apart_book_cover,
                new ThingsFallApartBook(appContext).getPagesOfTheBook());


        mMyLibrary.add(thingsFallApart);
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


    public ArrayList<Book> getMyLibrary() {
        return mMyLibrary;
    }

    public void setMyLibrary(ArrayList<Book> myLibrary) {
        mMyLibrary = myLibrary;
    }
}
