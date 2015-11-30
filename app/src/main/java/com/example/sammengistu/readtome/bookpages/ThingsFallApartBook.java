package com.example.sammengistu.readtome.bookpages;

import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.models.MakeAPage;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ThingsFallApartBook implements MakeAPage {

    public static List<PageOfBook> mPagesOfTheBook = new ArrayList<>();
    private final String SPACE = " ";
    private int wordCount; // Keeps track of where in the book you are
    private int pageNumber = 0; // Keeps track of the page you are on

    private Context mContext;

    public ThingsFallApartBook(Context c) {
        mContext = c;
    }

    public List<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private void addPages() {

        try {
            //opens file to read
            BufferedReader thingsFallApartFile = new BufferedReader(new InputStreamReader(
                mContext.getResources().openRawResource(
                    R.raw.things_fall_apart_story) // Text file to be read
            ));

            String inputString;
            StringBuilder stringOfAllWordsFromBook = new StringBuilder();

            //adds words from file into a string by reading one line at a time
            while ((inputString = thingsFallApartFile.readLine()) != null) {
                stringOfAllWordsFromBook.append(inputString).append("\n");
            }

            //creates an array of all the words from the book
            String[] allWordsFromBook = stringOfAllWordsFromBook.toString().split("\\s+");

            boolean makeTitlePageFirst = true;
            boolean makePageTwo = false;
            boolean hasChapter;

            for (wordCount = 0; wordCount < allWordsFromBook.length; wordCount++) {

                if (makeTitlePageFirst) {

                    int TITLE_PAGE_LIMIT = 9;
                    makeSpecialPage(allWordsFromBook, TITLE_PAGE_LIMIT);

                    makeTitlePageFirst = false;
                    makePageTwo = true;

                } else if (makePageTwo) {
                    int LENGTH_OF_QUOTE = 33;
                    makeSpecialPage(allWordsFromBook, LENGTH_OF_QUOTE);
                    makePageTwo = false;
                } else {
                    StringBuilder chapterName = new StringBuilder();
                    StringBuilder page = new StringBuilder();

                    //See if word has chapter
                    if (allWordsFromBook[wordCount].equals("CHAPTER")) {
                        Log.i("Things fall apart", allWordsFromBook[wordCount]);
                        Log.i("Things fall apart", allWordsFromBook[wordCount + 1]);
                        hasChapter = true;
                    } else {
                        hasChapter = false;
                    }

                    if (hasChapter) {
                        chapterName.append(allWordsFromBook[wordCount++]).append(SPACE);
                        chapterName.append(allWordsFromBook[wordCount++]);

                    }
                    //Puts together enough words to fill a page
                    int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;
                    for (int temp = 0; temp < MAX_NUMBER_OF_WORDS_PER_PAGE; temp++) {

                        if (wordCount == allWordsFromBook.length) {
                            break;
                        }
                        if (allWordsFromBook[wordCount].equals("CHAPTER")) {
                            break;
                        } else {
                            page.append(allWordsFromBook[wordCount++]).append(SPACE);
                        }
                    }

                    if (hasChapter) {
                        PageOfBook newPage = new PageOfBook(
                            page.toString(),
                            pageNumber++,
                            chapterName.toString());

                        mPagesOfTheBook.add(newPage);
                        wordCount--;

                    } else {
                        PageOfBook newPage = new PageOfBook(
                            page.toString(),
                            pageNumber++);
                        mPagesOfTheBook.add(newPage);
                        wordCount--;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to build the title page and the quote page
     * @param allWordsFromBook - gets words from the book
     * @param length - number of words from that page
     */
    private void makeSpecialPage(String[] allWordsFromBook, int length) {

        StringBuilder page = new StringBuilder();

        //adds title
        for (int temp = 0; temp < length; temp++) {

            page.append(allWordsFromBook[wordCount++]).append(SPACE);
        }

        wordCount--;
        PageOfBook newPage = new PageOfBook(
            page.toString(),
            pageNumber++);
        mPagesOfTheBook.add(newPage);
    }
}
