package com.example.sammengistu.readtome;

import com.example.sammengistu.readtome.fragments.PageFragment;
import com.example.sammengistu.readtome.fragments.SettingsDialog;
import com.example.sammengistu.readtome.fragments.SettingsDialogTypeAndRead;
import com.example.sammengistu.readtome.fragments.TypeAndReadHelpDialog;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TypeAndReadFragment extends Fragment {

    private static final int GET_SETTINGS = 2323;
    private final String TAG = "TypeAndReadFragment";

    private List<TableLayout> mTableLayouts;
    private List<TextView> mAllTextViews;
    private ImageView mPlayButtonImage;
    private static int sPlayOrStopCounterTypeAndRead;
    private WordPlayer mWordPlayer;
    private int mVoiceSpeed = 20;
    private List<TextView> mHighlightedTextViews;
    private List<String> mAllWords;
    private List<Integer> mPageStartNumber;
    private int mPageStarterTracker = 0;
    private List<PageOfBook> mPageOfTypedText;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTableLayouts = new ArrayList<>();
        mAllTextViews = new ArrayList<>();
        mHighlightedTextViews = new ArrayList<>();
        mAllWords = new ArrayList<>();
        mPageOfTypedText = new ArrayList<>();
        mPageStartNumber = new ArrayList<>();

        View typeAndReadView = inflater.inflate(R.layout.fragment_type_and_read, container, false);
        setTableLayouts(typeAndReadView);
        addAllTextViewsToList();
        mWordPlayer = new WordPlayer(getActivity(), getActivity(), mVoiceSpeed, mAllTextViews);

        EditText typedText = (EditText) typeAndReadView.findViewById(R.id.type_here_box);

        mPlayButtonImage = (ImageView) typeAndReadView.findViewById(R.id.play_button_type_and_read);

        typedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mPageStartNumber.clear();
                cleanUpPageText(Color.WHITE, true);
                mPageStarterTracker = 0;

                mAllWords = new ArrayList<>(Arrays.asList(s.toString().split("\\s")));

                for (int i = 0; i < mAllWords.size(); i++) {

                    if (i % mAllTextViews.size() == 0) {
                        mPageStartNumber.add(i);
                    }

                    if (i < mAllTextViews.size()) {
                        TextView wordBoxToFill = mAllTextViews.get(i);
                        wordBoxToFill.setText(mAllWords.get(i));
                    }
                }
            }
        });

        mPlayButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPlayOrStopCounterTypeAndRead++;

                if (sPlayOrStopCounterTypeAndRead == 1) {
                    PageFragment.findHighlightedWords(mTableLayouts, mHighlightedTextViews);
                    mWordPlayer.setPlay(true);

                    mPlayButtonImage.setImageResource(R.drawable.added_stop_button);
                    mWordPlayer.setVoiceSpeed(mVoiceSpeed);

                    mWordPlayer.playSentenceBySentence(
                        mHighlightedTextViews, mPlayButtonImage);

                } else {

                    stopReadingAndResetPlayButton();
                }

                mHighlightedTextViews.clear();
            }
        });

        mPlayButtonImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                sPlayOrStopCounterTypeAndRead++;

                if (sPlayOrStopCounterTypeAndRead == 1) {

                    PageFragment.highlightThePage(mTableLayouts);

                    mWordPlayer.setPlay(true);

                    mPlayButtonImage.setImageResource(R.drawable.added_stop_button);
                    mWordPlayer.setVoiceSpeed(mVoiceSpeed);

                    StringBuilder stringBuilderBuildWordPage = new StringBuilder();

                    mPageOfTypedText.clear();

                    for (int i = 0; i < mAllWords.size(); i++) {

                        if (i % mAllTextViews.size() == 0 && i != 0) {
                            mPageOfTypedText.add(
                                new PageOfBook(stringBuilderBuildWordPage.toString(), 0));
                            stringBuilderBuildWordPage = new StringBuilder();
                        }

                        stringBuilderBuildWordPage.append(mAllWords.get(i) + " ");
                    }

                    if (stringBuilderBuildWordPage.toString().length() != 0) {
                        mPageOfTypedText.add(
                            new PageOfBook(stringBuilderBuildWordPage.toString(), 0));
                    }

                    PageFragment.findHighlightedWords(mTableLayouts, mHighlightedTextViews);

                    mWordPlayer.playChapter(mHighlightedTextViews, mPlayButtonImage,
                        mPageOfTypedText,
                        null,
                        null);

                } else {

                    stopReadingAndResetPlayButton();
                }

                mHighlightedTextViews.clear();

                return true;
            }
        });

        ImageView pageButton = (ImageView)
            typeAndReadView.findViewById(R.id.page_button_type_and_read);
        pageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageFragment.highlightThePage(mTableLayouts);
            }
        });

        ImageView goBackButton = (ImageView)
            typeAndReadView.findViewById(R.id.go_back_type_and_read);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUpPageText(Color.WHITE, false);
                mHighlightedTextViews.clear();
                updatePage(false);

            }
        });

        ImageView goForwardButton = (ImageView)
            typeAndReadView.findViewById(R.id.turn_page_type_and_read);
        goForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUpPageText(Color.WHITE, false);
                mHighlightedTextViews.clear();
                updatePage(true);

            }
        });

        ImageView clearButton = (ImageView) typeAndReadView.findViewById(R.id.clear_highlights_button_type_and_read);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUpPageText(Color.WHITE, false);
                mHighlightedTextViews.clear();
            }
        });

        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cleanUpPageText(Color.WHITE, true);
                mHighlightedTextViews.clear();
                return false;
            }
        });

        return typeAndReadView;
    }

    private void updatePage(boolean turnPage) {
        if (turnPage) {
            if (mPageStarterTracker < mPageStartNumber.size() - 1) {
                mPageStarterTracker++;
            } else {
                return;
            }
        } else {
            if (mPageStarterTracker > 0) {
                mPageStarterTracker--;
            } else {
                return;
            }
        }

        for (TextView textView : mAllTextViews) {
            textView.setText("");
        }
        int stopLoop;

        try {
            stopLoop = mPageStartNumber.get(mPageStarterTracker + 1);

        } catch (IndexOutOfBoundsException e) {
            stopLoop = mAllWords.size();
        }

        int counter = 0;
        for (int i = mPageStartNumber.get(mPageStarterTracker);
             i < stopLoop; i++) {

            mAllTextViews.get(counter++).setText(mAllWords.get(i));
        }
    }

    /**
     * Turns all the highlighted text into a white background
     */
    private void cleanUpPageText(int backgroundColor, boolean clearTextBox) {
        stopReadingAndResetPlayButton();

        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                if (clearTextBox) {
                    word.setText("");
                }
                word.setBackgroundColor(backgroundColor);
            }
        }
    }

    public static void setPlayOrStopCounterTypeAndRead(int playOrStopCounters) {
        sPlayOrStopCounterTypeAndRead = playOrStopCounters;
    }


    private void stopReadingAndResetPlayButton() {

        mWordPlayer.stopTtsVoice();
        mWordPlayer.setPlay(false);
        mPlayButtonImage.setImageResource(R.drawable.play_button_updated);
        sPlayOrStopCounterTypeAndRead = 0;
    }

    public void addAllTextViewsToList() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView word = (TextView) row.getChildAt(j);
                word.setBackgroundColor(Color.WHITE);
                word.setOnClickListener(onClick());
                mAllTextViews.add(word);
            }
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
                ColorDrawable textBackGroundColor = (ColorDrawable) textView.getBackground();
                int backgroundColor = textBackGroundColor.getColor();


                if (backgroundColor == Color.WHITE) {
                    PageFragment.highlightSentenceMode(mTableLayouts, textView, Color.YELLOW);
                } else {
                    PageFragment.highlightSentenceMode(mTableLayouts, textView, Color.WHITE);
                }
            }
        };
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.type_and_read_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        switch (item.getItemId()) {

            case R.id.type_and_read_settings:
                SettingsDialogTypeAndRead settingsDialogTypeAndRead =
                    SettingsDialogTypeAndRead.newInstance(mVoiceSpeed);

                settingsDialogTypeAndRead.setTargetFragment(this, GET_SETTINGS);

                settingsDialogTypeAndRead.show(fm, SettingsDialog.SETTINGS);

                return true;

            case R.id.type_and_read_help:

                TypeAndReadHelpDialog typeAndReadHelpDialog = new TypeAndReadHelpDialog();
                typeAndReadHelpDialog.show(fm, "type and read help dialog");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_SETTINGS || requestCode == Activity.RESULT_OK) {

            mVoiceSpeed = data.getIntExtra(SettingsDialog.VOICE_SPEED, 20);

        }
    }
}

