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
public class TestTableOfContentsThePlanetMappersAndPageText extends ActivityInstrumentationTestCase2<MyLibraryActivity>  {
    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;

    EPubFileConverterToBook mEPubBookThePlanetMappers;

    List<PageOfBook> mPageOfBooksThePlanetMappers;

    List<String> mChapterNamesThePlanetMappers;

    private Context mContext;

    public TestTableOfContentsThePlanetMappersAndPageText() {
        super(MyLibraryActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mEPubBookThePlanetMappers = new EPubFileConverterToBook(mContext, "the_planet_mappers.epub");
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesThePlanetMappers() throws Exception {
        mChapterNamesThePlanetMappers = new ArrayList<>();

        mPageOfBooksThePlanetMappers = mEPubBookThePlanetMappers.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksThePlanetMappers){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesThePlanetMappers.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesThePlanetMappers.get(0), "The Planet Mappers");
        assertEquals(mChapterNamesThePlanetMappers.get(1), "THE PLANET MAPPERS");
        assertEquals(mChapterNamesThePlanetMappers.get(2), "1");
        assertEquals(mChapterNamesThePlanetMappers.get(3), "2");
        assertEquals(mChapterNamesThePlanetMappers.get(4), "3");
        assertEquals(mChapterNamesThePlanetMappers.get(5), "4");
        assertEquals(mChapterNamesThePlanetMappers.get(6), "5");
    }

    public void testChapter1Text() throws Exception {
        String
    }
}
