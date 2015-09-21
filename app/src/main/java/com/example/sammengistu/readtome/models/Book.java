package com.example.sammengistu.readtome.models;

import java.util.ArrayList;
import java.util.UUID;

public class Book {

    private String mTitle;
    private String mAuthor;
    private UUID mBookId;
    private int mBookCover;
    private ArrayList<PageOfBook> mPagesOfBook;
    private String mChapter;

    public Book(String title, String author, int bookCover, ArrayList<PageOfBook> pagesOfBook) {
        mBookCover = bookCover;
        mTitle = title;
        mAuthor = author;
        mPagesOfBook = pagesOfBook;
        mBookId = UUID.randomUUID();
    }

    public Book(String title, String author, int bookCover, ArrayList<PageOfBook> pagesOfBook, String chapter) {
        this(title, author, bookCover, pagesOfBook);
        mChapter = chapter;
    }

    public String getChapter() {
        return mChapter;
    }

    public ArrayList<PageOfBook> getPagesOfBook() {
        return mPagesOfBook;
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

    public int getBookCover() {
        return mBookCover;
    }

}
