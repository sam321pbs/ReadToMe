package com.example.sammengistu.readtome.models;

/**
 * Created by SamMengistu on 7/11/15.
 */
public class PageOfBook {

    private int mPagePicture;
    private String mPageText;
    private int mPageNumber;
    private String mChapterOfBook;


    public PageOfBook(String text, int pageNumber) {
        mChapterOfBook = "None";
        mPageText = text;
        mPageNumber = pageNumber;
    }

    public PageOfBook(String text, int pageNumber, String chapterOfBook) {
        mChapterOfBook = chapterOfBook;
        mPageText = text;
        mPageNumber = pageNumber;
    }

    public String getChapterOfBook() {
        return mChapterOfBook;
    }

    public void setChapterOfBook(String chapterOfBook) {
        mChapterOfBook = chapterOfBook;
    }

    public int getPagePicture() {
        return mPagePicture;
    }

    public String getPageText() {
        return mPageText;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
    }
}
