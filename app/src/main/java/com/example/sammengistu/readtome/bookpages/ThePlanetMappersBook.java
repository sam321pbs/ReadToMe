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
    private int wordCount; // Keeps track of where in the book you are
    private int pageNumber = 0; // Keeps track of the page you are on
    private Book mThePlanetMappersBookEpubLib;
    private StringBuilder mEntireBook;

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
        mEntireBook = new StringBuilder();
        mChapterNames = new ArrayList<>();

        AssetManager assetManager = mContext.getAssets();

        try {

            // find InputStream for book

            InputStream epubInputStream = assetManager

                .open("dave_dawson_with_the_eighth.epub");

            // Load Book from inputStream

            mThePlanetMappersBookEpubLib = (new EpubReader()).readEpub(epubInputStream);

            getBookInformation();

            Log.i(TAG, "Chapter label = " + mChapterNames.get(0));

            Spine spine = new Spine(mThePlanetMappersBookEpubLib.getTableOfContents());


            mChapterTracker = 0;

            for (SpineReference bookSection : spine.getSpineReferences()) {

                Log.i("Section", "-----------------------");
                Resource res = bookSection.getResource();

                try {

                    InputStream is = res.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
                    String line;

                    StringBuilder chapterLabel = new StringBuilder();
                    StringBuilder page = new StringBuilder();
                    ArrayList<String> leftOverWordsFromPrevPage = new ArrayList<>();
                    int wordCount = 0;
                    boolean pageIsReady = false;
                    boolean haveChapterLabel = false;
                    int createFirstChapter = 0;


                    int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

                    while ((line = r.readLine()) != null) {

                        line = Html.fromHtml(line).toString();

                        line = line.replace("\n", "").replace("\r", "");
                        Log.i("Read it ", line);


                        //Check if the current line is a chapter and decide if it needs a label
                        if (isItAChapterLabel(line)){
                            chapterLabel = new StringBuilder();
                            chapterLabel.append(line);
                            haveChapterLabel = true;
                            Log.i(TAG, "Found a chapter label : " + line);

                            if (createFirstChapter != 0 ){
                                PageOfBook newPage = new PageOfBook(page.toString(), pageNumber++);

                                mPagesOfTheBook.add(newPage);
                                page = new StringBuilder();
                                pageIsReady = false;
                            }
                            createFirstChapter++;

                            continue;
                        }
                        


                        String [] lineIntoArray = line.split("\\s+");

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





















//                        boolean isNextLineChapter = false;
//                        //Checks if the next line is a new chapter
//                        String followingLine = "";
//                        if ((followingLine = r.readLine()) != null){
//                            if (isItAChapterLabel(followingLine)){
//                                isNextLineChapter = true;
//                                Log.i(TAG, "Next Line is chapter");
//                            }
//                        }
//
//                        if (isItAChapterLabel(line)) {
//
//                            Log.i(TAG, "Found chapter match = " + line);
//                            chapterLabel = new StringBuilder();
//                            chapterLabel.append(line);
//                            haveChapterLabel = true;
//
//                            continue;
//                        } else {
//                            String[] wordsInLineArray = line.split("\\s+");
//
//                            Log.i(TAG, "Words in array are " + line);
//
//                            for (int i = 0; i < wordsInLineArray.length; i++) {
//
//                                if (wordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {
//                                    wordCount = 0;
//                                    pageIsReady = true;
//                                    break;
//                                }
//
//                                if (isNextLineChapter){
//                                    wordCount = 0;
//                                    pageIsReady = true;
//                                    break;
//                                }
//
//                                page.append(wordsInLineArray[i] + SPACE);
//                                wordCount++;
//                            }
//                        }
//
//                        if (pageIsReady) {
//
//                            if (haveChapterLabel) {
//
//                                PageOfBook newPage = new PageOfBook(
//                                    page.toString(),
//                                    pageNumber++,
//                                    chapterLabel.toString());
//
//                                mPagesOfTheBook.add(newPage);
//
//                                haveChapterLabel = false;
//
//                            } else {
//                                PageOfBook newPage = new PageOfBook(
//                                    page.toString(),
//                                    pageNumber++);
//                                mPagesOfTheBook.add(newPage);
//                            }
//                            pageIsReady = false;
//                            page = new StringBuilder();
//                            chapterLabel = new StringBuilder();
//                        }
                    }
                } catch (IOException e) {
                }
            }

        } catch (IOException e) {

            Log.e("epublib", e.getMessage());
        }
    }

    private void createPage(){

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

//        for (String chapter : mChapterNames) {
//            Log.i(TAG, chapter);
//        }

//        //creates an array of all the words from the book
//        String[] allWordsFromBook = mEntireBook.toString().split("\\s+");
//
//        for (String word : allWordsFromBook) {
//            if (mChapterNames.get(0).equals(word)) {
//                Log.i("Word", word);
//                break;
//            }
//        }
//
//        boolean makeTitlePageFirst = true;
//        boolean makePageTwo = false;
//
//        for (wordCount = 0; wordCount < allWordsFromBook.length; wordCount++) {
//
//            if (makeTitlePageFirst) {
//
//                int TITLE_PAGE_LIMIT = 9;
//                makeSpecialPage(allWordsFromBook, TITLE_PAGE_LIMIT);
//
//                makeTitlePageFirst = false;
//                makePageTwo = true;
//
//            } else if (makePageTwo) {
//                int LENGTH_OF_QUOTE = 33;
//                makeSpecialPage(allWordsFromBook, LENGTH_OF_QUOTE);
//                makePageTwo = false;
//            } else {
//
//                StringBuilder chapterLabel = new StringBuilder();
//                StringBuilder page = new StringBuilder();
//                boolean hasChapter;
//
//                //Builds a chapter label
//                if (allWordsFromBook[wordCount].equals(mChapterNames.get(0).replaceAll("\\s+", ""))) {
//                    Log.i(TAG, "found chapter 1");
//                    wordCount++;
//
//                    Log.i(TAG, allWordsFromBook[wordCount - 1]);
//
//                    hasChapter = true;
//
//                    for (int i = 0; wordCount < allWordsFromBook.length; wordCount++) {
//
//                        if (allWordsFromBook[wordCount].equals("+/")) {
//                            Log.i(TAG, chapterLabel.toString());
//                            wordCount++;
//                            Log.i(TAG, allWordsFromBook[wordCount - 1]);
//                            break;
//                        } else {
//                            chapterLabel.append(allWordsFromBook[wordCount]).append(" ");
//                        }
//                    }
//
//                } else {
//                    hasChapter = false;
//                }
//
//                //Puts together enough words to fill a page
//                int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;
//                for (int goToMaxNumberOfWordsPerPage = 0;
//                     goToMaxNumberOfWordsPerPage < MAX_NUMBER_OF_WORDS_PER_PAGE;
//                     goToMaxNumberOfWordsPerPage++) {
//
//                    if (wordCount == allWordsFromBook.length) {
//                        break;
//                    }
//                    if (allWordsFromBook[wordCount].equals("/+")) {
//                        break;
//                    } else {
//                        page.append(allWordsFromBook[wordCount++]).append(SPACE);
//                    }
//                }
//
//                if (hasChapter) {
//
//                    PageOfBook newPage = new PageOfBook(
//                        page.toString(),
//                        pageNumber++,
//                        chapterLabel.toString());
//
//                    mPagesOfTheBook.add(newPage);
//                    wordCount--;
//
//                } else {
//                    PageOfBook newPage = new PageOfBook(
//                        page.toString(),
//                        pageNumber++);
//                    mPagesOfTheBook.add(newPage);
//                    wordCount--;
//                }
//            }
//        }

//        for (String)

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

    /**
     * Used to build the title page and the quote page
     *
     * @param allWordsFromBook - gets words from the book
     * @param length           - number of words from that page
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
