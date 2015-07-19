package com.example.sammengistu.readtome;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/18/15.
 */
public class WordAudioFiles {
    private ArrayList<WordAndAudio> mWordAudioFiles = new ArrayList<>();

    private static WordAudioFiles sWordAudioFiles;


    public WordAudioFiles(Context c){
        mWordAudioFiles = new ArrayList<>();
        addWordsToAudioList();

    }

    public static WordAudioFiles get(Context c) {
        if (sWordAudioFiles == null){
            sWordAudioFiles = new WordAudioFiles(c.getApplicationContext());
        }
        return sWordAudioFiles;
    }

    private void addWordsToAudioList(){

        mWordAudioFiles.add(new WordAndAudio("george.", R.raw.word_george));
        mWordAudioFiles.add(new WordAndAudio("is", R.raw.word_is));
        mWordAudioFiles.add(new WordAndAudio("this", R.raw.word_this));
        mWordAudioFiles.add(new WordAndAudio("africa.", R.raw.word_africa));
        mWordAudioFiles.add(new WordAndAudio("but", R.raw.word_but));
        mWordAudioFiles.add(new WordAndAudio("curious.", R.raw.word_curious));
        mWordAudioFiles.add(new WordAndAudio("fault.",R.raw.word_fault));
        mWordAudioFiles.add(new WordAndAudio("had", R.raw.word_had));
        mWordAudioFiles.add(new WordAndAudio("happy.",  R.raw.word_happy));
        mWordAudioFiles.add(new WordAndAudio("he", R.raw.word_he));
        mWordAudioFiles.add(new WordAndAudio("in",  R.raw.word_in));
        mWordAudioFiles.add(new WordAndAudio("lived", R.raw.word_lived));
        mWordAudioFiles.add(new WordAndAudio("one", R.raw.word_one));
        mWordAudioFiles.add(new WordAndAudio("too",R.raw.word_too));
        mWordAudioFiles.add(new WordAndAudio("very",R.raw.word_very));
        mWordAudioFiles.add(new WordAndAudio("was", R.raw.word_was));

    }

    public int getWordAudio(String word){
        for (WordAndAudio audio: mWordAudioFiles){
            if (word.equalsIgnoreCase(audio.getWord())){
                return audio.getAudio();
            }
        }
        return 0;
    }

    public boolean isWordInFiles(String word){
        for (WordAndAudio wordInFile: mWordAudioFiles){
            if (wordInFile.getWord().equalsIgnoreCase(word)){
                return true;
            }
        }
        return false;
    }

}
