package com.example.sammengistu.readtome.bookpages;

import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.models.MakeAPage;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
 * Takes an Epub file and extracts its text and converts it in to an arraylist of PageOfBook
 */
public class EPubFileConverterToBook implements MakeAPage {
    private static final String TAG = EPubFileConverterToBook.class.getName();
    public static List<PageOfBook> mPagesOfTheBook = new ArrayList<>();
    private final String SPACE = " ";
    private int mPageNumber = 0; // Keeps track of the page you are on
    private Book mEpubBook;
    private ArrayList<String> mLeftOverWordsFromPrevPage = new ArrayList<>();
    private int mWordCount = 0; //Which textview on the page its in
    private boolean mWordMatchEntireLine;

    private int mChapterTracker;

    private List<String> mChapterNames;

    private File mEpubFile;
    private Context mAppContext;

    private StringBuilder mChapterLabel = new StringBuilder();
    private StringBuilder mPage = new StringBuilder();

    private boolean mHaveChapterLabel = false;
    private int mChapterWordLocation = 0; //Tracks the location of which word in the chapter its in
    private boolean mPageBreak = false;

    private String mPreviousWord = "";

    /**
     * Takes an epub file name and finds the file and sets up the class for conversion
     * @param appContext - get context for resource name
     * @param ePubFile - file to open
     */
    public EPubFileConverterToBook(Context appContext, File ePubFile) {
        mAppContext = appContext;
        mEpubFile = ePubFile;
    }

    /**
     * Opens the book file and sets up the eBook
     * and gets the table of contents from the epub file
     */
    private void setUpBookInfo() {
        mChapterNames = new ArrayList<>();


        FileInputStream epubInputStream;

        try {
            epubInputStream = new FileInputStream(mEpubFile);

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
        int skipNextThreeLines = 0;
        boolean skipLines = false;


        for (SpineReference bookSection : spine.getSpineReferences()) {

            Resource res = bookSection.getResource();

            try {

                InputStream is = res.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String line;

                int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;

                Log.i("Section", mPageNumber + "");

                //Starts reading line for line from the epub file
                while ((line = r.readLine()) != null) {

                    String lineInHtml = line;
                    line = Html.fromHtml(line).toString();

                    line = line.replace("\n", "").replace("\r", "");

                    if (line.equals("") || line.equals(" ")) {
                        continue;
                    }

                    //Skip the next couple of lines of the books table of content
                    //So it doesn't get confused for it being a chapter label
                    if (skipLines && skipNextThreeLines <= 3 && !line.equals("")) {
                        skipNextThreeLines++;
                        continue;
                    }

                    //Skips the table of contents in the epub file
                    if (mPreviousWord.equals(mAppContext.getString(R.string.contents))) {
                        mPreviousWord = "";
                        continue;
                    }

                    if (line.equalsIgnoreCase(mAppContext.getString(R.string.contents))) {
                        mPreviousWord = line;
                        skipLines = true;
                        continue;
                    }

                    String[] lineIntoArray = line.split("\\s+");

                     /*
                    Resets the chapter tracker when you are at the end of the book so it doesnt throw
                     arrayIndex out of bounds exception
                     */
                    if (mChapterTracker != mChapterNames.size()) {
                        if (ifChapterMatchBuildChapterLabel(lineIntoArray)) {
                             continue;
                        }
                    }

                    //Prevents the chapter label from being added to the page text
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

                        } else {
                            if (!mLeftOverWordsFromPrevPage.isEmpty()) {
                                addWordsToPage();
                                addAPage(false);
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
                            mHaveChapterLabel = false;
                        } else {

                            if (!mPage.toString().isEmpty()) {

                                addAPage(false);
                            }
                        }
                    }
                }

                //If any left over words it will make a page for it
                if (mPage.length() != 0) {
                    addAPage(false);
                }
                if (!mLeftOverWordsFromPrevPage.isEmpty()){
                    addWordsToPage();
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

            //Removes special characters so it only checks the words
            if (lineIntoArray[0].replaceAll(specialCharacters, "")
                .equalsIgnoreCase(currentChapterArray[mChapterWordLocation].replaceAll(specialCharacters, ""))) {

                for (String aLineIntoArray : lineIntoArray) {

                    String chapterLabelWithOutSpecialCharacters;

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

                        mChapterWordLocation++;

                        /*If it detects a the next chapter label but the page isnt full yet because
                        the chapter is only a couple lines long it will create a page for it
                         Ex. the chapter is less than MAX_NUMBER_OF_WORDS_PER_PAGE
                         */
                        if (mHaveChapterLabel) {
                            addAPage(true);

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

                        wordMatch = true;
                    } else {
                        mChapterWordLocation = 0;
                        wordMatch = false;
                        mChapterLabel = new StringBuilder();
                        break;
                    }

                    if (wordMatch && chapterLabelWithOutSpecialCharacters
                        .equalsIgnoreCase(chapterNameWithOutSpecialCharacters)) {

                        mChapterTracker++;

                        mHaveChapterLabel = true;

                        mChapterWordLocation = 0;

                        mPageBreak = true;
                        mWordMatchEntireLine = false;
                    }
                }
            } else {
                mChapterWordLocation = 0;

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

       addWordsToPage();

        //Adds words from line to the mPage and the left over words to the arrayList
        for (String aWordFromArray : lineIntoArray) {

            if (mWordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {

                String wordFromArrayPlusSpace = aWordFromArray + SPACE;
                mPage.append(wordFromArrayPlusSpace);
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
        int MAX_NUMBER_OF_WORDS_PER_PAGE = 184;
        //Checks if the previous line had any leftover words before mPage break
        if (!mLeftOverWordsFromPrevPage.isEmpty()) {
            for (String wordFromLastLine : mLeftOverWordsFromPrevPage) {
                if (mWordCount < MAX_NUMBER_OF_WORDS_PER_PAGE) {
                    String wordFromLastLinePlusSpace = wordFromLastLine + SPACE;
                    mPage.append(wordFromLastLinePlusSpace);
                    mWordCount++;
                } else {
                    addAPage(false);
                }
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
        mWordCount = 0;

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

            if (!tocString.toString().equalsIgnoreCase(mAppContext.getString(R.string.contents))) {

                mChapterNames.add(tocString.toString());
            }

            getTableOfContents(tocReference.getChildren(), depth + 1);

        }
    }
}
