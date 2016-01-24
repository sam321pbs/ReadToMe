package com.example.sammengistu.readtome.test;

import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by SamMengistu on 9/18/15.
 */
public class TestReadFromFile extends ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;

    private Context mContext;
    private final int OFF_BY_ONE = 1;


    public TestReadFromFile() {
        super(MyLibraryActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    /**
     * Actual tests
     */


//    public void testReadFirstLine() throws Exception {
//
//        if (mContext != null) {
//            assertEquals(WordLinkedWithDef.readLine(0, mContext),
//                    "/things/ - Plural form of thing.");
//        } else {
//            Log.i("Test", " it equals null");
//        }
//
//    }
//
//    public void testReadLineThirtyEight() throws Exception {
//
//        if (mContext != null) {
//            assertEquals(WordLinkedWithDef.readLine(38 - OFF_BY_ONE, mContext),
//                    "/nine/ - The cardinal number equal to 8 + 1.");
//        } else {
//            Log.i("Test", " it equals null");
//        }
//    }
//
//    public void testReadLine1384() throws Exception {
//
//        if (mContext != null) {
//            assertEquals(WordLinkedWithDef.readLine(1384 - OFF_BY_ONE, mContext),
//                    "/wishes/ - Plural form of wish.");
//        } else {
//            Log.i("Test", " it equals null");
//        }
//    }
//
//    public void testReadLine3888() throws Exception {
//
//        if (mContext != null) {
//            assertEquals(WordLinkedWithDef.readLine(3888 - OFF_BY_ONE, mContext),
//                    "/messengers/ - plural of messenger");
//        } else {
//            Log.i("Test", " it equals null");
//        }
//    }

}
