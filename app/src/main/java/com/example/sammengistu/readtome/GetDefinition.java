package com.example.sammengistu.readtome;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by SamMengistu on 8/8/15.
 */
public class GetDefinition {

    private static final String TAG = "GetDefinition";
    private static String mDefinition;
    static String[] def = new String[1];
    static ArrayList<String> o;
    private static Context mContext;

    public static String getDef(String word, Context context) {
        //findDefinition(word);
        return def[0];
    }

    public static void setDef(String def) {
        GetDefinition.def[0] = def;
    }

    public static void findDefinition(String word){

        // String[] def = new String[1];
             Request request = new Request.Builder()
                    .url("http://api.wordnik.com/v4/word.json/" +
                            word.toLowerCase() +
                            "/definitions?api_key=15dd8a92ea05080a54001050f1b0d798d2e96372d058cd735")
                    .build();

            // http://api.wordnik.com/v4/word.json/car/definitions?api_key=15dd8a92ea05080a54001050f1b0d798d2e96372d058cd735

            OkHttpClient client = new OkHttpClient();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.i(TAG, e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String jsonData = response.body().string();
                    Log.i(TAG, jsonData);

                        if (response.isSuccessful()) {
                            try {
                                GetDefinition.setDef(parseJSONForDefinition(jsonData));
                                GetDefinition.writeToFile(jsonData);
//                                 def[0] = parseJSONForDefinition(jsonData);
//                                Log.i(TAG + "inner class", def[0]);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                }
            });

    }

    public static void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    mContext.openFileOutput("dictionary_word_and_def.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String parseJSONForDefinition(String jsonData) throws JSONException {

        JSONArray arr  = new JSONArray(jsonData);

        Log.i(TAG, ((JSONObject)arr.get(0)).get("text") + "");

        return ((JSONObject)arr.get(0)).get("text") + "";
    }
}
