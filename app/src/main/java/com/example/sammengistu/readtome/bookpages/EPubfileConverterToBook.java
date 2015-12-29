package com.example.sammengistu.readtome.bookpages;

import com.example.sammengistu.readtome.models.MakeAPage;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Html;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by SamMengistu on 12/15/15.
 */
public class EPubfileConverterToBook implements MakeAPage {
    private static final String TAG = "The Planet Mappers";
    public static List<PageOfBook> mPagesOfTheBook = new ArrayList<>();
    private final String SPACE = " ";
    private int pageNumber = 0; // Keeps track of the page you are on
    private Book mThePlanetMappersBookEpubLib;
    private ArrayList<String> mLeftOverWordsFromPrevPage = new ArrayList<>();
    private int mWordCount = 0;
    private int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

    private Context mContext;
    private int mChapterTracker;

    private List<String> mChapterNames;

    private String mEPubFileName;

    private StringBuilder mChapterLabel = new StringBuilder();
    private StringBuilder mPage = new StringBuilder();

    public EPubfileConverterToBook(Context c, String ePubFileName) {
        mContext = c;
        mEPubFileName = ePubFileName;
    }

    private void setUpBookInfo() {
        mChapterNames = new ArrayList<>();

        AssetManager assetManager = mContext.getAssets();

        // find InputStream for book

        InputStream epubInputStream;

        try {
            epubInputStream = assetManager

                .open(mEPubFileName);


            // Load Book from inputStream

            mThePlanetMappersBookEpubLib = (new EpubReader()).readEpub(epubInputStream);

            getBookInformation();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PageOfBook> getPagesOfTheBook() {

        addPages();
        return mPagesOfTheBook;
    }

    private void makeBook() {

        Spine spine = new Spine(mThePlanetMappersBookEpubLib.getTableOfContents());

        mChapterTracker = 0;

        boolean haveChapterLabel = false;
        int mChapterLength = 0;
        boolean pageBreak = false;


        for (SpineReference bookSection : spine.getSpineReferences()) {

            Log.i("Section", "-----------------------");
            Resource res = bookSection.getResource();

            try {

                InputStream is = res.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String line;

                int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;


                while ((line = r.readLine()) != null) {

                    int entireLineIsApartOfChapterCount = 0;

                    line = Html.fromHtml(line).toString();

                    line = line.replace("\n", "").replace("\r", "");
//                    Log.i("Read it ", line);

                    if (mChapterTracker == mChapterNames.size()) {
                        mChapterTracker = 0;
                    }

                    String[] lineIntoArray = line.split("\\s+");
                    String[] currentChapterArray = mChapterNames.get(mChapterTracker).split("\\s+");

                    for (int i = 0; i < lineIntoArray.length; i++) {

                        if (mChapterLength == currentChapterArray.length) {
                            mChapterLength = 0;
                        }

                        //Remove all special characters so words to get interferred with special characters
                        if (lineIntoArray[i].replaceAll("[\\-\\+\\.\\^:,]", "")
                            .equals(currentChapterArray[mChapterLength]
                                .replaceAll("[\\-\\+\\.\\^:,]", ""))) {

//                            Log.i(TAG, "Found a match = " + lineIntoArray[i]);
                            mChapterLength++;

                                /*If it detects a chapter label is already set and it finds a the next chapter label
                                  before word count is full it will empty create the mPage
                                  Ex. the chapter is less than a mPage long
                                  */
                            if (haveChapterLabel) {
                                addAPage(true);

                                mWordCount = 0;

                                haveChapterLabel = false;
                                pageBreak = false;
                            }

                            mChapterLabel.append(lineIntoArray[i]);
                            entireLineIsApartOfChapterCount++;

                            if (!mChapterLabel.toString().replaceAll("[\\-\\+\\.\\^:,]", "")
                                .equals(mChapterNames.get(mChapterTracker)
                                    .replaceAll("[\\-\\+\\.\\^:,]", ""))) {
                                mChapterLabel.append(" ");
                            }
                        }

                        if (mChapterLabel.toString().replaceAll("[\\-\\+\\.\\^:,]", "")
                            .equals(mChapterNames.get(mChapterTracker)
                                .replaceAll("[\\-\\+\\.\\^:,]", ""))) {
//                            Log.i(TAG, "Chapter Label  = " + mChapterLabel.toString());
//                            Log.i(TAG, "Create first chapter = " + createFirstChapter);
//
//                            Log.i(TAG, "ChapterTracker = " + mChapterTracker);
//                            Log.i("mChapterName", "Length = " + mChapterNames.size());

                            mChapterTracker++;

                            haveChapterLabel = true;

                            mChapterLength = 0;

                            pageBreak = true;
                        }
                    }


                        /*
                        If found a new chapter it show create a new mPage for the currrent text and
                        add the chapter label to the next mPage
                         */
                    if (pageBreak) {
                        if (!mPage.toString().isEmpty()) {
                            addAPage(false);
                            mWordCount = 0;
                        }
                        pageBreak = false;
                        continue;
                    }


                    if (entireLineIsApartOfChapterCount == lineIntoArray.length) {
//                        Log.i("Line full", entireLineIsApartOfChapterCount + " ");
                        continue;
                    }

                    addWordsToPage(lineIntoArray);

                    //Finish forLoop to add the remainder of sentence to ArrayList then set Page up
                    if (mWordCount == MAX_NUMBER_OF_WORDS_PER_PAGE) {

                        if (haveChapterLabel) {

                            addAPage(true);

//                            Log.i("After", "Page down " + mChapterLabel.toString());

                            haveChapterLabel = false;
                        } else {

                            if (!mPage.toString().isEmpty()) {

                                addAPage(false);
                            }
                            mWordCount = 0;
                        }

                        mWordCount = 0;
                    }

                }

            } catch (IOException e) {
                Log.i("Exception", e.toString());
            }
        }

    }

    private void addWordsToPage(String[] lineIntoArray) {
        //Checks if the previous line had any leftover words before mPage break
        if (!mLeftOverWordsFromPrevPage.isEmpty()) {
            for (String wordFromLastLine : mLeftOverWordsFromPrevPage) {
                mPage.append(wordFromLastLine + SPACE);
                mWordCount++;
            }

            mLeftOverWordsFromPrevPage.clear();
        }

        //Adds words from line to the mPage and the left over words to the arrayList
        for (String aWordFromArray : lineIntoArray) {

//                            Log.i(TAG, mWordCount + "");
//                            Log.i(TAG, "Word from array " + aWordFromArray);

            if (mWordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {

                mPage.append(aWordFromArray + SPACE);
                mWordCount++;
            } else {

                mLeftOverWordsFromPrevPage.add(aWordFromArray);
            }
        }
    }

    private void addAPage(boolean haveAChapter) {
        PageOfBook newPage;

        if (haveAChapter) {
            newPage = new PageOfBook(mPage.toString(),
                pageNumber++, mChapterLabel.toString());
            mChapterLabel = new StringBuilder();
        } else {
            newPage = new PageOfBook(mPage.toString(), pageNumber++);
        }

        mPagesOfTheBook.add(newPage);
        mPage = new StringBuilder();

    }


    private void addPages() {
        setUpBookInfo();
        makeBook();

    }

    private void getBookInformation() {

//             Log the tale of contents

        logTableOfContents(mThePlanetMappersBookEpubLib.getTableOfContents().getTocReferences(), 0);


    }

    /**
     * Recursively Log the Table of Contents
     */

    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {

        if (tocReferences == null) {

            return;
        }

        for (TOCReference tocReference : tocReferences) {

            StringBuilder tocString = new StringBuilder();


            tocString.append(tocReference.getTitle());

            if (!tocString.toString().equalsIgnoreCase("Contents")) {

                mChapterNames.add(tocString.toString());
            }

            Log.d("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);

        }

    }
}
