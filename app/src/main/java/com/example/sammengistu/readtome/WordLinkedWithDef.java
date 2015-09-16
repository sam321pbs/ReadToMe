package com.example.sammengistu.readtome;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SamMengistu on 9/10/15.
 */
public class WordLinkedWithDef {

    public static ArrayList<WordLinkedWithDef> mWordsAndDef;

    private String mWord;
    private String mDefinition;

    public WordLinkedWithDef(String word, String definition) {
        mWord = word;
        mDefinition = definition;
    }

    public static ArrayList<WordLinkedWithDef> linkWordsWithDefinitions(Context context, int start,
                                                                        int end) {
        mWordsAndDef = new ArrayList<>();

        for (int i = start; i < end; i++) {

            //Gets the entire line from the file
            String entireLine = "";

            try {
                entireLine = WordLinkedWithDef.readLine(i, context);
            } catch (IOException e) {
                entireLine = "";
            }

            char[] letters = entireLine.toCharArray();

            int firstSlash = 0;
            int counter = 0;

            StringBuilder createdWord = new StringBuilder();

            StringBuilder definition = new StringBuilder();

            for (char letter : letters) {
                if (letter == '/') {
                    counter++;
                    firstSlash++;
                }
                // Starts building the definition
                else if (firstSlash == 2) {
                    definition.append(entireLine.substring(counter + 1));
                    break;
                }
                // Builds the word
                else {
                    counter++;
                    createdWord.append(letter);

                }
            }

            WordLinkedWithDef wordLinkedWithDef = new WordLinkedWithDef(
                    createdWord.toString(), definition.toString());
            mWordsAndDef.add(wordLinkedWithDef);
        }

        return mWordsAndDef;
    }


    public static String readLine(int line, Context context) throws IOException {

        InputStream in = context.getResources().openRawResource(R.raw.dictionary_words_and_def);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));

        try {

            String lineStr;
            int currentLine = 0;

            while ((lineStr = r.readLine()) != null) {

                if (currentLine++ == line) {
                    return lineStr;
                }

            }
        } catch (IOException e) {

        } finally {
            if (r != null) {
                r.close();
            }
        }
        return "didnt work";
    }

    public static WordLinkedWithDef findDefinition(
            ArrayList<WordLinkedWithDef> wordLinkedWithDefs,
            String findWord) {

        WordLinkedWithDef found = new WordLinkedWithDef("There was an error", "Sorry");

        for (WordLinkedWithDef word : wordLinkedWithDefs) {
            if (findWord.equalsIgnoreCase(word.getWord())) {
                found = word;
            }
        }
        return found;
    }

    public static String findDefFromFile (String word, Context context){

        for (int i = 0; i < 4677; i ++) {
            String entireLine = "";

            try {
                entireLine = WordLinkedWithDef.readLine(i, context);
            } catch (IOException e) {
                entireLine = "";
            }

            char[] letters = entireLine.toCharArray();

            int firstSlash = 0;
            int counter = 0;

            StringBuilder createdWord = new StringBuilder();

            StringBuilder definition = new StringBuilder();

            for (char letter : letters) {
                if (letter == '/') {
                    counter++;
                    firstSlash++;
                }
                // Starts building the definition
                else if (firstSlash == 2) {
                    definition.append(entireLine.substring(counter + 1));
                    break;
                }
                // Builds the word
                else {
                    counter++;
                    createdWord.append(letter);

                }
            }

            if (word.equals(createdWord.toString())){
                return definition.toString();
            }
            }
        return "Couldn't find it";
    }


    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getDefinition() {
        return mDefinition;
    }

}
