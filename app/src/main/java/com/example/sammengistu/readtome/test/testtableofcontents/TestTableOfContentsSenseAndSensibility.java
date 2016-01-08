package com.example.sammengistu.readtome.test.testtableofcontents;

import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.example.sammengistu.readtome.bookpages.EPubFileConverterToBook;
import com.example.sammengistu.readtome.models.PageOfBook;
import com.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamMengistu on 1/4/16.
 */
public class TestTableOfContentsSenseAndSensibility extends ActivityInstrumentationTestCase2<MyLibraryActivity> {
    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookSenseAndSensibility;

    List<PageOfBook> mPageOfBooksSenseAndSensiblity;

    List<String> mChapterNamesSenseAndSensibility;

    private Context mContext;

    public TestTableOfContentsSenseAndSensibility() {
        super(MyLibraryActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
//        mEPubBookSenseAndSensibility = new EPubFileConverterToBook(mContext, );
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForSenseAndSensibility() throws Exception {
        mChapterNamesSenseAndSensibility = new ArrayList<>();
        mPageOfBooksSenseAndSensiblity = mEPubBookSenseAndSensibility.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksSenseAndSensiblity){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesSenseAndSensibility.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesSenseAndSensibility.get(0), "Sense and Sensibility");
        assertEquals(mChapterNamesSenseAndSensibility.get(1), "(1811)");
        assertEquals(mChapterNamesSenseAndSensibility.get(2), "CHAPTER 1");
        assertEquals(mChapterNamesSenseAndSensibility.get(3), "CHAPTER 2");
        assertEquals(mChapterNamesSenseAndSensibility.get(4), "CHAPTER 3");
        assertEquals(mChapterNamesSenseAndSensibility.get(5), "CHAPTER 4");
        assertEquals(mChapterNamesSenseAndSensibility.get(6), "CHAPTER 5");
    }
}





















