package com.example.sammengistu.readtome;

import com.example.sammengistu.readtome.fragments.DefinitionDialog;
import com.example.sammengistu.readtome.fragments.PageFragment;
import com.example.sammengistu.readtome.models.PageOfBook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Plays words from either an audio file or uses TextToSpeech library
 */
public class WordPlayer implements TextToSpeech.OnInitListener {

    private static final int HAS_MORE_THAN_ONE = 0;
    private static final int FIRST_ITEM = 0;
    private final int PAGE_FRAGMENT_MAX_NUMBER_OF_TEXT_VIEWS = 184;

    private TextToSpeech mTts;
    private Activity mAppActivity;
    private float mVoiceSpeed;
    private boolean mPlay;
    private List<TextView> mAllTextViews;


    /**
     * Creates a wordPlayer that sets up all the requirements that is need for
     * WordPlayer to work
     *
     * @param c           - context of the activity
     * @param appActivity - used to run on the mainthread of the activity
     */
    public WordPlayer(Context c, Activity appActivity,
                      int voiceSpeed, List<TextView> allTextViews) {
        mTts = new TextToSpeech(c, this);
        mVoiceSpeed = ((float) voiceSpeed / 20);
        mTts.setSpeechRate(mVoiceSpeed);
        mAppActivity = appActivity;
        mPlay = false;
        mAllTextViews = allTextViews;

    }

    public void stopTtsVoice() {
        mTts.stop();
    }

    /**
     * Shuts down TextToSpeech
     */
    public void shutDownTTS() {
        mTts.shutdown();
    }

