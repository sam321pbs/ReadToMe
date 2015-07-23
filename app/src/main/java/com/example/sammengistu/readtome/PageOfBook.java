package com.example.sammengistu.readtome;

/**
 * Created by SamMengistu on 7/11/15.
 */
public class PageOfBook {

    private int mPagePicture;
    private String mPageText;
    private int mPageNumber;

    public PageOfBook(int pic, String text, int pageNumber) {
        mPagePicture = pic;
        mPageText = text;
        mPageNumber = pageNumber;
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
}
