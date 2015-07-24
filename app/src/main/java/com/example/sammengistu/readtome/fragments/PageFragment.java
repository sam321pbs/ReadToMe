package com.example.sammengistu.readtome.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.WordPlayer;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Sets up the page view of the book
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment /*implements
        TextToSpeech.OnInitListener*/ {

    private static final String TAG = "PageFragment";
    private ArrayList<PageOfBook> mPagesOfBook;
    private ImageView mTurnPage;
    private ImageView mGoBackPage;
    private ImageView mPagePicture;
    private String[] mPageWordBank;
    int pageNumber;
    private ArrayList<TableLayout> mTableLayouts;
    private ImageView mPlayButton;
    private ArrayList<String> mWordsToSpeechBank;
    private ImageView mClearHighlights;
    private Book currentBook;
    private TextView mChapterTextView;
    private ArrayList<TextView> mHighlightedTextViews;

    private WordPlayer mWordPlayer;

    @Override
    public void onCreate(Bundle savedInstnaceState) {
        super.onCreate(savedInstnaceState);

        mTableLayouts = new ArrayList<TableLayout>();
        mWordsToSpeechBank = new ArrayList<String>();
        mHighlightedTextViews = new ArrayList<>();

        UUID bookId = (UUID) getActivity().getIntent().getSerializableExtra(MyLibraryFragment.BOOK_ID);

        currentBook = Library.get(getActivity()).getBook(bookId);

        mPagesOfBook = currentBook.getPagesOfBook();

        pageNumber = 0;

        mWordPlayer = new WordPlayer(getActivity());

        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View blankPage;
        if (currentBook.getTitle().equalsIgnoreCase("Curious George")) {
            blankPage = inflater.inflate(R.layout.pages_fragment, container, false);
            mPagePicture = (ImageView) blankPage.findViewById(R.id.page_picture);
        } else {
            blankPage = inflater.inflate(R.layout.page_without_image_fragment, container, false);
            mChapterTextView = (TextView) blankPage.findViewById(R.id.book_chapter_textview);
            setUpChapterLabel();
        }

        setTableLayouts(blankPage);

        setImage();

        mTurnPage = (ImageView) blankPage.findViewById(R.id.turn_page);
        mTurnPage.setOnClickListener(new View.OnClickListener()

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
        mGoBackPage = (ImageView) blankPage.findViewById(R.id.go_back);
        mGoBackPage.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               pageNumber--;
                                               if (pageNumber < 0) {
                                                   pageNumber = 0;
                                               }
                                               handlePageTurn();
                                           }
                                       }

        );

        mPlayButton = (ImageView) blankPage.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               findHighlightedWords();

                                               mWordPlayer.play(mWordsToSpeechBank, mHighlightedTextViews);
                                               mWordsToSpeechBank.clear();
                                               mHighlightedTextViews.clear();
                                           }
                                       }

        );

        mClearHighlights = (ImageView) blankPage.findViewById(R.id.clear_highlights_button);
        mClearHighlights.setOnClickListener(new View.OnClickListener()

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

    /**
     * Handles the page turning
     * Sets up the views according to the content
     */
    private void handlePageTurn() {
        setUpChapterLabel();
        setUpPageText();
        setImage();
    }

    /**
     * Checks if the book has a chapter in it
     *
     * @return
     */
    private boolean doesBookHaveChapters() {
        return currentBook.getTitle().equalsIgnoreCase("Charlottes web");
    }

    /**
     * Sets up the chapter view based on whether the book has
     * chapters
     */
    private void setUpChapterLabel() {
        if (doesBookHaveChapters() && pageNumber == 0) {
            mChapterTextView.setVisibility(View.VISIBLE);
            mChapterTextView.setText(currentBook.getChapter());
        } else if (doesBookHaveChapters()) {
            mChapterTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Checks which book the current book is and sets up the images for curious
     * george
     */
    private void setImage() {
        if (currentBook.getTitle().equalsIgnoreCase("Curious George")) {
            mPagePicture.setImageResource(mPagesOfBook.get(pageNumber).getPagePicture());
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
        if (currentBook.getTitle().equalsIgnoreCase("Charlottes Web")) {
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout10));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout11));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout12));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout13));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout14));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout15));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout16));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout17));
            mTableLayouts.add((TableLayout) view.findViewById(R.id.fragment_page_tableLayout18));
        }
    }

    /**
     * Sets up the page text by first breaking the texts into individual strings
     * Then goes through the tableLayouts and fills them with the text of the book
     */
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
                if (backgroundColor == Color.YELLOW) {
                    textView.setBackgroundColor(Color.WHITE);
                } else {
                    textView.setBackgroundColor(Color.YELLOW);
                }
            }
        };
    }

    /**
     * Turns all the highlighted text into a White the background
     */
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
    public void onDestroy() {

        mWordPlayer.stopAudioFile();
        super.onDestroy();
    }
}

