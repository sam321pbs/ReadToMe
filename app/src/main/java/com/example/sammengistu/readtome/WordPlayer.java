package com.example.sammengistu.readtome;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sammengistu.readtome.fragments.DefinitionDialog;
import com.example.sammengistu.readtome.fragments.PageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Plays words from either an audio file or uses TextToSpeech library
 */
public class WordPlayer implements TextToSpeech.OnInitListener {

    private static final String TAG = "WordPlayer";
    private static final int HAS_MORE_THAN_ONE = 0;
    private static final int FIRST_ITEM = 0;

    private TextToSpeech mTts;
    private Activity mAppActivity;
    private float mVoiceSpeed;
    private boolean mOnClickHighLightSentenceMode;
    private boolean mPlay;


    /**
     * Creates a wordPlayer that sets up all the requirements that is need for
     * WordPlayer to work
     *
     * @param c              - context of the activity
     * @param appActivity    - used to run on the mainthread of the activity
     * @param readBySentence - to read like a sentence or word by word
     */
    public WordPlayer(Context c, Activity appActivity, boolean readBySentence,
                      int voiceSpeed) {
        mTts = new TextToSpeech(c, this);
        mVoiceSpeed = ((float)voiceSpeed / 20);
        mTts.setSpeechRate(mVoiceSpeed);
        mAppActivity = appActivity;
        mOnClickHighLightSentenceMode = readBySentence;
        mPlay = false;

    }

    public void stopTtsVoice(){
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
     * 1st checks if the word is in a media player  file if not
     * uses the TextToSpeech class to play the word
     * <p/>
     * It also highlights the text box of the word it is playing
     *
     * @param playMe
     * @param highlightedWords
     */
    @SuppressWarnings("deprecation")
    public void play(ArrayList<String> playMe, ArrayList<TextView> highlightedWords, final ImageView playStopButton) {

        if (mPlay) {
            if (playMe.size() > HAS_MORE_THAN_ONE) {
                mTts.setSpeechRate(mVoiceSpeed / 20);
                final ArrayList<String> wordsToPlay = new ArrayList<String>(playMe);
                final ArrayList<TextView> highLightedTextView = new ArrayList<TextView>(highlightedWords);

                final TextView textView = highLightedTextView.get(FIRST_ITEM);

                final String getFirst = DefinitionDialog.removePunctuations(wordsToPlay.get(FIRST_ITEM));
                wordsToPlay.remove(FIRST_ITEM);
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
                        play(wordsToPlay, highLightedTextView, playStopButton);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

                if (mOnClickHighLightSentenceMode) {
                    // StringBuilder
                }

                mTts.speak(getFirst, TextToSpeech.QUEUE_FLUSH, map);

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
    public void playSentenceBySentence(ArrayList<String> playMe, ArrayList<TextView> highlightedWords, final ImageView playStopButton) {

        if (mPlay) {
            mTts.setSpeechRate(mVoiceSpeed /20);
            if (playMe.size() > HAS_MORE_THAN_ONE) {

                final ArrayList<String> wordsToPlay = new ArrayList<String>(playMe);
                final ArrayList<TextView> highLightedTextViews = new ArrayList<TextView>(highlightedWords);

                //TextViews to highlight as a sentence
                final ArrayList<TextView> textViewsOfSentence = new ArrayList<>();
                //Sentence to play
                StringBuilder sentenceToPlay = new StringBuilder();

                for (int i = 0; i < highLightedTextViews.size(); i++) {

                    if (!highLightedTextViews.get(i).getText().toString().contains(".")) {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        sentenceToPlay.append(highLightedTextViews.get(i).getText().toString());
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        wordsToPlay.remove(FIRST_ITEM);
                        i--;

                    } else {

                        textViewsOfSentence.add(highLightedTextViews.get(0));
                        sentenceToPlay.append(highLightedTextViews.get(i).getText().toString());
                        sentenceToPlay.append(" ");

                        highLightedTextViews.remove(FIRST_ITEM);
                        wordsToPlay.remove(FIRST_ITEM);
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

                        playSentenceBySentence(wordsToPlay, highLightedTextViews, playStopButton);
                    }

                    @Override
                    public void onError(String utteranceId) {

                    }
                });

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

                mTts.speak(sentenceToPlay.toString(), TextToSpeech.QUEUE_FLUSH, map);

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

        /**
         * Checks to make sure the Text to speech engine is working properly
         * Then sets up its language
         *
         * @param status
         */
        @Override
        public void onInit ( int status){

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

    public void setPlay(boolean play) {
        mPlay = play;
    }

    public void setVoiceSpeed(float voiceSpeed) {
        mVoiceSpeed = voiceSpeed;
    }

}