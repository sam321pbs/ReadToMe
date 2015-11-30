package com.example.sammengistu.readtome.models;

import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.bookpages.ThingsFallApartBook;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO: Will later be used to get books from the server
public class Library {

    private static Library sLibrary;

    private List<Book> mMyLibrary;

    public Library(Context appContext) {
        mMyLibrary = new ArrayList<>();

        Book thingsFallApart = new Book(
            "Things Fall Apart",
            "Chinua Achebe",
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

    public List<Book> getMyLibrary() {
        return mMyLibrary;
    }

}
