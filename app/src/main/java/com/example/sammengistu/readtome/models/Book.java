package com.example.sammengistu.readtome.models;

import com.example.sammengistu.readtome.PageOfBook;

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

    public void setPageOfBook(ArrayList<PageOfBook> pageOfBook) {
        mPagesOfBook = pageOfBook;
    }

    public UUID getBookId() {
        return mBookId;
    }

    public void setBookId(UUID bookId) {
        mBookId = bookId;
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

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public int getBookCover() {
        return mBookCover;
    }

    public void setBookCover(int bookCover) {
        mBookCover = bookCover;
    }
}
