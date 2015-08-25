package com.example.sammengistu.readtome;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.TextView;

import com.example.sammengistu.readtome.fragments.DefinitionDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    private MediaPlayer mWordPlayer;

    private TextToSpeech mTts;
    private Context mAppContext;
    private Activity mAppActivity;

    public WordPlayer(Context c, Activity appActivity) {
        mTts = new TextToSpeech(c, this);
        mTts.setSpeechRate(1.0f);
        mAppContext = c;
        mAppActivity = appActivity;


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
    public void play(ArrayList<String> playMe, ArrayList<TextView> highlightedWords) {

        if (playMe.size() > HAS_MORE_THAN_ONE) {

            final ArrayList<String> wordsToPlay = new ArrayList<String>(playMe);
            final ArrayList<TextView> highLightedTextView = new ArrayList<TextView>(highlightedWords);

            final TextView textView = highLightedTextView.get(FIRST_ITEM);

            final String getFirst1 = wordsToPlay.get(FIRST_ITEM);
            final String getFirst = DefinitionDialog.removePunctuations(wordsToPlay.get(FIRST_ITEM));
            wordsToPlay.remove(FIRST_ITEM);
            highLightedTextView.remove(FIRST_ITEM);

//            if (WordAudioFiles.get(mAppContext).isWordInFiles(getFirst)) {
//
//                stopAudioFile();
//
//                mWordPlayer = MediaPlayer.create(mAppContext, WordAudioFiles.get(mAppContext)
//                        .getWordAudio(getFirst));
//
//               // mWordPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//                mWordPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        if (mp == mWordPlayer) {
//                            mWordPlayer.start();
//                            textView.setBackgroundColor(Color.BLUE);
//                        }
//                    }
//                });
//
//                mWordPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        stopAudioFile();
//                        textView.setBackgroundColor(Color.YELLOW);
//                        // Recursively call the play() method with one less
//                        // track in the list.
//                        play(wordsToPlay, highLightedTextView);
//                    }
//                });
//
//            } else {

            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    //Log.i(TAG, "Started");
                    mAppActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setBackgroundColor(Color.GREEN);
                        }
                    });

                }

                @Override
                public void onDone(String utteranceId) {
                    //Log.i(TAG, "Done");
                    mAppActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setBackgroundColor(Color.YELLOW);
                            // Log.i(TAG, textView.getText().toString());

                        }
                    });
                    play(wordsToPlay, highLightedTextView);
                }

                @Override
                public void onError(String utteranceId) {

                }
            });

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");

            mTts.speak(getFirst1, TextToSpeech.QUEUE_FLUSH, map);

            //  }
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

            int result = mTts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    static MediaPlayer getMediaPlayer(Context context) {

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {
        }

        return mediaplayer;
    }

}