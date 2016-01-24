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


public class TestANaturalHistoryOf extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookANaturalHistoryOf;

    List<PageOfBook> mPageOfBooksANaturalHistoryOf;

    List<String> mChapterNamesANaturalHistoryOf;

    private Context mContext;

    public TestANaturalHistoryOf() {
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
            if (book.getEPubFile().getName().equals("a_natural_history_for_young_people.epub")){
                mEPubBookANaturalHistoryOf = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
                break;
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForANaturalHistoryOf() throws Exception {
        mChapterNamesANaturalHistoryOf = new ArrayList<>();
        mPageOfBooksANaturalHistoryOf = mEPubBookANaturalHistoryOf.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksANaturalHistoryOf){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesANaturalHistoryOf.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesANaturalHistoryOf.get(0), "Our Animal Friends IN THEIR Native Homes");
        assertEquals(mChapterNamesANaturalHistoryOf.get(1), "PREFACE.");
        assertEquals(mChapterNamesANaturalHistoryOf.get(2), "LIST OF ILLUSTRATIONS.");
        assertEquals(mChapterNamesANaturalHistoryOf.get(3), "Our Animal Friends.");
        assertEquals(mChapterNamesANaturalHistoryOf.get(4), "Quadrumana—The Four-Handed Mammals.");
        assertEquals(mChapterNamesANaturalHistoryOf.get(5), "THE GORILLA—THE STRONGEST APE.");
        assertEquals(mChapterNamesANaturalHistoryOf.get(6), "THE ORANG-OUTANG—THE WILD MAN OF THE WOODS.");
    }
}