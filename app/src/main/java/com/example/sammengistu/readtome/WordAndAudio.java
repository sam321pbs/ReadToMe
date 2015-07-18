package com.example.sammengistu.readtome;

/**
 * Created by SamMengistu on 7/18/15.
 */
public class WordAndAudio {
    private String mWord;
    private int mAudioId;


    public WordAndAudio(String word, int audio){
        mWord = word;
        mAudioId = audio;
    }

    public String getWord() {

        return mWord;
    }

    public int getAudio() {
        return mAudioId;
    }
}
