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

/**
 * Created by SamMengistu on 1/15/16.
 */
public class TestATaleOfTwoCities  extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookATaleOfTwoCities;

    List<PageOfBook> mPageOfBooksATaleOfTwoCities;

    List<String> mChapterNamesATaleOfTwoCities;

    private Context mContext;

    private String mBookText =
        "A wonderful fact to reflect upon, that every human creature is constituted to be that profound secret and mystery to every other. A solemn consideration, when I enter a great city by night," +
            " that every one of those darkly clustered houses encloses its own secret; that every room in every one of them encloses its own secret; that every beating heart in the hundreds of thousands of breasts there, is, in some of its" +
            " imaginings, a secret to the heart nearest it! Something of the awfulness, even of Death itself, is referable to this. No more can I turn the leaves of this dear book that I loved, and vainly hope in time to read it all. No more can I look into the depths of " +
            "this unfathomable water, wherein, as momentary lights glanced into it, I have had glimpses of buried treasure and other things submerged. It was appointed that the book should shut with a spring, for ever and for ever, when I had read but a page. It was appointed that " +
            "the water should be locked in an eternal frost, when the light was playing on its surface, and I stood in ignorance on the shore. My friend is dead, my neighbour is dead, my love, the darling of my soul, is dead; it is the inexorable consolidation and perpetuation of the " +
            "secret that was always in that individuality, and which I shall carry in mine to my life's end. In any of the burial-places of this city through which I pass, is there a sleeper more inscrutable than its busy inhabitants are, in their innermost personality, to me, or than I am to them? " +
            "As to this, his natural and not to be alienated inheritance, the messenger on horseback had exactly the same possessions as the King, the first Minister of State, or the richest merchant in London. So with the three passengers shut up in the narrow compass of one lumbering old mail coach; they" +
            " were mysteries to one another, as complete as if each had been in his own coach and six, or his own coach and sixty, with the breadth of a county";

    public TestATaleOfTwoCities() {
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
            if (book.getEPubFile().getName().equals("a_tale_of_two_cities.epub")){
                mEPubBookATaleOfTwoCities = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForATaleOfTwoCities() throws Exception {
        mChapterNamesATaleOfTwoCities = new ArrayList<>();
        mPageOfBooksATaleOfTwoCities = mEPubBookATaleOfTwoCities.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksATaleOfTwoCities){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesATaleOfTwoCities.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesATaleOfTwoCities.get(0), "Format Choice");
        assertEquals(mChapterNamesATaleOfTwoCities.get(1), "A TALE OF TWO CITIES");
        assertEquals(mChapterNamesATaleOfTwoCities.get(2), "A STORY OF THE FRENCH REVOLUTION");
        assertEquals(mChapterNamesATaleOfTwoCities.get(3), "Book the First—Recalled to Life");
        assertEquals(mChapterNamesATaleOfTwoCities.get(4), "I. The Period");
        assertEquals(mChapterNamesATaleOfTwoCities.get(5), "II. The Mail");
        assertEquals(mChapterNamesATaleOfTwoCities.get(6), "III. The Night Shadows");
    }

    public void testTwoPages() throws Exception {
        mPageOfBooksATaleOfTwoCities = mEPubBookATaleOfTwoCities.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        int countPages = 0;
        boolean temp = false;

        for (PageOfBook pageOfBook : mPageOfBooksATaleOfTwoCities){
            if (pageOfBook.getChapterOfBook()
                .equals("III. The Night Shadows") || temp){
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
