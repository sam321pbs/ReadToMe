package com.example.sammengistu.readtome;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

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

    public WordPlayer (Context c, TextToSpeech.OnInitListener listener){
        tts = new TextToSpeech(c, listener);
        mAppContext = c;

    }

    /**
     * Stops the media player if it is turned on
     */
    public void stopAudioFile() {
        if (mWordPlayer != null) {
            mWordPlayer.release();
            mWordPlayer = null;
        }

    }

    /**
     * Takes in an ArrayList of words
     * Copies the list of words
     * Either finds its audio file or uses text to speech to play the word
     * @param words
     */
    public void play(ArrayList<String> words) {
        stopAudioFile();

        if (words != null && words.size() > 0) {
            final ArrayList<String> wordsToPlay = new ArrayList<String>(words);

            String word = wordsToPlay.get(0);
            wordsToPlay.remove(0);

            if (WordAudioFiles.get(mAppContext).isWordInFiles(word)) {
                Log.i(TAG, "True");
                mWordPlayer = MediaPlayer.create(mAppContext, WordAudioFiles.get(mAppContext).getWordAudio(word));

                mWordPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mp == mWordPlayer) {
                            mWordPlayer.start();
                        }
                    }
                });

                mWordPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopAudioFile();
                        // Recursively call the play() method with one less
                        // track in the list.
                        play(wordsToPlay);
                    }
                });
            } else{
                Log.i(TAG, "Inside else block of play");

                String wordsToGo = word + " ";
                int foundNextWordInFile = 0;
                for (int i = 0; i < wordsToPlay.size(); i++){
                    Log.i(TAG, "Word at " + i + " is: " + wordsToPlay.get(i));
                    Log.i(TAG, "Is in file: " + WordAudioFiles.get(mAppContext)
                            .isWordInFiles(wordsToPlay.get(i)));
                    if (WordAudioFiles.get(mAppContext).isWordInFiles(wordsToPlay.get(i))){
                        foundNextWordInFile = i;
                        break;
                    } else {
                        wordsToGo += wordsToPlay.get(i) + " ";
                        foundNextWordInFile++;
                    }
                }

                Log.i(TAG, foundNextWordInFile + "");
                for (int i = 0; i < foundNextWordInFile; i++){
                    Log.i(TAG, "Remove word at " + i + " is: " + wordsToPlay.get(0));

                    wordsToPlay.remove(0);
                }

                Log.i(TAG, " This is the word going: " + wordsToGo);
                speakOut(wordsToGo);

                do {

                } while (tts.isSpeaking());

                    play(wordsToPlay);

            }


        }
    }

    /**
     * Checks to make sure the Text to speech engine is working properly
     * Then sets up its language
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
     * @param words
     */
    @SuppressWarnings("deprecation")
    private void speakOut(String words) {
        Log.e("TTS", "Speak");
        tts.setSpeechRate(0.60f);
        tts.speak(words, TextToSpeech.QUEUE_FLUSH, null);
    }
}