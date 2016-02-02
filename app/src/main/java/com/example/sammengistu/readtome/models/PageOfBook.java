package com.example.sammengistu.readtome.models;

public class PageOfBook {

    private String mPageText;
    private int mPageNumber;
    private String mChapterOfBook;

    public static final String PAGE_HAS_NO_CHAPTER = "*None*";

    public PageOfBook(String text, int pageNumber) {
        mChapterOfBook = PAGE_HAS_NO_CHAPTER;
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

    public String getPageText() {
        return mPageText;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
