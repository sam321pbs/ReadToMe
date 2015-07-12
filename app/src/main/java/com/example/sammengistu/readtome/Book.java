package com.example.sammengistu.readtome;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by SamMengistu on 7/10/15.
 */
public class Book {

    private String mTitle;
    private String mAuthor;
    private UUID mBookId;
    private int mBookCover;
    private ArrayList<PageOfBook> mPagesOfBook;

    public Book(String title, String author, int bookCover, ArrayList<PageOfBook> pagesOfBook){
        mPagesOfBook = pagesOfBook;
        mBookId = UUID.randomUUID();
        mTitle = title;
        mAuthor = author;
        mBookCover = bookCover;
    }


    public ArrayList<PageOfBook> getPageOfBook() {
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
