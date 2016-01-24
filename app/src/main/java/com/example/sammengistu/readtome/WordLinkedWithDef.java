package com.example.sammengistu.readtome;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class WordLinkedWithDef {

    public List<WordLinkedWithDef> mWordsAndDef;

    private String mWord;
    private String mDefinition;
    private boolean mStopLoop;

    public WordLinkedWithDef(){
        mStopLoop = false;
    }

    public WordLinkedWithDef(String word, String definition) {
        mWord = word;
        mDefinition = definition;
    }

    public void setmStopLoop(boolean stopLoop) {
        mStopLoop = stopLoop;
    }

    /**
     * Creates an Object (WordLinked with defintion) pairs of word - defintion ()
     *
     * @param context- to get access of the deictionary file
     * @param start    - Where to start reading the file
     * @param end      - Where to stop reading the file
     * @return - List of WordLinkedWithDefinitions
     */
    public List<WordLinkedWithDef> linkWordsWithDefinitions(Context context, int start,
                                                                   int end) {
        mWordsAndDef = new ArrayList<>();

        for (int i = start; i < end; i++) {

            //Gets the entire line from the file
            String entireLine;

            if (mStopLoop){
                break;
            }

            try {
                entireLine = readLine(i, context);
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


    /**
     * Reads a line from the file and converts it to a string
     *
     * @param line    - line number you want to read
     * @param context - to get access to the file
     * @return - the string of the specified line
     */
    public String readLine(int line, Context context) throws IOException {

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
            r.close();
        }
        return "Error";
    }

    /**
     * Finds a defintion of a word bys searching through the ArrayList<WordLinkedWithDef>
     * and returns the Defintion
     *
     * @param wordLinkedWithDefs - ArrayList of where to search
     * @param findWord           - word they are looking for
     * @return - returns the definion
     */
    public WordLinkedWithDef findDefinition(
        List<WordLinkedWithDef> wordLinkedWithDefs,
        String findWord) {

        WordLinkedWithDef found = new WordLinkedWithDef("Error",
            "Sorry, couldn't find that word");

        for (WordLinkedWithDef word : wordLinkedWithDefs) {
            if (findWord.equalsIgnoreCase(word.getWord())) {
                found = word;
            }
        }
        return found;
    }

    public String getWord() {
        return mWord;
    }


    public String getDefinition() {
        return mDefinition;
    }

}
