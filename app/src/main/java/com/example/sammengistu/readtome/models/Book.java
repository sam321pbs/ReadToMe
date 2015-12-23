package com.example.sammengistu.readtome.models;

import com.example.sammengistu.readtome.bookpages.EPubFileConverterToBook;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.UUID;

public class Book {

    private String mTitle;
    private String mAuthor;
    private UUID mBookId;
    private Bitmap mBookCover;
    private String mEPubFileName;
    private Context mAppContext;

    public Book(String title, String author, Bitmap bookCover, String epubFileName, Context appContext) {
        mBookCover = bookCover;
        mTitle = title;
        mAuthor = author;
        mEPubFileName = epubFileName;
        mBookId = UUID.randomUUID();
        mAppContext = appContext;
    }

    public String getEPubFileName() {
        return mEPubFileName;
    }

    public List<PageOfBook> getPagesOfBook() {
        EPubFileConverterToBook book = new EPubFileConverterToBook(mAppContext, mEPubFileName);
        return book.getPagesOfTheBook();
    }

    public UUID getBookId() {
        return mBookId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public Bitmap getBookCover() {
        return mBookCover;
    }

}
