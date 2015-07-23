package com.example.sammengistu.readtome;

import android.content.Context;

import java.util.ArrayList;

/**
 * Stores all the audio files into one location
 * and makes it easy to access the files
 */
public class WordAudioFiles {
    private ArrayList<WordAndAudio> mWordAudioFiles = new ArrayList<>();

    private static WordAudioFiles sWordAudioFiles;


    /**
     * Adds the files to the list when it is created
     *
     * @param c eeeeeeeeeeeeeee
     */
    public WordAudioFiles(Context c) {
        mWordAudioFiles = new ArrayList<>();
        addWordsToAudioList();

    }

    public static WordAudioFiles get(Context c) {
        if (sWordAudioFiles == null) {
            sWordAudioFiles = new WordAudioFiles(c.getApplicationContext());
        }
        return sWordAudioFiles;
    }

    /**
     * Adds the words and audio to the ArrayList
     */
    private void addWordsToAudioList() {

        mWordAudioFiles.add(new WordAndAudio("george.", R.raw.word_george));
        mWordAudioFiles.add(new WordAndAudio("is", R.raw.word_is));
        mWordAudioFiles.add(new WordAndAudio("this", R.raw.word_this));
        mWordAudioFiles.add(new WordAndAudio("africa.", R.raw.word_africa));
        mWordAudioFiles.add(new WordAndAudio("but", R.raw.word_but));
        mWordAudioFiles.add(new WordAndAudio("curious.", R.raw.word_curious));
        mWordAudioFiles.add(new WordAndAudio("fault.", R.raw.word_fault));
        mWordAudioFiles.add(new WordAndAudio("had", R.raw.word_had));
        mWordAudioFiles.add(new WordAndAudio("happy.", R.raw.word_happy));
        mWordAudioFiles.add(new WordAndAudio("he", R.raw.word_he));
        mWordAudioFiles.add(new WordAndAudio("in", R.raw.word_in));
        mWordAudioFiles.add(new WordAndAudio("lived", R.raw.word_lived));
        mWordAudioFiles.add(new WordAndAudio("one", R.raw.word_one));
        mWordAudioFiles.add(new WordAndAudio("too", R.raw.word_too));
        mWordAudioFiles.add(new WordAndAudio("very", R.raw.word_very));
        mWordAudioFiles.add(new WordAndAudio("a", R.raw.word_a));
        mWordAudioFiles.add(new WordAndAudio("came", R.raw.word_came));
        mWordAudioFiles.add(new WordAndAudio("day", R.raw.word_day));
        mWordAudioFiles.add(new WordAndAudio("ground", R.raw.word_ground));
        mWordAudioFiles.add(new WordAndAudio("hat", R.raw.word_hat));
        mWordAudioFiles.add(new WordAndAudio("home", R.raw.word_home));
        mWordAudioFiles.add(new WordAndAudio("large", R.raw.word_large));
        mWordAudioFiles.add(new WordAndAudio("little", R.raw.word_little));
        mWordAudioFiles.add(new WordAndAudio("man", R.raw.word_man));
        mWordAudioFiles.add(new WordAndAudio("nice", R.raw.word_nice));
        mWordAudioFiles.add(new WordAndAudio("saw", R.raw.word_saw));
        mWordAudioFiles.add(new WordAndAudio("straw", R.raw.word_straw));
        mWordAudioFiles.add(new WordAndAudio("the", R.raw.word_the));
        mWordAudioFiles.add(new WordAndAudio("what", R.raw.word_what));
        mWordAudioFiles.add(new WordAndAudio("yellow", R.raw.word_yellow));
        mWordAudioFiles.add(new WordAndAudio("was", R.raw.word_was));

    }

    /**
     * Gets the audio for a string word
     *
     * @param word
     * @return
     */
    public int getWordAudio(String word) {
        for (WordAndAudio audio : mWordAudioFiles) {
            if (word.equalsIgnoreCase(audio.getWord())) {
                return audio.getAudio();
            }
        }
        return 0;
    }

    public boolean isWordInFiles(String word) {
        for (WordAndAudio wordInFile : mWordAudioFiles) {
            if (wordInFile.getWord().equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

}
