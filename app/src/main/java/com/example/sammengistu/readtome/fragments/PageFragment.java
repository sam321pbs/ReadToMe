package com.example.sammengistu.readtome.fragments;


import android.app.Activity;
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

import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.ReadToMeJSONSerializer;
import com.example.sammengistu.readtome.SettingsPreferences;
import com.example.sammengistu.readtome.WordLinkedWithDef;
import com.example.sammengistu.readtome.WordPlayer;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;


/**
 * Sets up the page view of the book
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    private static final String TAG = "PageFragment";
    private static final int PAGE = 1;
    private static final int GET_SETTINGS = 3;
    private static final int GET_PAGE_NUMBER = 4;

    private static final String FILENAME = "readtome.json";

    private ArrayList<PageOfBook> mPagesOfBook;
    private TextView mPageNumber;
    private ImageView mBookmark;
    private String[] mPageWordBank;
    int pageNumber;
    private ArrayList<TableLayout> mTableLayouts;
    private ArrayList<String> mWordsToSpeechBank;
    private Book currentBook;
    private TextView mChapterTextView;
    private ArrayList<TextView> mHighlightedTextViews;
    private TextToSpeech mTts;
    private WordPlayer mWordPlayer;
    private boolean mOnClickHighLightSentenceMode;
    private int voiceSpeed;
    private static int playOrStopCounter;
    public ImageView playButton;
    private SettingsPreferences mSettingsPreferences;
    private ReadToMeJSONSerializer mReadToMeJSONSerializer;

    private boolean dictionaryReady;

    private ArrayList<WordLinkedWithDef> mDictionaryOne;

    private Dictionary1Loader mDictionaryLoader;

    @Override
    public void onCreate(Bundle savedInstnaceState) {
        super.onCreate(savedInstnaceState);
        setHasOptionsMenu(true);


        mDictionaryLoader = new Dictionary1Loader();
        mDictionaryLoader.execute();


        mReadToMeJSONSerializer = new ReadToMeJSONSerializer(getActivity(), FILENAME);


        try {
            mSettingsPreferences = mReadToMeJSONSerializer.loadSettings();
            Log.i(TAG, "Success loading");
        } catch (Exception e) {

            Log.i(TAG, e.getMessage());
            Log.i(TAG, " Error loading");
            mSettingsPreferences = new SettingsPreferences();
        }


        voiceSpeed = mSettingsPreferences.getVoiceSpeed();
        mOnClickHighLightSentenceMode = mSettingsPreferences.isReadSentenceMode();

        playOrStopCounter = 0;

        mTableLayouts = new ArrayList<TableLayout>();
        mWordsToSpeechBank = new ArrayList<String>();
        mHighlightedTextViews = new ArrayList<>();

        UUID bookId = (UUID) getActivity().getIntent().getSerializableExtra(MyLibraryFragment.BOOK_ID);

        currentBook = Library.get(getActivity()).getBook(bookId);

        mPagesOfBook = currentBook.getPagesOfBook();

        pageNumber = mSettingsPreferences.getBookMarkedPage();

        mWordPlayer = new WordPlayer(getActivity(), getActivity(), mOnClickHighLightSentenceMode,
                voiceSpeed);

        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

        mTts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = mTts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
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
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        dictionaryReady = false;

        Log.i(TAG, mSettingsPreferences.getBookMarkedPage() + " - Bookmarked page  " +
                mSettingsPreferences.isReadSentenceMode() + " - readmode   " +
                mSettingsPreferences.getVoiceSpeed() + " - voice speed");

        View blankPage = inflater.inflate(R.layout.page_without_image_fragment, container, false);
        mChapterTextView = (TextView) blankPage.findViewById(R.id.book_chapter_textView);
        setUpChapterLabel();

        setTableLayouts(blankPage);

        ImageView turnPage = (ImageView) blankPage.findViewById(R.id.turn_page);
        turnPage.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            pageNumber++;
                                            if (pageNumber > mPagesOfBook.size() - 1) {
                                                pageNumber = mPagesOfBook.size() - 1;
                                            }
                                            handlePageTurn();
                                            mWordsToSpeechBank.clear();
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
                                              pageNumber--;
                                              if (pageNumber < 0) {
                                                  pageNumber = 0;
                                              }
                                              handlePageTurn();
                                              mWordsToSpeechBank.clear();
                                              mHighlightedTextViews.clear();
                                          }
                                      }

        );

        playButton = (ImageView) blankPage.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {

                                              playOrStopCounter++;

                                              if (playOrStopCounter == 1) {
                                                  mWordPlayer.setPlay(true);

                                                  playButton.setImageResource(R.drawable.added_stop_button);
                                                  mWordPlayer.setVoiceSpeed(voiceSpeed);

                                                  findHighlightedWords();
                                                  if (mOnClickHighLightSentenceMode) {
                                                      mWordPlayer.playSentenceBySentence(mWordsToSpeechBank,
                                                              mHighlightedTextViews, playButton);
                                                  } else {
                                                      mWordPlayer.play(mWordsToSpeechBank,
                                                              mHighlightedTextViews, playButton);
                                                  }
                                              } else {

                                                  stopVoiceAndResetPlayButton();
                                              }

                                              mWordsToSpeechBank.clear();
                                              mHighlightedTextViews.clear();

                                          }
                                      }

        );

        mPageNumber = (TextView) blankPage.findViewById(R.id.action_command_page_number);

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
                                                   mWordsToSpeechBank.clear();
                                                   mHighlightedTextViews.clear();
                                               }

                                           }
        );

        setUpPageText();

        return blankPage;
    }

    private void stopVoiceAndResetPlayButton() {

        mWordPlayer.stopTtsVoice();
        mWordPlayer.setPlay(false);
        playButton.setImageResource(R.drawable.play_button_updated);
        playOrStopCounter = 0;
    }

    public static void setPlayOrStopCounter(int playOrStopCounters) {
        playOrStopCounter = playOrStopCounters;
    }

    /**
     * Hightlights a sentence based on the location of the textView that was selected
     *
     * @param v - specific textView that was clicked
     */
    private void highlightSentenceMode(TextView v, int color) {

        int tableLayoutHolder = 0;
        int tableRowHolderForHighlightingToEndOfSent = 0;
        int tableRowHolderForHighlightingToBeginingOfSent = 0;

        for (int i = 0; i < mTableLayouts.size(); i++) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                if (v.equals(word)) {
                    tableLayoutHolder = i;
                    tableRowHolderForHighlightingToEndOfSent = j;
                    tableRowHolderForHighlightingToBeginingOfSent = j;
                }
            }
        }

        highlightSentenceLoops(tableLayoutHolder,
                tableRowHolderForHighlightingToEndOfSent,
                tableRowHolderForHighlightingToBeginingOfSent, color);

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
     * Given the parameters it will know where to start highlightings and stop highlighting
     * @param tableLayoutHolder
     * @param tableRowHolderForHighlightingToEndOfSent - where to
     * @param tableRowHolderForHighlightingToBeginingOfSent - where to highlight to
     * @param color - color to highlight
     */
    private void highlightSentenceLoops(int tableLayoutHolder,
                                        int tableRowHolderForHighlightingToEndOfSent,
                                        int tableRowHolderForHighlightingToBeginingOfSent,
                                        int color) {
        boolean end = false;


        for (int i = tableLayoutHolder; i < mTableLayouts.size(); i++) {
            TableRow row = (TableRow) mTableLayouts.get(i).getChildAt(0);
            for (int j = tableRowHolderForHighlightingToEndOfSent; j < row.getChildCount(); j++) {

                tableRowHolderForHighlightingToEndOfSent = 0; // when the row changes it starts highlighting at beginning
                TextView word = (TextView) row.getChildAt(j);


                // If the word box is not empty it will highlight it
                if (!(word.getText().toString().isEmpty())) {
                    word.setBackgroundColor(color);
                }
                String wordFromView = word.getText().toString();

                if (wordFromView.contains(".") || wordFromView.contains("!")
                        || wordFromView.contains("?")) {

                    if (isItAFamilyName(wordFromView)) {

                        continue;
                    } else {

                        // has it break out of the second loop
                        end = true;
                        break;
                    }
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
            for (int j = tableRowHolderForHighlightingToBeginingOfSent - skipCurrentWord; j >= 0; j--) {
                TextView word = (TextView) row.getChildAt(j);

                if (!word.getText().toString().contains(".") ||
                        isItAFamilyName(word.getText().toString())) {

                    if (!(word.getText().toString().isEmpty())) {
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
                tableRowHolderForHighlightingToBeginingOfSent = row.getChildCount() - 1;
            }

            skipCurrentWord = 0;
        }
    }

    private boolean isItAFamilyName(String word){

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

    private void handleBookmark(){
        if (pageNumber == mSettingsPreferences.getBookMarkedPage()){
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

        Log.i(TAG, "Number of book pages " + mPagesOfBook.size());
        handleBookmark();

        setUpPageText();
        setUpChapterLabel();
    }

    /**
     * Sets up the chapter view based on whether the book has
     * chapters
     */
    private void setUpChapterLabel() {
        if (!mPagesOfBook.get(pageNumber).getChapterOfBook().equals("None")) {
            mChapterTextView.setVisibility(View.VISIBLE);
            mChapterTextView.setText(mPagesOfBook.get(pageNumber).getChapterOfBook());
            mChapterTextView.setTextColor(Color.BLACK);
        } else {
            mChapterTextView.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Sets up the table layouts in side the view
     * Adds the extra layouts to the table views for the Charolottes web book
     *
     * @param view
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

                showDictionaryDialog((TextView) v);
                return true;
            }
        };
    }


    /**
     * Sets up the page text by first breaking the texts into individual strings
     * Then goes through the tableLayouts and fills them with the text of the book
     */
    private void setUpPageText() {
        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");
        mPageNumber.setText(pageNumber + "");
        mPageNumber.setTextColor(Color.BLACK);

        cleanUpPageText(Color.WHITE);

        int placeHolder = 0;

        for (TableLayout tableLayout : mTableLayouts) {
            //sets up title page
            if ((pageNumber == 0 || pageNumber == 1)) {
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

    /**
     * This is used to set up the title page/ page zero
     */
    public void setupTitlePage() {
        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");
        mPageNumber.setText(pageNumber + "");
        mPageNumber.setTextColor(Color.BLACK);

        int setUpTitlePage = 5;
        int takeAwayFromEnd = 3;


        cleanUpPageText(Color.WHITE);

        int placeHolder = 0;

        for (TableLayout tableLayout : mTableLayouts) {
            //sets up title page
            if ((pageNumber == 0 || pageNumber == 1) && setUpTitlePage > 0) {
                setUpTitlePage--;
                continue;
            }

            if (pageNumber == 0) {


                TableRow row = (TableRow) tableLayout.getChildAt(0);
                for (int j = 2; j < row.getChildCount() - takeAwayFromEnd; j++) {

                    if (mPageWordBank.length != placeHolder) {
                        TextView word = (TextView) row.getChildAt(j);
                        word.setText(mPageWordBank[placeHolder]);
                        word.setTextSize(30f);
                        word.setTextColor(Color.BLACK);
                        word.setOnClickListener(onClick());
                        word.setOnLongClickListener(onLongClick());
                        placeHolder++;

                    } else {
                        break;
                    }
                }
                takeAwayFromEnd = 4;
                continue;
            }
            takeAwayFromEnd = 0;

            if (pageNumber == 1) {

                TableRow row = (TableRow) tableLayout.getChildAt(0);
                for (int j = 2; j < row.getChildCount() - takeAwayFromEnd; j++) {

                    if (mPageWordBank.length != placeHolder) {
                        TextView word = (TextView) row.getChildAt(j);
                        word.setText(mPageWordBank[placeHolder]);
                        word.setTextSize(25f);
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

    /**
     * This creates the dialog after finding the definition
     * @param wordLinkedWithDefs
     * @param currentWordTextView
     */
    private void getDefinition(ArrayList<WordLinkedWithDef> wordLinkedWithDefs,
                               TextView currentWordTextView){
        String newWord = currentWordTextView.getText()
                .toString().replaceAll("\\s+", "");

        WordLinkedWithDef findDef = WordLinkedWithDef.findDefinition(
                wordLinkedWithDefs,
                DefinitionDialog.removePunctuations(newWord.toLowerCase()));


        DefinitionDialog dialog = DefinitionDialog.newInstance(

                findDef.getWord(), findDef.getDefinition());

        FragmentManager fm = getActivity().getSupportFragmentManager();
        dialog.show(fm, DefinitionDialog.DEFINITION);

    }

    /**
     * If the dictionary is ready it will let you get the definition
     * @param currentWordTextView
     */
    private void showDictionaryDialog(TextView currentWordTextView) {
        if (dictionaryReady) {
            String newWord = currentWordTextView.getText()
                    .toString().replaceAll("\\s+", "");

            WordLinkedWithDef findDef = WordLinkedWithDef.findDefinition(
                    mDictionaryOne,
                    DefinitionDialog.removePunctuations(newWord.toLowerCase()));


            DefinitionDialog dialog = DefinitionDialog.newInstance(

                    newWord, findDef.getDefinition()
                    );

            FragmentManager fm = getActivity().getSupportFragmentManager();
            dialog.show(fm, DefinitionDialog.DEFINITION);

        }
    else {
            Toast.makeText(getActivity(),
                    "Sorry, dictionary is being set up", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets the onClickListener for the texts
     * Changes the color of the background onClick
     *
     * @return
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
        stopVoiceAndResetPlayButton();

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
                    mWordsToSpeechBank.add(word.getText() + "");
                    mHighlightedTextViews.add(word);
                    Log.i(TAG, word.getText() + "");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_SETTINGS || requestCode == Activity.RESULT_OK) {

            voiceSpeed = data.getIntExtra(SettingsDialog.VOICE_SPEED, 20);

            mOnClickHighLightSentenceMode = data.getBooleanExtra(
                    SettingsDialog.SENTENCE_BY_SENTENCE_MODE, true);

            mSettingsPreferences.setVoiceSpeed(voiceSpeed);
            mSettingsPreferences.setReadSentenceMode(mOnClickHighLightSentenceMode);


            saveSettings();
            Toast.makeText(getActivity(), "Settings Saved"
                    , Toast.LENGTH_LONG).show();

        }
        if (requestCode == GET_PAGE_NUMBER) {

            pageNumber = data.getIntExtra(SelectPageDialog.SELECTED_PAGE, 0);
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

            dictionaryReady = false;
            Toast.makeText(getActivity(), "Dictionary1 is being set up", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            mDictionaryOne = WordLinkedWithDef.linkWordsWithDefinitions(getActivity(), 0, 4677);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dictionaryReady = true;

           // mDictionary2Loader.execute();
            //this method will be running on UI thread
            Toast.makeText(getActivity(), "Dictionary1 is ready", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pages, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bookmark_page:
                if (mBookmark.getVisibility() == View.INVISIBLE) {
                    mBookmark.setVisibility(View.VISIBLE);

                    mSettingsPreferences.setBookMarkedPage(pageNumber);
                    saveSettings();
                } else {
                    mBookmark.setVisibility(View.INVISIBLE);
                }
                return true;

            case R.id.menu_setting:
                SettingsDialog dialog = SettingsDialog.newInstance(
                        voiceSpeed, mOnClickHighLightSentenceMode);
                dialog.setTargetFragment(PageFragment.this, GET_SETTINGS);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                dialog.show(fm, SettingsDialog.SETTINGS);

                return true;

            case R.id.menu_select_page:
                SelectPageDialog newPageDialog = SelectPageDialog.newInstance(pageNumber, mPagesOfBook.size());
                newPageDialog.setTargetFragment(PageFragment.this, GET_PAGE_NUMBER);

                FragmentManager fm1 = getActivity().getSupportFragmentManager();
                newPageDialog.show(fm1, SelectPageDialog.SELECT_PAGE);

                return true;

            case R.id.menu_help:
                HelpDialog helpDialog = new HelpDialog();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                helpDialog.show(fragmentManager, TAG);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean saveSettings() {
        try {
            mReadToMeJSONSerializer.savePreferences(mSettingsPreferences);
            Log.d(TAG, "Settings saved");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving settings", e);
            return false;
        }
    }

    private void stopAsyncTasks(){
        if(mDictionaryLoader.getStatus().equals(AsyncTask.Status.RUNNING))
        {
            mDictionaryLoader.cancel(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
        stopAsyncTasks();
        stopVoiceAndResetPlayButton();
    }

    @Override
    public void onDestroy() {
        mTts.shutdown();
        mWordPlayer.shutDownTTS();
        mWordPlayer.stopAudioFile();
        Log.i(TAG, "Destroyed");
        super.onDestroy();
    }
}