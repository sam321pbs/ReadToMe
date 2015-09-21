package com.example.sammengistu.readtome.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sammengistu.readtome.R;
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

/**
 * Created by SamMengistu on 8/10/15.
 */
public class DefinitionDialog extends DialogFragment {

    public static final String FIND_WORD_DEFINITION = "find word defintion";
    public static final String DEFINITION = "definition";
    private static final String TAG = "DefinitionDialog";
    private TextView mDefintion;
    private TextView mWord;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String findWord = getArguments().getString(FIND_WORD_DEFINITION);
        Log.i(TAG, findWord);
        String definition = getArguments().getString(DEFINITION);
        Log.i(TAG, definition);

        Log.i(TAG, "word is " + findWord );
        //Log.i(TAG, "definition is " + definition);
        char [] array = findWord.toCharArray();
        String updatedLetter = Character.toString(array[0]).toUpperCase();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(updatedLetter);
        stringBuilder.append(findWord.substring(1));

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.definition_dialog, null);

        mWord = (TextView)v.findViewById(R.id.dialog_word_title);
        mWord.setTextColor(Color.BLACK);

        mDefintion = (TextView)v.findViewById(R.id.dialog_word_defintion_box);
        mDefintion.setTextColor(Color.BLACK);

        //WordLinkedWithDef.findDefinition(getActivity(),)
        mWord.setText(stringBuilder.toString());

        mDefintion.setText(definition);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    public static String removePunctuations(String wordIn){
        StringBuilder cleanedWord = new StringBuilder();

        char [] currentWord = wordIn.toCharArray();

        for (char letter : currentWord){
            if (Character.isLetter(letter)){
                cleanedWord.append(letter);
            }
        }
        return cleanedWord.toString();
    }

    public void findDefinition(String word){
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

            final String jsonData = response.body().string();

            if (response.isSuccessful()) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mDefintion.setText(parseJSONForDefinition(jsonData));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    });

}

    public static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput("dictionary_words_and_def.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String parseJSONForDefinition(String jsonData) throws JSONException {

        JSONArray arr  = new JSONArray(jsonData);

        Log.i(TAG, ((JSONObject) arr.get(0)).get("text") + "");

        return ((JSONObject)arr.get(0)).get("text") + "";
    }



    public static DefinitionDialog newInstance(String word, String definition){
        Bundle args = new Bundle();
        args.putString(FIND_WORD_DEFINITION, word);
        args.putString(DEFINITION, definition);

        DefinitionDialog definitionDialog = new DefinitionDialog();
        definitionDialog.setArguments(args);

        return definitionDialog;
    }
}
