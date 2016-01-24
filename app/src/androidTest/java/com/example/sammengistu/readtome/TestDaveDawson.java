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

public class TestDaveDawson extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookDaveDawson;

    List<PageOfBook> mPageOfBooksDaveDawson;

    List<String> mChapterNamesDaveDawson;

    private Context mContext;

    private String mBookText = "Believing that he had said more than enough, and that to so much as open his mouth would invite sudden disaster, Dawson ignored the worried, questioning eyes fixed upon him, and let his own gaze wander about the room. The first thing he noted was that there were windows on two sides." +
        " Windows that had steel shutters for blackout curtains. They were so fitted into the sash frame that when drawn they kept out both light and air. And bullets too, no doubt. But apart from the windows the room wasn't any different from scores of London apartment living rooms that he had seen. " +
        "But no! There was one big, big difference. Hanging on the wall to his right was a framed photograph of the lowest form of life ever to be born. A framed photograph of Adolf (Slaughter the women and children, too) Hitler. Just to see the photograph made Dave Dawson sick to his stomach, and he quickly took his eyes from it. " +
        "And then the side door opened and the thick-set man came into the room. " +
        "\"In a few minutes, dog traitors!\" he rasped at the two prisoners. \"In a few minutes Herr Baron will be here.\"" +
        "\"And after that where will you be, I wonder!\" Dawson couldn't keep himself from saying. " +
        "The thick-set man blinked, frowned, and turned to his partner. Hans frowned, too, and his voice sounded definitely worried as he spoke." +
        "\"The swine is trying to make us believe it is all a mistake, Erich,\" he said. \"But there is no mistake, no?\"" +
        "The man called Erich switched his beady eyes back to Dawson's face again. It seemed as though he had a moment of doubt; then it was gone as his lips slid back in a cruel smile. “No, there is no mistake!\" he said harshly. \"Too long " +
        "were we together in Herr Himmler's training school not to recognize you at once, even though you have changed a lot. No, Hans. Do not let what the dog says worry you. Come, Hans. Let us enjoy some schnapps before Herr Baron arrives. Keep your eye on them. I will get the bottle and the glasses.\"";

    public TestDaveDawson() {
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
            if (book.getEPubFile().getName().equals("dave_dawson_with_the_eighth.epub")){
                mEPubBookDaveDawson = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForDaveDawson() throws Exception {
        mChapterNamesDaveDawson = new ArrayList<>();
        mPageOfBooksDaveDawson = mEPubBookDaveDawson.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksDaveDawson){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesDaveDawson.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesDaveDawson.get(0), "DAVE DAWSON WITH THE EIGHTH AIR FORCE");
        assertEquals(mChapterNamesDaveDawson.get(1), "DAVE DAWSON WITH THE EIGHTH AIR FORCE");
        assertEquals(mChapterNamesDaveDawson.get(2), "CHAPTER ONE Junk Wings");
        assertEquals(mChapterNamesDaveDawson.get(3), "CHAPTER TWO Blitz Scars");
        assertEquals(mChapterNamesDaveDawson.get(4), "CHAPTER THREE The Dead Can't Breathe");
        assertEquals(mChapterNamesDaveDawson.get(5), "CHAPTER FOUR Herr Baron No Face");
        assertEquals(mChapterNamesDaveDawson.get(6), "CHAPTER FIVE Satan's Pawns");
    }


    public void testTwoPages() throws Exception {
        mPageOfBooksDaveDawson = mEPubBookDaveDawson.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        int countPages = 0;
        boolean temp = false;

        for (PageOfBook pageOfBook : mPageOfBooksDaveDawson){
            if (pageOfBook.getChapterOfBook()
                .equals("CHAPTER FOUR Herr Baron No Face") || temp){
                temp = true;
                pageTextsb.append(pageOfBook.getPageText());
                countPages++;
                if (countPages == 2){
                    break;
                }
            }
        }

        StringBuilder twohundredWords = new StringBuilder();
        StringBuilder actualsb = new StringBuilder();

        String pageTextArrays [] = pageTextsb.toString().split(" ");
        String [] actualArray = mBookText.split(" ");

        for (int i = 0; i < 199; i++){
            twohundredWords.append(pageTextArrays[i] + " ");
            actualsb.append(actualArray[i] + " ");
        }

        assertEquals(actualsb.toString(), twohundredWords.toString());
    }
}
