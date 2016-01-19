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

public class TestFamousGiversAnd extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookFamousGiversAnd;

    List<PageOfBook> mPageOfBooksFamousGiversAnd;

    List<String> mChapterNamesFamousGiversAnd;

    private Context mContext;


    private String mBookText = "\"It is a good thing to be famous, provided that the fame has been honestly won. It is a good thing to be rich when the image and superscription of" +
        " God is recognized on every coin. But the sweetest thing in the world is to be loved. The tears that were shed over the coffin of Charles Pratt welled up out of loving hearts.... I count " +
        "his death to have been the sorest bereavement Brooklyn has ever suffered; for he was yet in his vigorous prime, with large plans and possibilities yet to be accomplished. " +
        "\"Charles Pratt belonged to the only true nobility in America,—the men who do not inherit a great name, but make one for themselves.\" Thus wrote the Rev. Dr. Theodore L." +
        " Cuyler of Brooklyn, after Mr. Pratt's death in 1891. " +
        "Charles Pratt, the founder of Pratt Institute, was born at Watertown, Mass., Oct. 2, 1830. His father, Asa Pratt, a cabinet-maker, had ten children to support, so that it became necessary for each child to earn for himself whenever" +
        " that was possible. CHARLES PRATT CHARLES PRATT. When Charles was ten years old, he left home, and found a place to labor on a neighboring farm. For three years the lad, slight in physique, but ambitious to earn, worked faithfully, " +
        "and was allowed to attend school three months in each winter. At thirteen he was eager for a broader field, and, going to Boston, was employed for a year in a grocery store. Soon after he went to Newton, " +
        "and there learned the machinist's trade, saving every cent carefully, " +
        "because he had a plan in his mind; and that plan was to get an education, even if a meagre one, that he might do something in the world. Finally he had saved enough for a year's schooling, ";

    public TestFamousGiversAnd() {
        super(MyLibraryActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
        Library library = new Library(getActivity());
        List<Book> allBooks = library.getMyLibrary();

        for (Book book : allBooks) {
            Log.i("Test", "File name = " + book.getEPubFile().getName());
            if (book.getEPubFile().getName().equals("famous_givers_and_their_gifts.epub")) {
                mEPubBookFamousGiversAnd = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForFamousGivers() throws Exception {
        mChapterNamesFamousGiversAnd = new ArrayList<>();
        mPageOfBooksFamousGiversAnd = mEPubBookFamousGiversAnd.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksFamousGiversAnd) {
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)) {
                mChapterNamesFamousGiversAnd.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesFamousGiversAnd.get(0), "E-text prepared by Shaun Pinder, Martin Pettit, and the Online Distributed Proofreading Team" +
            " (http://www.pgdp.net) from page images generously made available by Internet Archive (https://archive.org)");
        assertEquals(mChapterNamesFamousGiversAnd.get(1), "Mrs. Bolton's Famous Books.");
        assertEquals(mChapterNamesFamousGiversAnd.get(2), "FAMOUS GIVERS AND THEIR GIFTS");
        assertEquals(mChapterNamesFamousGiversAnd.get(3), "PREFACE.");
        assertEquals(mChapterNamesFamousGiversAnd.get(4), "JOHN LOWELL, Jr., AND HIS FREE LECTURES.");
        assertEquals(mChapterNamesFamousGiversAnd.get(5), "STEPHEN GIRARD AND HIS COLLEGE FOR ORPHANS.");
        assertEquals(mChapterNamesFamousGiversAnd.get(6), "ANDREW CARNEGIE AND HIS LIBRARIES.");
        assertEquals(mChapterNamesFamousGiversAnd.get(7), "THOMAS HOLLOWAY: HIS SANATORIUM AND COLLEGE.");
        assertEquals(mChapterNamesFamousGiversAnd.get(8), "CHARLES PRATT AND HIS INSTITUTE.");
        assertEquals(mChapterNamesFamousGiversAnd.get(9), "THOMAS GUY AND HIS HOSPITAL.");
    }

    public void testTwoPages() throws Exception {
        mPageOfBooksFamousGiversAnd = mEPubBookFamousGiversAnd.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        int countPages = 0;
        boolean temp = false;

        for (PageOfBook pageOfBook : mPageOfBooksFamousGiversAnd) {
            if (pageOfBook.getChapterOfBook()
                .equals("CHARLES PRATT AND HIS INSTITUTE.") || temp) {
                temp = true;
                pageTextsb.append(pageOfBook.getPageText());
                countPages++;
                if (countPages == 2) {
                    break;
                }
            }
        }

        StringBuilder twohundredWords = new StringBuilder();
        StringBuilder actualsb = new StringBuilder();

        String pageTextArrays[] = pageTextsb.toString().split(" ");
        String[] actualArray = mBookText.split(" ");

        for (int i = 0; i < 199; i++) {
            twohundredWords.append(pageTextArrays[i] + " ");
            actualsb.append(actualArray[i] + " ");
        }

        assertEquals(actualsb.toString(), twohundredWords.toString());
    }

    //TODO: test page before chapter
//    public void testPageBeforeChapter() throws Exception {
//
//        mPageOfBooksFamousGiversAnd = mEPubBookFamousGiversAnd.getPagesOfTheBook();
//        StringBuilder pageTextsb = new StringBuilder();
//
//        int pageTracker = 0;
//
//        for (PageOfBook pageOfBook : mPageOfBooksFamousGiversAnd) {
//            pageTracker++;
//            if (pageOfBook.getChapterOfBook()
//                .equals("CHARLES PRATT AND HIS INSTITUTE.")) {
//                break;
//            }
//        }
//
//        for (int i = 0; i < 0; i++){
//
//        }
//
//        StringBuilder thirtyWords = new StringBuilder();
//        StringBuilder actualsb = new StringBuilder();
//
//        String pageTextArrays[] = pageTextsb.toString().split(" ");
//        String[] actualArray = mBookText.split(" ");
//
//        for (int i = 0; i < 30; i++) {
//            thirtyWords.append(pageTextArrays[i] + " ");
//            actualsb.append(actualArray[i] + " ");
//        }
//
//        assertEquals(actualsb.toString(), thirtyWords.toString());
//    }
}
