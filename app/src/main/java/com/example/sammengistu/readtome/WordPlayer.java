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
            final ArrayList<String> wordsToPlay = new ArrayList<String>(words);

            String word = wordsToPlay.get(0);
            wordsToPlay.remove(0);

            mWordPlayer = MediaPlayer.create(c, WordAudioFiles.get(c).getWordAudio(word));

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
                    play(c, wordsToPlay);
                }
            });
        }
    }
}