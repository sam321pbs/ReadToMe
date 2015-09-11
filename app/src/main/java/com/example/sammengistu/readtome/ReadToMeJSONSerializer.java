package com.example.sammengistu.readtome;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by SamMengistu on 9/11/15.
 */
public class ReadToMeJSONSerializer {

    private Context mContext;
    private String mFileName;

    public ReadToMeJSONSerializer (Context context, String fileName){
        mContext = context;
        mFileName = fileName;
    }

    public void savePreferences(SettingsPreferences settingsPreferences) throws JSONException, IOException {
        //Builds an array in JSON
        JSONObject jsonObject = settingsPreferences.preferenceToPageToJSON();

        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext
                    .openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonObject.toString());
            Log.i("JSONSERIALIZER ", "Save -- Voice speed - " + settingsPreferences.getVoiceSpeed());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public SettingsPreferences loadSettings () throws IOException, JSONException {
        SettingsPreferences settingsPreferences = new SettingsPreferences(0, true, 20);
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Parse the JSON using JSONTokener
            JSONObject array = (JSONObject) new JSONTokener(jsonString.toString())
                    .nextValue();
            //Build the Array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++){
                settingsPreferences = new SettingsPreferences(array);

            }
        }
        catch (FileNotFoundException e){
            //Ignore this one; it happens when starting fresh
        }
        finally {
            if(reader != null){
                reader.close();
            }

        }
        Log.i("JSONSERIALIZER ", "Load -- Voice speed - " + settingsPreferences.getVoiceSpeed());
        return settingsPreferences;
    }
}
