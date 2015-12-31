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


public class EPubFileConverterToBook implements MakeAPage {
    private static final String TAG = EPubFileConverterToBook.class.getName();
    public static List<PageOfBook> mPagesOfTheBook = new ArrayList<>();
    private final String SPACE = " ";
    private int mPageNumber = 0; // Keeps track of the page you are on
    private Book mEpubBook;
    private ArrayList<String> mLeftOverWordsFromPrevPage = new ArrayList<>();
    private int mWordCount = 0;
    boolean mWordMatchEntireLine;

    private Context mContext;
    private int mChapterTracker;

    private List<String> mChapterNames;

    private String mEPubFileName;

    private StringBuilder mChapterLabel = new StringBuilder();
    private StringBuilder mPage = new StringBuilder();

    private boolean mHaveChapterLabel = false;
    private int mChapterWordLocation = 0;
    private boolean mPageBreak = false;

    private String mPreviousWord = "";

    /**
     * Takes an epub file name and finds the file and sets up the class for conversion
     *
     * @param c            - context to get access to the file
     * @param ePubFileName - file name to search for
     */
    public EPubFileConverterToBook(Context c, String ePubFileName) {
        mContext = c;
        mEPubFileName = ePubFileName;
    }

    /**
     * Opens the book file and sets up the eBook
     * and gets the table of contents from the epub file
     */
    private void setUpBookInfo() {
        mChapterNames = new ArrayList<>();

        AssetManager assetManager = mContext.getAssets();

        // find InputStream for book

        InputStream epubInputStream;

        try {
            epubInputStream = assetManager

                .open(mEPubFileName);


            // Load Book from inputStream

            mEpubBook = (new EpubReader()).readEpub(epubInputStream);

            getTableOfContents(mEpubBook.getTableOfContents().getTocReferences(), 0);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<PageOfBook> getPagesOfTheBook() {

        addPages();
        return mPagesOfTheBook;
    }

    private void makeBook() {

        Spine spine = new Spine(mEpubBook.getTableOfContents());

        mChapterTracker = 0;
        int skipLines3 = 0;
        boolean skipLines = false;


//        boolean skipSection = false;

        for (SpineReference bookSection : spine.getSpineReferences()) {

//            Log.i("Section", "-----------------------");

//            skipSection = true;

            Resource res = bookSection.getResource();

            try {

                InputStream is = res.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String line;

                int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

                //Starts reading line for line of the epub file
                while ((line = r.readLine()) != null) {

                    line = Html.fromHtml(line).toString();

                    line = line.replace("\n", "").replace("\r", "");
                    if (line.equals("") || line.equals(" ")){
                        continue;
                    }
                    Log.i("Read it ", line);

                    if (skipLines && skipLines3 <= 3 && !line.equals("")) {
                        Log.i("Skip", "Skip = " + line);
                        skipLines3++;
                        continue;
                    }

                    //Skips the first line of the table of contents so it doesnt confuse it for a chapter
                    if (mPreviousWord.equals("Contents")) {
                        Log.i("Contents", "Line = " + line);

                        mPreviousWord = "";
                        continue;
                    }

                    /*Resets the chapter tracker when you are at the end of the book so it doesnt throw
                     arrayIndex out of bounds exception
                     */
                    if (mChapterTracker == mChapterNames.size()) {
                        mChapterTracker = 0;
                    }

                    String[] lineIntoArray = line.split("\\s+");

                    if (line.equalsIgnoreCase("Contents")) {
                        Log.i("Contents", "Line = " + line);
                        mPreviousWord = line;
                        skipLines = true;
                        continue;
                    }

                    if (ifChapterMatchBuildChapterLabel(lineIntoArray)) {
                        Log.i(TAG, line + "= Line to skip");
                        continue;
                    }

                    //prevents the chapter label from being added to the page text
                    if (mWordMatchEntireLine) {
                        mWordMatchEntireLine = false;
                        continue;
                    }

                    /*
                    If found a new chapter it should create a new mPage for the current text and
                    add the chapter label to the next mPage
                    */
                    if (mPageBreak) {
                        if (!mPage.toString().isEmpty() && mLeftOverWordsFromPrevPage.isEmpty()) {
                            addAPage(false);
                            mWordCount = 0;
                        } else {
                            if (!mLeftOverWordsFromPrevPage.isEmpty()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (String s : mLeftOverWordsFromPrevPage){
                                    stringBuilder.append(s + " ");
                                }
                                Log.i("Left Over", stringBuilder.toString());
                                addWordsToPage();
                                addAPage(false);
                                mWordCount = 0;
                            }
                        }
                        mPageBreak = false;
                        continue;
                    }


                    addWordsToPage(lineIntoArray);

                    //Finish forLoop to add the remainder of sentence to ArrayList then set Page up
                    if (mWordCount == MAX_NUMBER_OF_WORDS_PER_PAGE) {

                        if (mHaveChapterLabel) {

                            addAPage(true);

                            Log.i("After", "Page down " + mChapterLabel.toString());

                            mHaveChapterLabel = false;
                        } else {

                            if (!mPage.toString().isEmpty()) {

                                addAPage(false);
                            }
                        }
                        mWordCount = 0;
                    }
                }

                //If any left over words it will make a page for it
                if (mPage.length() != 0) {
                    addAPage(false);
                }

            } catch (IOException e) {
                Log.i("Exception", e.toString());
            }
        }
    }

    /**
     * Takes in a line of text and sees if it is part of the table of contents by checking the
     * first
     * word of the line and the first word of the chapter. If they match it continues to check the
     * rest
     * of the line and builds a chapter label if the match
     *
     * @param lineIntoArray - line to check the table of contents against
     */
    private boolean ifChapterMatchBuildChapterLabel(String[] lineIntoArray) {
        String[] currentChapterArray = mChapterNames.get(mChapterTracker).split("\\s+");

        boolean continueToNextLine = false;
        boolean wordMatch = false;
        String specialCharacters = "[\\-\\+\\.\\^:]";

        try {

            if (lineIntoArray[0].equals("THE GORILLA")){
                Log.i(TAG, "The");
            }
            //Removes special characters so it only checks the words
            if (lineIntoArray[0].replaceAll(specialCharacters, "")
                .equalsIgnoreCase(currentChapterArray[mChapterWordLocation].replaceAll(specialCharacters, ""))) {

//                Log.i("Word match", lineIntoArray[0] + " = " + currentChapterArray[0]);
                for (String aLineIntoArray : lineIntoArray) {

                    String chapterLabelWithOutSpecialCharacters =
                        mChapterLabel.toString().replaceAll(specialCharacters, "");

                    String chapterNameWithOutSpecialCharacters = mChapterNames.get(mChapterTracker)
                        .replaceAll(specialCharacters, "");

                    String wordAtIWithoutSpecialCharacters = aLineIntoArray.replaceAll(specialCharacters, "");


                    if (mChapterWordLocation == currentChapterArray.length) {
                        mChapterWordLocation = 0;
                    }

                    String currentChapterArrayWordAtChapterLengthWithoutSpecialCharacters =
                        currentChapterArray[mChapterWordLocation].replaceAll(specialCharacters, "");

                    //Remove all special characters so words don't get interfered with special characters
                    if (wordAtIWithoutSpecialCharacters
                        .equalsIgnoreCase(currentChapterArrayWordAtChapterLengthWithoutSpecialCharacters)) {

                        Log.i(TAG, "Found a match = " + aLineIntoArray);
                        mChapterWordLocation++;

                        /*If it detects a the next chapter label but the page isnt full yet because
                        the chapter is only a couple lines long it will create a page for it
                         Ex. the chapter is less than MAX_NUMBER_OF_WORDS_PER_PAGE
                         */
                        if (mHaveChapterLabel) {
                            addAPage(true);

                            mWordCount = 0;

                            mHaveChapterLabel = false;
                            mPageBreak = false;
                        }

                        mChapterLabel.append(aLineIntoArray);

                        chapterLabelWithOutSpecialCharacters =
                            mChapterLabel.toString().replaceAll("[\\-\\+\\.\\^:]", "");

                        //If the chapter label is more than one word add a space to mChapterLabel
                        if (!chapterLabelWithOutSpecialCharacters
                            .equalsIgnoreCase(chapterNameWithOutSpecialCharacters)) {
                            mChapterLabel.append(" ");
                        }

                        Log.i("Chapter label ", mChapterLabel.toString());

                        wordMatch = true;
                    } else {
                        mChapterWordLocation = 0;
                        wordMatch = false;
                        mChapterLabel = new StringBuilder();
                        break;
                    }

                    if (wordMatch && chapterLabelWithOutSpecialCharacters
                        .equalsIgnoreCase(chapterNameWithOutSpecialCharacters)) {
                        Log.i(TAG, "Chapter Label  = " + mChapterLabel.toString());

                        Log.i(TAG, "ChapterTracker = " + mChapterTracker);

//                        mChapterWordLoc = 0;
                        mChapterTracker++;

                        mHaveChapterLabel = true;

                        mChapterWordLocation = 0;

                        mPageBreak = true;
                        mWordMatchEntireLine = false;

                    }

                }
            } else {
                mChapterWordLocation = 0;

//                Log.i("Chapter", "Chapter label else = " + mChapterLabel.toString());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            continueToNextLine = true;
        }

        if (wordMatch && !mHaveChapterLabel) {
            mWordMatchEntireLine = true;
        }

        return continueToNextLine;
    }

    /**
     * Loops throught line being passed in and adds words to the page and builds a page if the page
     * is
     * full and adds the words from the previous line if the page was full
     */
    private void addWordsToPage(String[] lineIntoArray) {
        int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

        //Checks if the previous line had any leftover words before mPage break
        if (!mLeftOverWordsFromPrevPage.isEmpty()) {
            for (String wordFromLastLine : mLeftOverWordsFromPrevPage) {
                if (mWordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {
                    mPage.append(wordFromLastLine + SPACE);
                    mWordCount++;
                } else {
                    addAPage(false);
                    mWordCount = 0;
                }
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

    /**
     * Empty arraylist
     */
    private void addWordsToPage() {
        //Checks if the previous line had any leftover words before mPage break
        if (!mLeftOverWordsFromPrevPage.isEmpty()) {
            for (String wordFromLastLine : mLeftOverWordsFromPrevPage) {
                mPage.append(wordFromLastLine + SPACE);
                mWordCount++;
            }


            mLeftOverWordsFromPrevPage.clear();
        }
    }

    /**
     * Creates a page based of whether it has a chapter label
     *
     * @param haveAChapter - add a chapter label
     */
    private void addAPage(boolean haveAChapter) {
        PageOfBook newPage;

        if (haveAChapter) {
            newPage = new PageOfBook(mPage.toString(),
                mPageNumber++, mChapterLabel.toString());
            mChapterLabel = new StringBuilder();
        } else {
            newPage = new PageOfBook(mPage.toString(), mPageNumber++);
        }

        mPagesOfTheBook.add(newPage);
        mPage = new StringBuilder();

    }


    private void addPages() {
        setUpBookInfo();
        makeBook();

    }

    /**
     * Recursively Log the Table of Contents and add it to mChapterNames
     */

    private void getTableOfContents(List<TOCReference> tocReferences, int depth) {

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

            getTableOfContents(tocReference.getChildren(), depth + 1);

        }

    }
}
