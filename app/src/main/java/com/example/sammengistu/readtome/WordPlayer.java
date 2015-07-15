package com.example.sammengistu.readtome;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/13/15.
 */
public class WordPlayer {

    private MediaPlayer mWordPlayer;

    public static MediaPlayer getMediaPlayer(Context context) {

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

    public void stop() {
        if (mWordPlayer != null) {
            mWordPlayer.release();
            mWordPlayer = null;
        }
    }

    public void play(final Context c, ArrayList<String> words) {
        stop();
        if (words != null && words.size() > 0) {
            final ArrayList<String> wordList = new ArrayList<String>(words);

            String wordAudio = wordList.get(0);
            wordList.remove(0);

            mWordPlayer = MediaPlayer.create(c, getSongResourceId(wordAudio));

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
                    stop();
                    // Recursively call the play() method with one less
                    // track in the list.
                    play(c, wordList);
                }
            });
        }
    }

    public int getSongResourceId(String word) {
        int wordAudio = 0;
        if (word.equalsIgnoreCase("george.")) {
            wordAudio = R.raw.word_george1;
        } else if (word.equalsIgnoreCase("is")) {
            wordAudio = R.raw.word_is;
        } else if (word.equalsIgnoreCase("this")) {
            wordAudio = R.raw.word_this;
        }

        return wordAudio;
    }
}