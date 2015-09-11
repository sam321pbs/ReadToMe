package com.example.sammengistu.readtome;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SamMengistu on 9/11/15.
 */
public class SettingsPreferences {

    private static final String SENTENCE_READING_MODE = "Sentence reading mode";
    private static final String VOICE_SPEED_FOR_SETTINGS = "Voice Speed settings";
    private static final String BOOKMARKED_PAGE = "Bookmarked page";
    private static final String JSON_BOOKMARK = "JSON Bookmarked page";
    private static final String JSON_READ_SENTENCE_MODE = "json read sentence mode";
    private static final String JSON_VOICE_SPEED = "json voice speed";
    private int mBookMarkedPage;
    private boolean mReadSentenceMode;
    private int mVoiceSpeed;

    public SettingsPreferences (int bookMarkedPage, boolean readSentenceMode,
                                int voiceSpeed){
        mBookMarkedPage = bookMarkedPage;
        mReadSentenceMode = readSentenceMode;
        mVoiceSpeed = voiceSpeed;
    }

    public SettingsPreferences (){
        this(0, true, 20);
    }

    public SettingsPreferences (JSONObject json) throws JSONException {
        if (json.has(JSON_BOOKMARK)){
            mBookMarkedPage = json.getInt(JSON_BOOKMARK);
        }
        if (json.has(JSON_READ_SENTENCE_MODE)){
            mReadSentenceMode = json.getBoolean(JSON_READ_SENTENCE_MODE);
        }
        if (json.has(JSON_VOICE_SPEED)){
            mVoiceSpeed = json.getInt(JSON_VOICE_SPEED);
        }
    }

    public int getBookMarkedPage() {
        return mBookMarkedPage;
    }

    public void setBookMarkedPage(int bookMarkedPage) {
        mBookMarkedPage = bookMarkedPage;
    }

    public boolean isReadSentenceMode() {
        return mReadSentenceMode;
    }

    public void setReadSentenceMode(boolean readSentenceMode) {
        mReadSentenceMode = readSentenceMode;
    }

    public int getVoiceSpeed() {
        return mVoiceSpeed;
    }

    public void setVoiceSpeed(int voiceSpeed) {
        mVoiceSpeed = voiceSpeed;
    }

    public JSONObject preferenceToPageToJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_BOOKMARK, mBookMarkedPage);
        jsonObject.put(JSON_READ_SENTENCE_MODE, mReadSentenceMode);
        jsonObject.put(JSON_VOICE_SPEED, mVoiceSpeed);

        return jsonObject;
    }
}
