package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;


public class DefinitionDialog extends DialogFragment {

    public static final String FIND_WORD_DEFINITION = "find word definition";
    public static final String DEFINITION = "definition";
    private static final String TAG = "DefinitionDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String findWord = getArguments().getString(FIND_WORD_DEFINITION);
        String definitionOfWord = getArguments().getString(DEFINITION);

        char[] array = new char[0];
        if (findWord != null) {
            array = findWord.toCharArray();
        }
        String updatedLetter = Character.toString(array[0]).toUpperCase();

        View v = getActivity().getLayoutInflater()
            .inflate(R.layout.definition_dialog, null);

        TextView wordTextView = (TextView) v.findViewById(R.id.dialog_word_title);
        wordTextView.setTextColor(Color.BLACK);

        TextView definitionView = (TextView) v.findViewById(R.id.dialog_word_defintion_box);
        definitionView.setTextColor(Color.BLACK);

        String cleanedUpWord = updatedLetter +
            (findWord != null ? findWord.substring(1) : "Error");
        wordTextView.setText(cleanedUpWord);

        definitionView.setText(definitionOfWord);

        return new AlertDialog.Builder(getActivity())
            .setView(v)
            .create();
    }

    /**
     * Removes all punctuation marks from word
     * @param wordIn - word to remove punctuation marks from
     * @return - new word without punctuations
     */
    public static String removePunctuations(String wordIn) {
        StringBuilder cleanedWord = new StringBuilder();

        char[] currentWord = wordIn.toCharArray();

        for (char letter : currentWord) {
            if (Character.isLetter(letter)) {
                cleanedWord.append(letter);
            }
        }
        return cleanedWord.toString();
    }

    /**
     * Brings in word that is being defined and is used in the dialog as a label
     * and takes in the words definition and puts it in the definition box
     * @param word - word that is being defined
     * @param definition - words definition
     * @return - instance of the fragment
     */
    public static DefinitionDialog newInstance(String word, String definition) {
        Bundle args = new Bundle();
        args.putString(FIND_WORD_DEFINITION, word);
        args.putString(DEFINITION, definition);

        DefinitionDialog definitionDialog = new DefinitionDialog();
        definitionDialog.setArguments(args);

        return definitionDialog;
    }
}
