package com.example.sammengistu.readtome;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 9/10/15.
 */
public class DefinitionsLab {

    private ArrayList<WordLinkedWithDef> mWordLinkedWithDefs;
    private static DefinitionsLab sDefinitionsLab;
    private Context mAppContext;

    private DefinitionsLab (Context appContext){
        mAppContext = appContext;
    }

    public static DefinitionsLab get(Context c){
        if (sDefinitionsLab == null){
            sDefinitionsLab = new DefinitionsLab(c.getApplicationContext());
        }

        return sDefinitionsLab;
    }

    public ArrayList<WordLinkedWithDef> getDefinitions(){
        return mWordLinkedWithDefs;
    }

//    public WordLinkedWithDef getDefinition(){
//
//    }
}