    /**
     * Plays the word
     * uses the TextToSpeech class to play the word
     * <p/>
     * It also highlights the text box of the word it is playing
     *
     * @param highlightedWords - change the text view box color
     */
    @SuppressWarnings("deprecation")
    public void play(List<TextView> highlightedWords, final ImageView playStopButton) {

        if (mPlay) {
            if (highlightedWords.size() > HAS_MORE_THAN_ONE) {
                mTts.setSpeechRate(mVoiceSpeed / 20);

                final List<TextView> highLightedTextView = new ArrayList<>(highlightedWords);

                final TextView textView = highLightedTextView.get(FIRST_ITEM);

                final String getFirstWord = DefinitionDialog.removePunctuations(
                    highlightedWords.get(FIRST_ITEM).getText().toString());

                highLightedTextView.remove(FIRST_ITEM);

                mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setBackgroundColor(Color.GREEN);
                            }
                        });
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setBackgroundColor(Color.YELLOW);
                            }
                        });
                        play(highLightedTextView, playStopButton);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });

                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

                mTts.speak(getFirstWord, TextToSpeech.QUEUE_FLUSH, map);

            } else {
                mAppActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playStopButton.setImageResource(R.drawable.play_button_updated);
                        PageFragment.setPlayOrStopCounter(0);
                    }
                });
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void playSentenceBySentence(List<TextView> highlightedWords,
                                       final ImageView playStopButton) {

        if (mPlay) {
            mTts.setSpeechRate(mVoiceSpeed / 20);

            if (highlightedWords.size() > HAS_MORE_THAN_ONE) {

                final List<TextView> highLightedTextViews = new ArrayList<>(highlightedWords);

                //TextViews to highlight as a sentence
                final List<TextView> textViewsOfSentence = new ArrayList<>();
                //Sentence to play
                StringBuilder sentenceToPlay = new StringBuilder();

                for (int i = 0; i < highLightedTextViews.size(); i++) {

                    String currentWord = highLightedTextViews.get(i).getText().toString();

                    if (!PageFragment.endOfSentence(currentWord)
                        || PageFragment.isItAFamilyName(currentWord)) {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        //Remove single quotes from word
                        String cleanedWord = highLightedTextViews.get(i)
                            .getText().toString().replaceAll("'", "");
                        if (cleanedWord.equals("Dr.")) {
                            cleanedWord = "Doctor";
                        }
                        sentenceToPlay.append(cleanedWord);
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        i--;

                    } else {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        sentenceToPlay.append(highLightedTextViews.get(i)
                            .getText().toString().replaceAll("'", ""));
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        break;
                    }
                }

                mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView textView : textViewsOfSentence) {
                                    textView.setBackgroundColor(Color.GREEN);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView textView : textViewsOfSentence) {
                                    textView.setBackgroundColor(Color.YELLOW);
                                }
                            }
                        });

                        playSentenceBySentence(highLightedTextViews, playStopButton);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });

                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

                mTts.speak(sentenceToPlay.toString(), TextToSpeech.QUEUE_FLUSH, map);

            } else {
                mAppActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playStopButton.setImageResource(R.drawable.play_button_updated);
                        PageFragment.setPlayOrStopCounter(0);
                        try {
                            TypeAndReadFragment.setPlayOrStopCounterTypeAndRead(0);
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void playChapter(List<TextView> highlightedWordsTextView,
                            final ImageView playStopButton,
                            List<PageOfBook> pageOfBookList,
                            final TextView chapterLabelTextView,
                            final TextView pageNumberTextView) {

        Log.i("WordPlayer", "highlighted textviews = " + highlightedWordsTextView.size());
        Log.i("WordPlayer", "Page of books = " + pageOfBookList.size());
        if (mPlay) {

            mTts.setSpeechRate(mVoiceSpeed / 20);

            final List<PageOfBook> pageOfBooks = new ArrayList<>(pageOfBookList);

            if (highlightedWordsTextView.size() > HAS_MORE_THAN_ONE) {

                final List<TextView> highLightedTextViews = new ArrayList<>(highlightedWordsTextView);

                //TextViews to highlight as a sentence
                final List<TextView> textViewsOfSentence = new ArrayList<>();
                //Sentence to play
                StringBuilder sentenceToPlay = new StringBuilder();

                for (int i = 0; i < highLightedTextViews.size(); i++) {

                    String currentWord = highLightedTextViews.get(i).getText().toString();

                    if (!PageFragment.endOfSentence(currentWord)
                        || PageFragment.isItAFamilyName(currentWord)) {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        //Remove single quotes from word
                        String cleanedWord = highLightedTextViews.get(i)
                            .getText().toString().replaceAll("'", "");
                        if (cleanedWord.equals("Dr.")) {
                            cleanedWord = "Doctor";
                        }
                        sentenceToPlay.append(cleanedWord);
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        i--;

                    } else {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        sentenceToPlay.append(highLightedTextViews.get(i)
                            .getText().toString().replaceAll("'", ""));
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        break;
                    }
                }

                mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView textView : textViewsOfSentence) {
                                    textView.setBackgroundColor(Color.GREEN);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        mAppActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView textView : textViewsOfSentence) {
                                    textView.setBackgroundColor(Color.YELLOW);
                                }
                            }
                        });

                        playChapter(highLightedTextViews, playStopButton, pageOfBooks,
                            chapterLabelTextView, pageNumberTextView);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });

                HashMap<String, String> map = new HashMap<>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

                mTts.speak(sentenceToPlay.toString(), TextToSpeech.QUEUE_FLUSH, map);

            } else {

                pageOfBooks.remove(FIRST_ITEM);

                if (pageOfBooks.size() > 0) {
                    mAppActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            updatePage(mAllTextViews.size(), pageOfBooks.get(FIRST_ITEM),
                                chapterLabelTextView);

                            try {
                                String pageNumberForView = PageFragment.updatePageNumber() + "";

                                pageNumberTextView.setText(pageNumberForView);
                            } catch (Exception e){

                            }

                            List<TextView> textViews = new ArrayList<>();

                            for (int i = 0; i < mAllTextViews.size(); i++) {
                                if (!mAllTextViews.get(i).getText().equals("")) {
                                    textViews.add(mAllTextViews.get(i));
                                }
                            }

                            playChapter(textViews, playStopButton,
                                pageOfBooks,
                                chapterLabelTextView,
                                pageNumberTextView);
                        }
                    });
                } else {

                    mAppActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playStopButton.setImageResource(R.drawable.play_button_updated);
                            PageFragment.setPlayOrStopCounter(0);
                            try {
                                TypeAndReadFragment.setPlayOrStopCounterTypeAndRead(0);
                            } catch (Exception e){

                            }
                        }
                    });
                }
            }
        }
    }

    public void updatePage( int maxNumberOfTextViews,
        PageOfBook pageOfBook, TextView chapterLabelTextView){

        String[] pageWords = pageOfBook.getPageText().split(" ");

        //Highlight and update text box
        for (int j = 0; j < pageWords.length; j++) {

            mAllTextViews.get(j).setBackgroundColor(Color.YELLOW);
            mAllTextViews.get(j).setText(pageWords[j]);
        }

        //Turn all textviews with no words in them to white background
        if (pageWords.length < maxNumberOfTextViews) {

            for (int l = pageWords.length; l < maxNumberOfTextViews; l++) {
                mAllTextViews.get(l).setText("");
                mAllTextViews.get(l).setBackgroundColor(Color.WHITE);
            }
        }
        try {
            chapterLabelTextView.setVisibility(View.INVISIBLE);
        } catch (Exception e){

        }

    }

    /**
     * Checks to make sure the Text to speech engine is working properly
     * Then sets up its language
     *
     * @param status - gets the status of the text to speech engine on initialization
     */
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = mTts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    public void setPlay(boolean play) {
        mPlay = play;
    }

    public void setVoiceSpeed(float voiceSpeed) {
        mVoiceSpeed = voiceSpeed;
    }

}