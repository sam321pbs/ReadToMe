package com.example.sammengistu.readtome.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sammengistu.readtome.Book;
import com.example.sammengistu.readtome.Library;
import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.WordPlayer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment implements
        TextToSpeech.OnInitListener  {

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    private static final String TAG = "PageFragment";
    private ArrayList<PageOfBook> mPagesOfBook;
    private ImageView mTurnPage;
    private ImageView mGoBackPage;
    private ImageView mPagePicture;
    private String[] mPageWordBank;
    int pageNumber;
    private ArrayList<TableLayout> mTableLayouts;
    private TextToSpeech tts;
    private ImageView mPlayButton;
    private ArrayList<String> mWordsToSpeechBank;
    private Button mClearHighlights;
    private Book currentBook;

    private WordPlayer mWordPlayer = new WordPlayer();

    @Override
    public void onCreate(Bundle savedInstnaceState) {
        super.onCreate(savedInstnaceState);

        mTableLayouts = new ArrayList<TableLayout>();
        mWordsToSpeechBank = new ArrayList<String>();

        UUID bookId = (UUID)getActivity().getIntent().getSerializableExtra(MyLibraryFragment.BOOK_ID);

        currentBook = Library.get(getActivity()).getBook(bookId);

        mPagesOfBook = currentBook.getPagesOfBook();

        pageNumber = 0;

        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

        tts = new TextToSpeech(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View blankPage;
        if (currentBook.getTitle().equalsIgnoreCase("Curious George")){
             blankPage = inflater.inflate(R.layout.pages_fragment, container, false);
            mPagePicture = (ImageView) blankPage.findViewById(R.id.page_picture);
        }else {
             blankPage = inflater.inflate(R.layout.page_without_image_fragment, container, false);
        }

        setTableLayouts(blankPage);
        setImage();

        mTurnPage = (ImageView) blankPage.findViewById(R.id.turn_page);
        mTurnPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber++;
                if (pageNumber > mPagesOfBook.size() -1 ){
                    pageNumber = mPagesOfBook.size() -1;
                }
                setUpPageText();
                setImage();
                mWordsToSpeechBank.clear();
            }
        });
        mGoBackPage = (ImageView) blankPage.findViewById(R.id.go_back);
        mGoBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber--;
                if (pageNumber < 0){
                    pageNumber = 0;
                }
                setUpPageText();
                setImage();
                }
        });

        mPlayButton = (ImageView) blankPage.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findHighlightedWords();
                if (pageNumber == 1) {
                    speakOut();
                } else {
                    mWordPlayer.play(getActivity(), mWordsToSpeechBank);
                }
                mWordsToSpeechBank.clear();
            }
        });

        mClearHighlights = (Button)blankPage.findViewById(R.id.clear_highlights_button);
        mClearHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpPageText();
                mWordsToSpeechBank.clear();
            }
        });

        setUpPageText();
        return blankPage;
    }

    private void setImage (){
        if (currentBook.getTitle().equalsIgnoreCase("Curious George")) {
            mPagePicture.setImageResource(mPagesOfBook.get(pageNumber).getPagePicture());
        }
    }

    private void setTableLayouts(View view){
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout1));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout2));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout3));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout4));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout5));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout6));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout7));
        mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout8));
        if (currentBook.getTitle().equalsIgnoreCase("Charlottes Web")) {
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout9));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout10));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout11));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout12));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout13));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout14));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout15));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout16));
        }
    }


    private void setUpPageText() {
        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

        cleanUpPageText();

        int placeHolder = 0;

        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                if (mPageWordBank.length != placeHolder) {
                    TextView word = (TextView) row.getChildAt(j);
                    word.setText(mPageWordBank[placeHolder]);
                    word.setOnClickListener(onClick());
                    placeHolder++;

                } else {
                    break;
                }
            }
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                textView.setBackgroundColor(Color.YELLOW);
                // Log.i(TAG, textView.getText() + "");
            }
        };
    }

    private void cleanUpPageText() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                word.setText("");
                word.setBackgroundColor(Color.WHITE);
            }
        }
    }

    private void findHighlightedWords() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                ColorDrawable textBackGroundColor = (ColorDrawable) word.getBackground();
                int backgroundColor = textBackGroundColor.getColor();
                if (backgroundColor == Color.YELLOW) {
                    mWordsToSpeechBank.add(word.getText() + "");
                    Log.i(TAG, word.getText() + "");
                }
            }
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                mPlayButton.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

        String wordsToSpeech = "";

        for (String word : mWordsToSpeechBank) {
            wordsToSpeech += word + " ";
        }
        mWordsToSpeechBank.clear();

        tts.setSpeechRate(0.65f);
        tts.speak(wordsToSpeech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        mWordPlayer.stop();
        super.onDestroy();
    }
}

