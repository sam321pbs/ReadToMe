package com.example.sammengistu.readtome.models;

import com.example.sammengistu.readtome.bookpages.EPubFileConverterToBook;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Book {

    private String mTitle;
    private UUID mBookId;
    private Context mAppContext;
    private File mEPubFile;

    public Book (File epubFile, Context appContext){
        mEPubFile = epubFile;
        mBookId = UUID.randomUUID();
        mAppContext = appContext;
    }

    public File getEPubFile() {
        return mEPubFile;
    }

    public List<PageOfBook> getPagesOfBook() {
        EPubFileConverterToBook book = new EPubFileConverterToBook(mAppContext, mEPubFile);
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

}
