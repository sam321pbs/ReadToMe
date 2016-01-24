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
 * Created by SamMengistu on 1/4/16.
 */
public class TestTableOfContentsThePlanetMappersAndPageText extends ActivityInstrumentationTestCase2<MyLibraryActivity> {
    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;

    private EPubFileConverterToBook mEPubBookThePlanetMappers;

    List<PageOfBook> mPageOfBooksThePlanetMappers;

    List<String> mChapterNamesThePlanetMappers;
    List<Integer> mChaptersOfTheBookPageNum;

    private Context mContext;

    String chapter1book = "As he heard that dread yet telltale spang against the hull of their spaceboat, young Jon Carver dropped his reelbook and sprang to his feet. His eyes looked swiftly to help his ears trace the sudden hiss he knew was their precious air escaping.\n" +
        "In the back of his mind he heard the sudden grunt his father made, the sound of a falling body, his mother's frightened scream, and his brother's \"What's wrong?\" But he did not stop his own lanky, gangling body in its leap toward the outer bulkhead. And as he jumped, he pulled his handkerchief from his hip pocket.\n" +
        "\"Leaping tuna! If that isn't fixed quick, we'll lose our air,\" was his near-panicked thought. \"We won't be able to get where we're going. Be lucky if we come out of it alive!\"\n" +
        "So, guided by the whistling, escaping air, Jon found the hole, nearly half an inch in diameter. Into it he wadded the corner of the cloth as best he could. The outward loss of their precious air slackened, although there was still some leakage he could not stop this way. He jumped to the nearest of the many emergency repair kits scattered about the ship. From it he grabbed a metal patch and an electric torch.\n" +
        "Swiftly he plugged the latter into a wall socket. With it he quickly welded the patch into place, after pulling—with considerable difficulty—his handkerchief from the hole. \"It'll do for now,\" he decided, after carefully examining his work and listening closely to make sure there was no more whistling-out of air. \"But we'll have to go outside and really fill in and weld-plug that hole in the hull, but quick.\"\n" +
        "He re-stowed the torch, then opened a flagon of emergency oxygen-helium mixture in front of the electric blowers that kept their air circulating—to replenish what had been lost. Only then—although it had been less than two minutes, really—did he turn back to the rest of the family. He had been somewhat surprised that his father had not come to help him; he had not been at all surprised that his brother had not. Jak was a grand guy—Jon thought the world of him—but he just wasn't worth a dead salmon in an emergency like this; he did not have a mechanical type of mind.\n" +
        "Now, as he turned, Jon saw his mother and brother kneeling beside the prone body of his father, and noted with astonishment that she was crying. There was something stiff and unnatural about the man's body, too, lying there on the deck beside his recline seat.\n" +
        "A sudden fear sent the";
    public TestTableOfContentsThePlanetMappersAndPageText() {
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
            if (book.getEPubFile().getName().equals("the_planet_mappers.epub")){
                mEPubBookThePlanetMappers = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }

        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesThePlanetMappers() throws Exception {
        mChapterNamesThePlanetMappers = new ArrayList<>();
        mChaptersOfTheBookPageNum = new ArrayList<>();

        mPageOfBooksThePlanetMappers = mEPubBookThePlanetMappers.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksThePlanetMappers) {
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)) {
                mChapterNamesThePlanetMappers.add(pageOfBook.getChapterOfBook());
                mChaptersOfTheBookPageNum.add(pageOfBook.getPageNumber());
            }
        }

        assertEquals(mChapterNamesThePlanetMappers.get(0), "The Planet Mappers");
        assertEquals(mChapterNamesThePlanetMappers.get(1), "The Planet Mappers");
        assertEquals(mChapterNamesThePlanetMappers.get(2), "1");
        assertEquals(mChapterNamesThePlanetMappers.get(3), "2");
        assertEquals(mChapterNamesThePlanetMappers.get(4), "3");
        assertEquals(mChapterNamesThePlanetMappers.get(5), "4");
        assertEquals(mChapterNamesThePlanetMappers.get(6), "5");
    }

    public void testChapter1Text() throws Exception {
        StringBuilder actualPageText = new StringBuilder();
        StringBuilder bookText = new StringBuilder();

        mChapterNamesThePlanetMappers = new ArrayList<>();
        mChaptersOfTheBookPageNum = new ArrayList<>();

        mPageOfBooksThePlanetMappers = mEPubBookThePlanetMappers.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksThePlanetMappers) {
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)) {
                mChapterNamesThePlanetMappers.add(pageOfBook.getChapterOfBook());
                mChaptersOfTheBookPageNum.add(pageOfBook.getPageNumber());
            }
        }

        StringBuilder mPage = new StringBuilder();

        mPage.append(mPageOfBooksThePlanetMappers.get(mChaptersOfTheBookPageNum.get(2)).getPageText());


        mPage.append(mPageOfBooksThePlanetMappers.get(mChaptersOfTheBookPageNum.get(2) + 1).getPageText());

        chapter1book = chapter1book.replace("\n", " ");

        String [] chapter1Text = chapter1book.split(" ");
        String [] pageText = mPage.toString().split(" ");

        for (int i = 0; i <  368; i++){
            bookText.append(chapter1Text[i] + " ");
        }

        for (int i = 0; i < 368; i++){
            actualPageText.append(pageText[i] + " ");
        }

        assertEquals(actualPageText.toString(), bookText.toString());
    }
}
