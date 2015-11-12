package com.example.sammengistu.readtome.bookpages;

import android.content.Context;
import android.util.Log;

import com.example.sammengistu.readtome.models.MakeAPage;
import com.example.sammengistu.readtome.models.PageOfBook;
import com.example.sammengistu.readtome.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SamMengistu on 9/8/15.
 */
public class ThingsFallApartBook implements MakeAPage {

    public static ArrayList<PageOfBook> mPagesOfTheBook = new ArrayList<PageOfBook>();
    private final int TITLE_PAGE_LIMIT = 9;
    private final int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;
    private final int LENGTH_OF_QUOTE = 33;
    int wordCount;
    int pageNumber = 0;

    private Context mContext;

    public ThingsFallApartBook(Context c) {
        mContext = c;
    }

    public ArrayList<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private void addPages() {

        try {
            //opens file to read
            BufferedReader thingsFallApartFile = new BufferedReader(new InputStreamReader(
                    mContext.getResources().openRawResource(R.raw.things_fall_apart_story)));

            String inputString;
            StringBuilder stringBuffer = new StringBuilder();

            //adds words from file into a string
            while ((inputString = thingsFallApartFile.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }

            //creates an array of all the words from the book
            String[] allWordsFromBook = stringBuffer.toString().split("\\s+");

            boolean makeTitlePageFirst = true;
            boolean makePageTwo = false;
            boolean hasChapter = true;
            boolean nextPageHasChapter = false;

            for (wordCount = 0; wordCount < allWordsFromBook.length; wordCount++) {

                if (makeTitlePageFirst) {

                    makeSpecialPage(allWordsFromBook, TITLE_PAGE_LIMIT);

                    makeTitlePageFirst = false;
                    makePageTwo = true;

                } else if (makePageTwo) {
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
                        chapterName.append(allWordsFromBook[wordCount++] + " ");
                        chapterName.append(allWordsFromBook[wordCount++]);

                    }
                    //Puts together enough words to fill a page
                    for (int temp = 0; temp < MAX_NUMBER_OF_WORDS_PER_PAGE; temp++) {

                        if (wordCount == allWordsFromBook.length) {
                            break;
                        }
                        if (allWordsFromBook[wordCount].equals("CHAPTER")) {
                            break;
                        } else {
                            page.append(allWordsFromBook[wordCount++] + " ");
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
        } catch (IOException e)

        {
            e.printStackTrace();
        }
    }

    private void makeSpecialPage(String[] allWordsFromBook, int length) {

        StringBuilder page = new StringBuilder();

        //adds title
        for (int temp = 0; temp < length; temp++) {

            page.append(allWordsFromBook[wordCount++] + " ");
        }

        wordCount--;
        PageOfBook newPage = new PageOfBook(
                page.toString(),
                pageNumber++);
        mPagesOfTheBook.add(newPage);
    }
}
