package com.example.sammengistu.readtome.bookpages;

import com.example.sammengistu.readtome.models.MakeAPage;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class ThePlanetMappersBook implements MakeAPage {
    private static final String TAG = "The Planet Mappers";
    public static List<PageOfBook> mPagesOfTheBook = new ArrayList<>();
    private final String SPACE = " ";
    private int pageNumber = 0; // Keeps track of the page you are on
    private Book mThePlanetMappersBookEpubLib;

    private Context mContext;
    private int mChapterTracker;

    private List<String> mChapterNames;

    public ThePlanetMappersBook(Context c) {
        mContext = c;
    }

    public List<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private void makeStringBuilderOfEntireBook() {
        mChapterNames = new ArrayList<>();

        AssetManager assetManager = mContext.getAssets();

        try {

            // find InputStream for book

            InputStream epubInputStream = assetManager

                .open("the_planet_mappers.epub");

            // Load Book from inputStream

            mThePlanetMappersBookEpubLib = (new EpubReader()).readEpub(epubInputStream);

            getBookInformation();

            Log.i(TAG, "Chapter label = " + mChapterNames.get(0));

            Spine spine = new Spine(mThePlanetMappersBookEpubLib.getTableOfContents());


            mChapterTracker = 0;

            StringBuilder chapterLabel = new StringBuilder();
            StringBuilder page = new StringBuilder();
            ArrayList<String> leftOverWordsFromPrevPage = new ArrayList<>();
            int wordCount = 0;
            boolean pageIsReady = false;
            boolean haveChapterLabel = false;
            int createFirstChapter = 0;
            boolean skipNextLineChapterRemainder = false;
            StringBuilder actualChapterLabelToCheckAgainst = new StringBuilder();

            for (SpineReference bookSection : spine.getSpineReferences()) {

                Log.i("Section", "-----------------------");
                Resource res = bookSection.getResource();

                try {

                    InputStream is = res.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
                    String line;

                    int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

                    while ((line = r.readLine()) != null) {

                        line = Html.fromHtml(line).toString();

                        line = line.replace("\n", "").replace("\r", "");
                        Log.i("Read it ", line);

                        String [] lineIntoArray = line.split("\\s+");

                        //Removes
                        if (skipNextLineChapterRemainder){
                            skipNextLineChapterRemainder = false;

                            String [] chapterLabelArray = actualChapterLabelToCheckAgainst
                                .toString().split("\\s+");

                            //May not a
//                            if (line.contains(chapterLabelArray[chapterLabelArray.length - 1])){
//                                Log.i("Next", "Line = " + line + "ChapterLabelArray = " + chapterLabelArray[chapterLabelArray.length -1]);
//                                continue;
//                            }

                        }

                        //Check if the current line is a chapter and decide if it needs a label
                        if (isItAChapterLabel(line)){
                            chapterLabel = new StringBuilder();
                            chapterLabel.append(mChapterNames.get(mChapterTracker - 1));
                            haveChapterLabel = true;
                            Log.i(TAG, "Found a chapter label : " + line);

                            if (createFirstChapter != 0 ){
                                PageOfBook newPage = new PageOfBook(page.toString(), pageNumber++);

                                mPagesOfTheBook.add(newPage);
                                page = new StringBuilder();
                                pageIsReady = false;
                                wordCount = 0;
                            }
                            createFirstChapter++;

                            skipNextLineChapterRemainder = true;
                            continue;
                        }




                        //Checks if the previous line had any leftover words before page break
                        if (!leftOverWordsFromPrevPage.isEmpty()){
                            for (String wordFromLastLine : leftOverWordsFromPrevPage) {
                                page.append(wordFromLastLine + SPACE);
                                wordCount++;
                            }

                            leftOverWordsFromPrevPage.clear();
                        }

                        //Adds words from line to the page and the left words to the arrayList
                        for (String aWordFromArray : lineIntoArray) {

//                            Log.i(TAG, wordCount + "");
//                            Log.i(TAG, "Word from array " + aWordFromArray);

                            if (wordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {

                                page.append(aWordFromArray + SPACE);
                                wordCount++;
                            } else {

                                leftOverWordsFromPrevPage.add(aWordFromArray);
                            }
                        }

                        //Finish forLoop to add the remainder of sentence to ArrayList then set Page up
                        if (wordCount == MAX_NUMBER_OF_WORDS_PER_PAGE){
                            pageIsReady = true;
                            wordCount = 0;
                        }

                        if (pageIsReady){

                            if (haveChapterLabel){
                                PageOfBook newPage = new PageOfBook(
                                    page.toString(),
                                    pageNumber++,
                                    chapterLabel.toString());

                                mPagesOfTheBook.add(newPage);
                                page = new StringBuilder();
                                pageIsReady = false;
                                chapterLabel = new StringBuilder();

                                haveChapterLabel = false;
                            } else {
                                PageOfBook newPage = new PageOfBook(page.toString(), pageNumber++);

                                mPagesOfTheBook.add(newPage);
                                page = new StringBuilder();
                                pageIsReady = false;
                            }
                        }

                    }
                } catch (IOException e) {
                }
            }

        } catch (IOException e) {

            Log.e("epublib", e.getMessage());
        }
    }


    private boolean isItAChapterLabel(String line) {
        String firstWordInChapterName = "";
        String firstWordInLine = "";

        if (mChapterTracker >= mChapterNames.size()) {
            return false;
        }
        try {
            firstWordInChapterName = mChapterNames.get(mChapterTracker).substring(0
                , mChapterNames.get(mChapterTracker).indexOf(' '));
        } catch (StringIndexOutOfBoundsException e) {

            firstWordInChapterName = mChapterNames
                .get(mChapterTracker).replaceAll("\\s+", "");

        }

        try {
            firstWordInLine = line.substring(0, line.indexOf(' '));
        } catch (StringIndexOutOfBoundsException e) {

            firstWordInLine = line.replaceAll("\\s+", "");
            ;
        }


        if (firstWordInLine.equals(firstWordInChapterName)) {

            mChapterTracker++;
            return true;
        } else {
            return false;
        }
    }

    private void addPages() {

        makeStringBuilderOfEntireBook();

    }

    private void getBookInformation() {
        try {

            // Log the book's authors

            Log.i("epublib", "author(s): " + mThePlanetMappersBookEpubLib.getMetadata().getAuthors());


            // Log the book's title

            Log.i("epublib", "title: " + mThePlanetMappersBookEpubLib.getTitle());


            // Log the book's coverimage property

            Bitmap coverImage = BitmapFactory.decodeStream(mThePlanetMappersBookEpubLib.getCoverImage()

                .getInputStream());

            Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "

                + coverImage.getHeight() + " pixels");


//             Log the tale of contents

            logTableOfContents(mThePlanetMappersBookEpubLib.getTableOfContents().getTocReferences(), 0);

        } catch (IOException e) {

        }

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

            mChapterNames.add(tocString.toString());
            Log.d("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);

        }

    }
}
