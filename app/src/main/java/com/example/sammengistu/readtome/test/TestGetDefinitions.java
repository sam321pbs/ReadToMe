package com.example.sammengistu.readtome.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.example.sammengistu.readtome.WordLinkedWithDef;
import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Tests the first 10 words from the Dictionary file
 * Created by SamMengistu on 9/21/15.
 */
public class TestGetDefinitions extends ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo solo;
    MyLibraryActivity mMyLibraryActivity;
    private ArrayList<WordLinkedWithDef> mWordLinkedWithDefs;

    private Context mContext;
    private final int OFF_BY_ONE = 1;


    public TestGetDefinitions() {
        super(MyLibraryActivity.class);
    }

    /**
     * Sets up dictionary
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        solo = new Solo(getInstrumentation(), getActivity());
        mWordLinkedWithDefs = WordLinkedWithDef.linkWordsWithDefinitions(mContext, 0 , 10);
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /**
     * Actual tests
     */


    /**
     * /things/ - Plural form of thing.
     * @throws Exception
     */
    public void testFindWordThings() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "things");

        assertEquals(wordLinkedWithDef.getWord(), "things");

    }
    public void testFindDefintionThings() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "things");

        assertEquals(wordLinkedWithDef.getDefinition(), "- Plural form of thing.");
    }


    /**
     * /and/ - Together with or along with; in addition to; as well as. Used to connect words, phrases, or clauses that have the same grammatical function in a construction.
     * Test and
     * @throws Exception
     */
    public void testFindWordAnd() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "and");

        assertEquals(wordLinkedWithDef.getWord(), "and");

    }
    public void testFindDefintionAnd() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "and");

        assertEquals(wordLinkedWithDef.getDefinition(),
                "- Together with or along with; in addition to; as well as. Used to connect words, phrases, or clauses that have the same grammatical function in a construction.");
    }

    /**
     * /first/ - The ordinal number matching the number one in a series.
     * Test first
     * @throws Exception
     */
    public void testFindWordFirst() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "first");

        assertEquals(wordLinkedWithDef.getWord(), "first");

    }
    public void testFindDefintionFirst() throws Exception {

        WordLinkedWithDef wordLinkedWithDef = WordLinkedWithDef.findDefinition(mWordLinkedWithDefs,
                "first");

        assertEquals(wordLinkedWithDef.getDefinition(),
                "- The ordinal number matching the number one in a series.");
    }

}
