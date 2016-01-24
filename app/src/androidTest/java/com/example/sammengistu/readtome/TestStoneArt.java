package com.example.sammengistu.readtome;

import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.example.sammengistu.readtome.bookpages.EPubFileConverterToBook;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;
import com.example.sammengistu.readtome.models.PageOfBook;
import com.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TestStoneArt extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookStoneArt;

    List<PageOfBook> mPageOfBooksStoneArt;

    List<String> mChapterNamesStoneArt;

    private Context mContext;

    public TestStoneArt() {
        super(MyLibraryActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
        Library library = new Library(getActivity());
        List<Book> allBooks = library.getMyLibrary();

        for (Book book : allBooks){
            Log.i("Test", "File name = " + book.getEPubFile().getName());
            if (book.getEPubFile().getName().equals("stone.epub")){
                mEPubBookStoneArt = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForStoneArt() throws Exception {
        mChapterNamesStoneArt = new ArrayList<>();
        mPageOfBooksStoneArt = mEPubBookStoneArt.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksStoneArt){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesStoneArt.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesStoneArt.get(0), "STONE ART BY GERARD FOWKE");
        assertEquals(mChapterNamesStoneArt.get(1), "ILLUSTRATIONS");
        assertEquals(mChapterNamesStoneArt.get(2), "INTRODUCTION.");
        assertEquals(mChapterNamesStoneArt.get(3), "Basis for the Work.");
        assertEquals(mChapterNamesStoneArt.get(4), "Classification of Objects and Materials.");
        assertEquals(mChapterNamesStoneArt.get(5), "THE ARTS AND THEIR DISTRIBUTION.");
        assertEquals(mChapterNamesStoneArt.get(6), "Districts.");
    }
}
