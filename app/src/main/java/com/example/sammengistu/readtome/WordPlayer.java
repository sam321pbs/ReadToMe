package com.example.sammengistu.readtome;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/13/15.
 */
public class WordPlayer {

    private MediaPlayer mWordPlayer;

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
            wordAudio = R.raw.word_george;
        } else if (word.equalsIgnoreCase("is")) {
            wordAudio = R.raw.word_is;
        } else if (word.equalsIgnoreCase("this")) {
            wordAudio = R.raw.word_this;
        } else if (word.equalsIgnoreCase("africa.")) {
            wordAudio = R.raw.word_africa;
        }else if (word.equalsIgnoreCase("but")) {
            wordAudio = R.raw.word_but;
        }else if (word.equalsIgnoreCase("curious.")) {
            wordAudio = R.raw.word_curious;
        }else if (word.equalsIgnoreCase("fault.")) {
            wordAudio = R.raw.word_fault;
        }else if (word.equalsIgnoreCase("had")) {
            wordAudio = R.raw.word_had;
        }else if (word.equalsIgnoreCase("happy.")) {
            wordAudio = R.raw.word_happy;
        }else if (word.equalsIgnoreCase("he")) {
            wordAudio = R.raw.word_he;
        }else if (word.equalsIgnoreCase("in")) {
            wordAudio = R.raw.word_in;
        }else if (word.equalsIgnoreCase("lived")) {
            wordAudio = R.raw.word_lived;
        }else if (word.equalsIgnoreCase("one")) {
            wordAudio = R.raw.word_one;
        }else if (word.equalsIgnoreCase("too")) {
            wordAudio = R.raw.word_too;
        }else if (word.equalsIgnoreCase("very")) {
            wordAudio = R.raw.word_very;
        }else if (word.equalsIgnoreCase("was")) {
            wordAudio = R.raw.word_was;
        }

        return wordAudio;
    }
}