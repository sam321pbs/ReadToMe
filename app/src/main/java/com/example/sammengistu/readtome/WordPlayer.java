package com.example.sammengistu.readtome;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Plays words from either an audio file or uses TextToSpeech library
 */
public class WordPlayer implements TextToSpeech.OnInitListener {

    private static final String TAG = "WordPlayer";
    private MediaPlayer mWordPlayer;
    private TextToSpeech tts;
    private Context mAppContext;

    public WordPlayer(Context c) {
        tts = new TextToSpeech(c, this);
        mAppContext = c;
    }

    /**
     * Stops the media player if it is turned on
     */
    public void stopAudioFile() {
        if (mWordPlayer != null) {
            mWordPlayer.release();
            mWordPlayer = null;
            tts.shutdown();
        }

    }

    /**
     * Takes in an ArrayList of words
     * Copies the list of words
     * Either finds its audio file or uses text to speech to play the word
     *
     * @param words
     */
    public void play(ArrayList<String> words, ArrayList<TextView> highlightedWords) {
        stopAudioFile();

        if (words != null && words.size() > 0) {
            final ArrayList<String> wordsToPlay = new ArrayList<String>(words);
            final ArrayList<TextView> highLightedTextView = new ArrayList<TextView>(highlightedWords);

            String word = wordsToPlay.get(0);
            final TextView highlighted = highLightedTextView.get(0);
            wordsToPlay.remove(0);
            highLightedTextView.remove(0);

            Log.i(TAG, highlighted.getText() + "");

            if (WordAudioFiles.get(mAppContext).isWordInFiles(word)) {
                Log.i(TAG, "True");
                mWordPlayer = MediaPlayer.create(mAppContext, WordAudioFiles.get(mAppContext).getWordAudio(word));

                mWordPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == mWordPlayer) {
                            mWordPlayer.start();
                            highlighted.setBackgroundColor(Color.BLUE);
                        }
                    }
                });

                mWordPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopAudioFile();
                        highlighted.setBackgroundColor(Color.YELLOW);
                        // Recursively call the play() method with one less
                        // track in the list.
                        play(wordsToPlay, highLightedTextView);
                    }
                });
            } else {
                Log.i(TAG, "Inside else block of play");

                Handler mainHandler = new Handler(mAppContext.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        highlighted.setBackgroundColor(Color.BLUE);
                    }
                };
                mainHandler.post(myRunnable);


                speakOut(word);

                do {

                } while (tts.isSpeaking());

                tts.shutdown();
                play(wordsToPlay, highLightedTextView);

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
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    /**
     * Uses the text to speech engine to actually play the word that is being
     * passed in
     *
     * @param word
     */
    @SuppressWarnings("deprecation")
    private void speakOut(String word) {

        tts.setSpeechRate(0.53f);

        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);

    }
}