package com.example.sammengistu.readtome.models;

import java.util.List;
import java.util.UUID;

public class Book {

    private String mTitle;
    private String mAuthor;
    private UUID mBookId;
    private int mBookCover;
    private List<PageOfBook> mPagesOfBook;

    public Book(String title, String author, int bookCover, List<PageOfBook> pagesOfBook) {
        mBookCover = bookCover;
        mTitle = title;
        mAuthor = author;
        mPagesOfBook = pagesOfBook;
        mBookId = UUID.randomUUID();
    }

    public List<PageOfBook> getPagesOfBook() {
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
