package com.example.sammengistu.readtome.test.testtableofcontents;

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

/**
 * Created by SamMengistu on 1/15/16.
 */
public class TestMobyDick extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookMobyDick;

    List<PageOfBook> mPageOfBooksMobyDick;

    List<String> mChapterNamesMobyDick;

    private Context mContext;

    public TestMobyDick() {
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
            if (book.getEPubFile().getName().equals("moby_dick.epub")){
                mEPubBookMobyDick = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForMobyDick() throws Exception {
        mChapterNamesMobyDick = new ArrayList<>();
        mPageOfBooksMobyDick = mEPubBookMobyDick.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksMobyDick){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesMobyDick.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesMobyDick.get(0), "MOBY DICK; or, THE WHALE");
        assertEquals(mChapterNamesMobyDick.get(1), "Original Transcriber's Notes:");
        assertEquals(mChapterNamesMobyDick.get(2), "ETYMOLOGY.");
        assertEquals(mChapterNamesMobyDick.get(3), "(Supplied by a Late Consumptive Usher to a Grammar School)");
        assertEquals(mChapterNamesMobyDick.get(4), "EXTRACTS (Supplied by a Sub-Sub-Librarian).");
        assertEquals(mChapterNamesMobyDick.get(5), "EXTRACTS.");
        assertEquals(mChapterNamesMobyDick.get(6), "CHAPTER 1. Loomings.");
        assertEquals(mChapterNamesMobyDick.get(7), "CHAPTER 2. The Carpet-Bag.");
        assertEquals(mChapterNamesMobyDick.get(8), "CHAPTER 3. The Spouter-Inn.");
        assertEquals(mChapterNamesMobyDick.get(9), "CHAPTER 4. The Counterpane.");
    }
}
