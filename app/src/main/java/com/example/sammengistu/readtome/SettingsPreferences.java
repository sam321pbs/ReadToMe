package com.example.sammengistu.readtome;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsPreferences {

    private static final String JSON_BOOKMARK = "JSON Bookmarked page";
    private static final String JSON_READ_SENTENCE_MODE = "json read sentence mode";
    private static final String JSON_VOICE_SPEED = "json voice speed";

    private boolean mReadSentenceMode;
    private int mVoiceSpeed;


    public SettingsPreferences( boolean readSentenceMode,
                               int voiceSpeed) {
        mReadSentenceMode = readSentenceMode;
        mVoiceSpeed = voiceSpeed;
    }

    public SettingsPreferences() {
        this(true, 20);
    }

    public SettingsPreferences(JSONObject json) throws JSONException {

        if (json.has(JSON_READ_SENTENCE_MODE)) {
            mReadSentenceMode = json.getBoolean(JSON_READ_SENTENCE_MODE);
        }
        if (json.has(JSON_VOICE_SPEED)) {
            mVoiceSpeed = json.getInt(JSON_VOICE_SPEED);
        }
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
        jsonObject.put(JSON_READ_SENTENCE_MODE, mReadSentenceMode);
        jsonObject.put(JSON_VOICE_SPEED, mVoiceSpeed);

        return jsonObject;
    }
}
