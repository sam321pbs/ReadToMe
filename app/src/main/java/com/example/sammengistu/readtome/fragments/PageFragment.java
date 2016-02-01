package com.example.sammengistu.readtome.fragments;


import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.ReadToMeJSONSerializer;
import com.example.sammengistu.readtome.WordLinkedWithDef;
import com.example.sammengistu.readtome.WordPlayer;
import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.GetBookInfo;
import com.example.sammengistu.readtome.models.Library;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


/**
 * Sets up the page view of the book
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    private static final String TAG = "PageFragment";
    public static final String ERROR_MESSAGE = "error message";
    private static final int GET_SETTINGS = 3;
    private static final int GET_PAGE_NUMBER = 4;
    private final String VOICE_SPEED = "+voice speed+";
    private final String SPEECH_MODE = "+Speech mode+";
    private final int TRUE_SPEECH_MODE = 999999;

    private static final String FILENAME = "readtome.json";
    private static final int GET_CHAPTER_NUMBER = 2;

    private List<PageOfBook> mPagesOfBook;
    private List<TableLayout> mTableLayouts;
    private List<TextView> mHighlightedTextViews;
    private List<TextView> mAllTextViews;
    private List<WordLinkedWithDef> mDictionaryOne;
    private List<String> mChaptersOfTheBookName;
    private List<Integer> mChaptersOfTheBookPageNum;

    public static TextView mPageNumberTextView;
    private ImageView mBookmark;
    private String[] mPageWordBank;
    public static int mPageNumber;
    public static TextView mChapterTextView;
    private TextToSpeech mTts;
    private WordPlayer mWordPlayer;
    private boolean mOnClickHighLightSentenceMode;
    private int mVoiceSpeed;
    private static int sPlayOrStopCounter;
    private ReadToMeJSONSerializer mReadToMeJSONSerializer;
    private boolean mDictionaryReady;
    private Dictionary1Loader mDictionaryLoader;
    private Book mCurrentBook;
    private SetUpBookAsync mSetUpBookAsync;
    private String mAuthor;
    private String mTitle;
    private Map<String, Object> mAllBookMarksAndSettings;
    private int mBookMarkPageNumber;
    private WordLinkedWithDef mWordLinkedWithDef;

    public ImageView mPlayButton;

    private ProgressDialog mProgressDialogSettingUpBook = null;

    @Override
    public void onCreate(Bundle savedInstnaceState) {
        super.onCreate(savedInstnaceState);
        setHasOptionsMenu(true);

        mWordLinkedWithDef = new WordLinkedWithDef();

        mReadToMeJSONSerializer = new ReadToMeJSONSerializer(getActivity(), FILENAME);

        loadUpSettings();
        instantiateLists();

        if (mAllBookMarksAndSettings.containsKey(VOICE_SPEED)) {
            mVoiceSpeed = (Integer) mAllBookMarksAndSettings.get(VOICE_SPEED);
        } else {
            mVoiceSpeed = 20;
        }

        if (mAllBookMarksAndSettings.containsKey(SPEECH_MODE)) {
            if (mAllBookMarksAndSettings.get(SPEECH_MODE) instanceof Integer) {

                int speechMode = (Integer) mAllBookMarksAndSettings.get(SPEECH_MODE);
                mOnClickHighLightSentenceMode = speechMode == TRUE_SPEECH_MODE;
            }
        } else {
            mOnClickHighLightSentenceMode = true;
        }

        sPlayOrStopCounter = 0; // Will change if it is on play mode or stop mode

        mSetUpBookAsync = new SetUpBookAsync();

        mSetUpBookAsync.execute();

        mWordPlayer = new WordPlayer(getActivity(), getActivity(),
            mVoiceSpeed, mAllTextViews);

        mTts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = mTts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Initilization Failed!");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    /**
     * Decides whether the page has a picture or not
     * then adds the words to the page in individual text text views
     */
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View blankPage = inflater.inflate(R.layout.page_without_image_fragment, container, false);
        mChapterTextView = (TextView) blankPage.findViewById(R.id.book_chapter_textView);

        mPageNumberTextView = (TextView) blankPage.findViewById(R.id.action_command_page_number);


        setTableLayouts(blankPage);

        ImageView turnPage = (ImageView) blankPage.findViewById(R.id.turn_page);
        turnPage.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {

                                            mPageNumber++;
                                            if (mPageNumber > mPagesOfBook.size() - 1) {
                                                mPageNumber = mPagesOfBook.size() - 1;
                                            }
                                            handlePageTurn();
                                            mHighlightedTextViews.clear();

                                        }
                                    }

        );

        mBookmark = (ImageView) blankPage.findViewById(R.id.page_without_picture_bookmark);
        handleBookmark();

        ImageView goBackPage = (ImageView) blankPage.findViewById(R.id.go_back);
        goBackPage.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              mPageNumber--;
                                              if (mPageNumber < -1) {
                                                  mPageNumber = -1;
                                              }
                                              handlePageTurn();
                                              mHighlightedTextViews.clear();
                                          }
                                      }

        );

        mPlayButton = (ImageView) blankPage.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {

                                               sPlayOrStopCounter++;

                                               if (sPlayOrStopCounter == 1) {
                                                   mWordPlayer.setPlay(true);

                                                   mPlayButton.setImageResource(R.drawable.added_stop_button);
                                                   mWordPlayer.setVoiceSpeed(mVoiceSpeed);

                                                   findHighlightedWords();
                                                   if (mOnClickHighLightSentenceMode) {
                                                       mWordPlayer.playSentenceBySentence(
                                                           mHighlightedTextViews, mPlayButton);
                                                   } else {
                                                       mWordPlayer.play(
                                                           mHighlightedTextViews, mPlayButton);
                                                   }
                                               } else {

                                                   stopReadingAndResetPlayButton();
                                               }

                                               mHighlightedTextViews.clear();
                                           }
                                       }

        );

        mPlayButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                sPlayOrStopCounter++;

                if (sPlayOrStopCounter == 1 && mPageNumber != -1) {

                    highlightThePage();

                    mWordPlayer.setPlay(true);

                    mPlayButton.setImageResource(R.drawable.added_stop_button);
                    mWordPlayer.setVoiceSpeed(mVoiceSpeed);

                    List<PageOfBook> pageOfBooksTillEndOfChapter = new ArrayList<>();

                    boolean firstLoop = true;


                    for (int currentPageOfBook = mPageNumber;
                         currentPageOfBook < mPagesOfBook.size(); currentPageOfBook++) {

                        if (mPagesOfBook.get(currentPageOfBook).getChapterOfBook()
                            .equals(PageOfBook.PAGE_HAS_NO_CHAPTER) || firstLoop) {

                            pageOfBooksTillEndOfChapter.add(mPagesOfBook.get(currentPageOfBook));
                        } else {
                            break;
                        }

                        firstLoop = false;

                    }


                    findHighlightedWords();

                    mWordPlayer.playChapter(mHighlightedTextViews, mPlayButton,
                        pageOfBooksTillEndOfChapter,
//                        mAllTextViews,
                        mChapterTextView,
                        mPageNumberTextView);

                } else {

                    stopReadingAndResetPlayButton();
                }

                mHighlightedTextViews.clear();

                return true;
            }
        });


        ImageView highlightPage = (ImageView) blankPage.findViewById(R.id.page_button);
        highlightPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                highlightThePage();

            }
        });

        ImageView highlightSentence = (ImageView) blankPage.findViewById(R.id.sentence_button);
        highlightSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightSentence();
            }
        });

        ImageView clearHighlights = (ImageView) blankPage.findViewById(R.id.clear_highlights_button);
        clearHighlights.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   setUpPageText();
                                                   mHighlightedTextViews.clear();
                                               }

                                           }
        );

        addAllTextViewsToList();

        return blankPage;
    }

    public static int updatePageNumber() {
       return ++mPageNumber;
    }

    /**
     * Loads up the book that was selected
     * If there is an error it will send you back to the library
     */
    private void setUpBook() {
        UUID bookId = (UUID) getActivity().getIntent().getSerializableExtra(MyLibraryFragment.BOOK_ID);

        mCurrentBook = Library.get(getActivity()).getBook(bookId);

        mPagesOfBook = mCurrentBook.getPagesOfBook();

        try {
            mPageNumber = (Integer) mAllBookMarksAndSettings
                .get(mCurrentBook.getEPubFile().getName());

        } catch (NullPointerException e) {
            mPageNumber = -1;
        }

        mBookMarkPageNumber = mPageNumber;

        try {
            Log.i("Pages", "Page of book = " + mPagesOfBook.size());
            if (mPageNumber == -1) {
                mPageWordBank = mPagesOfBook.get(0).getPageText().split("\\s+");
            } else {
                mPageWordBank = mPagesOfBook.get(mPageNumber).getPageText().split("\\s+");
            }
        } catch (IndexOutOfBoundsException e) {

            e.printStackTrace();

            stopAsyncTasks();

            Intent intent = new Intent(getActivity(), MyLibraryActivity.class);
            intent.putExtra(MyLibraryFragment.LIBRARY_PAGE_NUMBER,
                getActivity().getIntent().getIntExtra(MyLibraryFragment.LIBRARY_PAGE_NUMBER, 0));
            intent.putExtra(ERROR_MESSAGE, true);
            startActivity(intent);
        }

        setUpChapters();

        getTitleAndAuthor();
    }

    private void loadDictionary() {
        mDictionaryLoader = new Dictionary1Loader();
        mDictionaryLoader.execute();
    }

    /**
     * Goes through all the pages of the book and decides whether the page is a chapter and if it
     * is, it saves the page number to be used in the chapter dialog
     */
    private void setUpChapters() {

        for (PageOfBook pageOfBook : mPagesOfBook) {
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)) {
                mChaptersOfTheBookName.add(pageOfBook.getChapterOfBook());
                mChaptersOfTheBookPageNum.add(pageOfBook.getPageNumber());

            }
        }
    }

    private void instantiateLists() {
        mAllTextViews = new ArrayList<>();
        mTableLayouts = new ArrayList<>();
        mHighlightedTextViews = new ArrayList<>();
        mChaptersOfTheBookName = new ArrayList<>();
        mChaptersOfTheBookPageNum = new ArrayList<>();
    }

    private void loadUpSettings() {

        try {
            mAllBookMarksAndSettings = mReadToMeJSONSerializer.loadBookMarks();
        } catch (Exception e) {
            mAllBookMarksAndSettings = new HashMap<>();
        }
    }

    private void stopReadingAndResetPlayButton() {

        mWordPlayer.stopTtsVoice();
        mWordPlayer.setPlay(false);
        mPlayButton.setImageResource(R.drawable.play_button_updated);
        sPlayOrStopCounter = 0;
    }

    public static void setPlayOrStopCounter(int playOrStopCounters) {
        sPlayOrStopCounter = playOrStopCounters;
    }

    /**
     * Highlights a sentence based on the location of the textView that was selected
     *
     * @param viewThatWillBeHighlighted - specific textView that was clicked
     */
    private void highlightSentenceMode(TextView viewThatWillBeHighlighted, int color) {

        int tableLayoutHolder = 0;
        int tableRowHolderForHighlightingToEndOfSent = 0; //Row to end the highlighting
        int tableRowHolderForHighlightingToBeginningOfSent = 0; //Row to begin highlighting

        for (int i = 0; i < mTableLayouts.size(); i++) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView wordView = (TextView) row.getChildAt(j);
                if (viewThatWillBeHighlighted.equals(wordView)) {
                    //Get the positions
                    tableLayoutHolder = i;
                    tableRowHolderForHighlightingToEndOfSent = j;
                    tableRowHolderForHighlightingToBeginningOfSent = j;
                }
            }
        }

        //Loop through the sentence and start highlighting
        highlightSentenceLoops(tableLayoutHolder,
            tableRowHolderForHighlightingToEndOfSent,
            tableRowHolderForHighlightingToBeginningOfSent,
            color);

    }

    /**
     * Highlights a sentence. By finding where all the words that were highlighted and
     * makes a call to highlightSentenceMode.
     */
    private void highlightSentence() {

        //Checks to see that only one word is highlighted and sets up its location
        for (int i = 0; i < mTableLayouts.size(); i++) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {

                TextView word = (TextView) row.getChildAt(j);
                ColorDrawable textBackGroundColor = (ColorDrawable) word.getBackground();
                int backgroundColor = textBackGroundColor.getColor();

                if (backgroundColor == Color.YELLOW) {
                    highlightSentenceMode(word, Color.YELLOW);
                }
            }
        }
    }

    /**
     * Given the parameters it will know where to start highlighting and stop highlighting
     *
     * @param tableLayoutHolder                              - the table layout were to begin
     *                                                       highlighting
     * @param tableRowHolderForHighlightingToEndOfSent       - where to stop highlighting in
     *                                                       sentence
     * @param tableRowHolderForHighlightingToBeginningOfSent - where to start highlighting in
     *                                                       sentence
     * @param color                                          - color to highlight
     */
    private void highlightSentenceLoops(int tableLayoutHolder,
                                        int tableRowHolderForHighlightingToEndOfSent,
                                        int tableRowHolderForHighlightingToBeginningOfSent,
                                        int color) {
        boolean end = false;

        for (int i = tableLayoutHolder; i < mTableLayouts.size(); i++) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = tableRowHolderForHighlightingToEndOfSent; j < row.getChildCount(); j++) {

                // when the row changes it starts highlighting at beginning of the row
                tableRowHolderForHighlightingToEndOfSent = 0;
                TextView wordView = (TextView) row.getChildAt(j);
                String wordFromView = wordView.getText().toString();

                // If the word box is NOT empty it will highlight it
                if (!(wordFromView.isEmpty())) {
                    wordView.setBackgroundColor(color);
                }

                if (endOfSentence(wordFromView) && !isItAFamilyName(wordFromView)) {
                    // break out of the second loop
                    end = true;
                    break;
                }
            }
            if (end) {
                break;
            }
        }

        boolean breakPoint = false;
        int skipCurrentWord = 1;

        for (int i = tableLayoutHolder; i >= 0; i--) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = tableRowHolderForHighlightingToBeginningOfSent - skipCurrentWord;
                 j >= 0; j--) {

                TextView word = (TextView) row.getChildAt(j);
                String textWord = word.getText().toString();

                if (isItAFamilyName(textWord)) {
                    word.setBackgroundColor(color);
                    continue;
                }

                if (!endOfSentence(textWord)) {

                    if (!(textWord.isEmpty())) {
                        word.setBackgroundColor(color);
                    }

                } else {
                    breakPoint = true;
                    break;
                }
            }

            if (breakPoint) {
                break;
            } else {
                tableRowHolderForHighlightingToBeginningOfSent = row.getChildCount() - 1;
            }

            skipCurrentWord = 0;
        }
    }

    public static boolean endOfSentence(String textWord) {
        return textWord.contains(".") || textWord.contains("!")
            || textWord.contains("?") || textWord.contains(":");

    }

    public static boolean isItAFamilyName(String word) {

        return word.equals("Mrs.") ||
            word.equals("Mr.") ||
            word.equals("Ms.");
    }

    private void highlightThePage() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                if (!word.getText().equals("")) {
                    word.setBackgroundColor(Color.YELLOW);
                }
            }
        }
    }

    private void addAllTextViewsToList (){
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                mAllTextViews.add(word);
            }
        }
    }

    private void handleBookmark() {
        if (mPageNumber == mBookMarkPageNumber) {
            mBookmark.setVisibility(View.VISIBLE);
        } else {
            mBookmark.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Handles the page turning
     * Sets up the views according to the content
     */
    private void handlePageTurn() {

        handleBookmark();

        setUpPageText();
        setUpChapterLabel();
    }

    /**
     * Sets up the chapter view based on whether the book has
     * chapters
     */
    private void setUpChapterLabel() {
        if (mPageNumber != -1) {
            if (!mPagesOfBook.get(mPageNumber).getChapterOfBook().equals(
                PageOfBook.PAGE_HAS_NO_CHAPTER)) {

                mChapterTextView.setVisibility(View.VISIBLE);
                String chapterLabel = mPagesOfBook.get(mPageNumber).getChapterOfBook();
                if (chapterLabel.length() > 60) {
                    chapterLabel = chapterLabel.substring(0, 59) + "...";
                }
                mChapterTextView.setText(chapterLabel);
                mChapterTextView.setTextColor(Color.BLACK);
            } else {
                mChapterTextView.setVisibility(View.INVISIBLE);
                mChapterTextView.setText("");
            }
        }
    }


    /**
     * Sets up the table layouts in side the view
     * Adds the extra layouts to the table views for the Charolottes web book
     */
    private void setTableLayouts(View view) {
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout1));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout2));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout3));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout4));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout5));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout6));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout7));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout8));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout9));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout10));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout11));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout12));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout13));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout14));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout15));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout16));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout17));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout18));

        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout19));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout20));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout21));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout22));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout23));
    }

    private View.OnLongClickListener onLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (v instanceof TextView) {
                    TextView textView = (TextView) v;

                    if (!textView.getText().equals("")) {
                        showDictionaryDialog((TextView) v);
                    }
                }

                return true;
            }
        };
    }


    /**
     * Sets up the page text by first breaking the texts into individual strings
     * Then goes through the tableLayouts and fills them with the text of the book
     */
    private void setUpPageText() {
        if (mPageNumber != -1) {
            mPageWordBank = mPagesOfBook.get(mPageNumber).getPageText().split("\\s+");
            String pageNumberText = mPageNumber + "";
            mPageNumberTextView.setText(pageNumberText);
            mPageNumberTextView.setTextColor(Color.BLACK);
            mPageNumberTextView.setVisibility(View.VISIBLE);
        } else {
            mPageNumberTextView.setVisibility(View.INVISIBLE);
        }

        cleanUpPageText(Color.WHITE);

        //used to stop printing words on the screen because it is the end of the view
        int placeHolder = 0;

        for (TableLayout tableLayout : mTableLayouts) {
            //sets up title page
            if ((mPageNumber == -1)) {
                setupTitlePage();

            } else {
                TableRow row = (TableRow) tableLayout.getChildAt(0);
                for (int j = 0; j < row.getChildCount(); j++) {

                    if (mPageWordBank.length != placeHolder) {
                        TextView word = (TextView) row.getChildAt(j);
                        word.setText(mPageWordBank[placeHolder]);
                        word.setTextSize(20f);
                        word.setTextColor(Color.BLACK);
                        word.setOnClickListener(onClick());
                        word.setOnLongClickListener(onLongClick());
                        placeHolder++;

                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void getTitleAndAuthor() {
        mTitle = GetBookInfo.getBookTitle(
            mCurrentBook.getEPubFile());

        mAuthor = GetBookInfo.getBookAuthor(
            mCurrentBook.getEPubFile());
    }

    /**
     * This is used to set up the title page/page zero
     */
    public void setupTitlePage() {
        String pageNumberForView = mPageNumber + "";
        mPageNumberTextView.setText(pageNumberForView);
        mPageNumberTextView.setTextColor(Color.BLACK);

        cleanUpPageText(Color.WHITE);

        for (int i = 0; i < mTableLayouts.size(); i++) {
            TableLayout tableLayout = mTableLayouts.get(i);
            //sets up title page

            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                if (i == 8 && j == 4) {
                    TextView textView = (TextView) row.getChildAt(j);

                    textView.setText(mTitle);
                }

                if (i == 9 && j == 4) {

                    TextView textView = (TextView) row.getChildAt(j);

                    textView.setText(R.string.first_page_by);
                }
                if (i == 10 && j == 4) {

                    TextView textView = (TextView) row.getChildAt(j);

                    textView.setText(mAuthor.substring(1, mAuthor.length() - 2));

                }
            }
        }
    }


    /**
     * If the dictionary is ready it will let you get the definition
     */
    private void showDictionaryDialog(TextView currentWordTextView) {
        if (mDictionaryReady) {
            String newWord = DefinitionDialog.removePunctuations(currentWordTextView.getText()
                .toString().replaceAll("\\s+", ""));

            WordLinkedWithDef findDef = mWordLinkedWithDef.findDefinition(
                mDictionaryOne,
                DefinitionDialog.removePunctuations(newWord.toLowerCase()));


            DefinitionDialog dialog = DefinitionDialog.newInstance(

                newWord, findDef.getDefinition()
            );

            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, DefinitionDialog.DEFINITION);

        } else {
            Toast.makeText(getActivity(),
                R.string.dictionary_being_set_up, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets the onClickListener for the texts
     * Changes the color of the background onClick
     */
    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                ColorDrawable textBackGroundColor = (ColorDrawable) v.getBackground();
                int backgroundColor = textBackGroundColor.getColor();

                if (mOnClickHighLightSentenceMode) {
                    if (backgroundColor == Color.WHITE) {
                        highlightSentenceMode((TextView) v, Color.YELLOW);
                    } else {
                        highlightSentenceMode((TextView) v, Color.WHITE);
                    }
                } else {
                    if (!((TextView) v).getText().equals("")) {
                        if (backgroundColor == Color.YELLOW) {
                            textView.setBackgroundColor(Color.WHITE);
                        } else {
                            textView.setBackgroundColor(Color.YELLOW);
                        }
                    }
                }
            }
        };
    }


    /**
     * Turns all the highlighted text into a white background
     */
    private void cleanUpPageText(int backgroundColor) {
        stopReadingAndResetPlayButton();

        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                word.setText("");
                word.setBackgroundColor(backgroundColor);
            }
        }
    }

    /**
     * Finds all the highlighted text on the screen and stores it into an arrayList
     */
    private void findHighlightedWords() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                ColorDrawable textBackGroundColor = (ColorDrawable) word.getBackground();
                int backgroundColor = textBackGroundColor.getColor();
                if (backgroundColor == Color.YELLOW) {
                    mHighlightedTextViews.add(word);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_SETTINGS || requestCode == Activity.RESULT_OK) {

            mVoiceSpeed = data.getIntExtra(SettingsDialog.VOICE_SPEED, 20);

            mOnClickHighLightSentenceMode = data.getBooleanExtra(
                SettingsDialog.SENTENCE_BY_SENTENCE_MODE, true);

            mAllBookMarksAndSettings.put(VOICE_SPEED, mVoiceSpeed);

            if (mOnClickHighLightSentenceMode) {
                mAllBookMarksAndSettings.put(SPEECH_MODE, TRUE_SPEECH_MODE);
            } else {
                int FALSE_SPEECH_MODE = 88888;
                mAllBookMarksAndSettings.put(SPEECH_MODE, FALSE_SPEECH_MODE);
            }

            saveSettings();
            Toast.makeText(getActivity(), R.string.settings_saved
                , Toast.LENGTH_LONG).show();

        }
        if (requestCode == GET_PAGE_NUMBER) {

            mPageNumber = data.getIntExtra(SelectPageDialog.SELECTED_PAGE, 0);
            setUpPageText();
            setUpChapterLabel();
            handleBookmark();
        }

        if (requestCode == GET_CHAPTER_NUMBER) {

            mPageNumber = data.getIntExtra(ChaptersDialog.SELECTED_CHAPTER, 0);
            setUpPageText();
            setUpChapterLabel();
            handleBookmark();
        }
    }

    /**
     * Loads/sets the dictionary up in a background thread
     */
    private class Dictionary1Loader extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread

            mDictionaryReady = false;
            Toast.makeText(getActivity(),
                R.string.dictionary_being_set_up_first, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you dont want
            // to pass argument and u can access the parent class' variable url over here

            mDictionaryOne = mWordLinkedWithDef.linkWordsWithDefinitions(getActivity(), 0, 4677);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mDictionaryReady = true;

            //this method will be running on UI thread
            Toast.makeText(getActivity(), R.string.dictionary_ready, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pages, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.menu_bookmark_page:
                if (mBookmark.getVisibility() == View.INVISIBLE) {
                    mBookmark.setVisibility(View.VISIBLE);

                    mBookMarkPageNumber = mPageNumber;
                    mAllBookMarksAndSettings.put(mCurrentBook.getEPubFile().getName(), mPageNumber);
                    saveSettings();
                } else {
                    mBookmark.setVisibility(View.INVISIBLE);
                }

                return true;

            case R.id.menu_setting:
                SettingsDialog dialog = SettingsDialog.newInstance(
                    mVoiceSpeed, mOnClickHighLightSentenceMode);
                dialog.setTargetFragment(PageFragment.this, GET_SETTINGS);

                dialog.show(fm, SettingsDialog.SETTINGS);

                return true;

            case R.id.menu_select_page:
                SelectPageDialog newPageDialog = SelectPageDialog.newInstance(mPageNumber, mPagesOfBook.size());
                newPageDialog.setTargetFragment(PageFragment.this, GET_PAGE_NUMBER);

                newPageDialog.show(fm, SelectPageDialog.SELECT_PAGE);

                return true;

            case R.id.menu_select_chapter:
                ChaptersDialog chaptersDialog = ChaptersDialog.newInstance(mChaptersOfTheBookName,
                    mChaptersOfTheBookPageNum);
                chaptersDialog.setTargetFragment(PageFragment.this, GET_CHAPTER_NUMBER);

                chaptersDialog.show(fm, ChaptersDialog.SELECT_CHAPTER);

                return true;

            case R.id.menu_help:
                HelpDialog helpDialog = new HelpDialog();
                helpDialog.show(fm, TAG);

                return true;

            case R.id.menu_library:
                mChaptersOfTheBookName.clear();
                mChaptersOfTheBookPageNum.clear();
                mTableLayouts.clear();
                mHighlightedTextViews.clear();
                mPagesOfBook.clear();

                mWordLinkedWithDef.setmStopLoop(true);

                stopAsyncTasks();

                Intent intent = new Intent(getActivity(), MyLibraryActivity.class);
                intent.putExtra(MyLibraryFragment.LIBRARY_PAGE_NUMBER,
                    getActivity().getIntent().getIntExtra(MyLibraryFragment.LIBRARY_PAGE_NUMBER, 0));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean saveSettings() {
        try {
            mReadToMeJSONSerializer.saveBookMarks(mAllBookMarksAndSettings);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void stopAsyncTasks() {

        if (mDictionaryLoader != null) {
            if (mDictionaryLoader.getStatus().equals(AsyncTask.Status.RUNNING)) {
                mDictionaryLoader.cancel(true);
            }
        }

        if (mSetUpBookAsync.getStatus().equals(AsyncTask.Status.RUNNING)) {
            mSetUpBookAsync.cancel(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
        stopAsyncTasks();
        stopReadingAndResetPlayButton();
    }

    @Override
    public void onDestroy() {
        mTts.shutdown();
        stopAsyncTasks();
        mWordPlayer.shutDownTTS();
        super.onDestroy();
    }

    /**
     * Loads the book and when its ready it displays it to the user
     */
    private class SetUpBookAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialogSettingUpBook != null) {
                mProgressDialogSettingUpBook.dismiss();
            }

            setUpChapterLabel();
            setUpPageText();

            handlePageTurn();

            loadDictionary();
        }

        @Override
        protected String doInBackground(Void... params) {
            setUpBook();
            return "";
        }

        @Override
        protected void onPreExecute() {
            // Show the ProgressDialog on this thread
            mProgressDialogSettingUpBook = ProgressDialog.show(
                getActivity(), getActivity().getString(R.string.progress_setting_up_title),
                getActivity().getString(R.string.progress_setting_up_message), true, false);
        }
    }
}