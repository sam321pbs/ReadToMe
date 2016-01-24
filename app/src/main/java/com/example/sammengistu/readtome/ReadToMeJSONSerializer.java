package com.example.sammengistu.readtome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReadToMeJSONSerializer {

    private Context mContext;
    private String mFileName;

    public ReadToMeJSONSerializer (Context context, String fileName){
        mContext = context;
        mFileName = fileName;
    }

    public void saveBookMarks(Map<String, Object> bookMarks) throws JSONException, IOException {
        //Builds an array in JSON
        JSONObject jsonObject = new JSONObject(bookMarks);

        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext
                .openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonObject.toString());

        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public Map<String, Object> loadBookMarks () throws IOException, JSONException {
        Map<String,Object> bookMarks = new HashMap<>();
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

            bookMarks = jsonToMap(array);

        }
        catch (FileNotFoundException e){
            //Ignore this
        }
        finally {
            if(reader != null){
                reader.close();
            }
        }
        return bookMarks;
    }



    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
