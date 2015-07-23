package com.example.sammengistu.readtome.bookpages;

import android.content.Context;
import android.util.Log;

import com.example.sammengistu.readtome.MakeAPage;
import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/16/15.
 */
public class CharlottesWebPages implements MakeAPage {
    public static ArrayList<PageOfBook> mPagesOfTheBook = new ArrayList<PageOfBook>();

    private Context mContext;

    public CharlottesWebPages(Context c) {
        mContext = c;
    }

    public ArrayList<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private void addPages() {


        StringBuilder page1 = new StringBuilder();
        StringBuilder page2 = new StringBuilder();
        try {
            BufferedReader charlottesWeb = new BufferedReader(new InputStreamReader(
                    mContext.getResources().openRawResource(R.raw.charlottes_web_story)));

            String inputString;
            StringBuilder stringBuffer = new StringBuilder();
            while ((inputString = charlottesWeb.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
            String[] allWordsFromFirstTwoPages = stringBuffer.toString().split("\\s+");
            for (int i = 0; i < allWordsFromFirstTwoPages.length; i++) {
                if (i < 144) {
                    page1.append(allWordsFromFirstTwoPages[i] + " ");
                } else {
                    page2.append(allWordsFromFirstTwoPages[i] + " ");
                }
            }
            Log.i("charlottesWeb", page1.toString());
        } catch (IOException e)

        {
            e.printStackTrace();
        }


        PageOfBook pageOne = new PageOfBook(R.drawable.curious_geroge_page_1,
                page1.toString(),
                1);

        PageOfBook pageTwo = new PageOfBook(R.drawable.curious_george_page_2,
                page2.toString(), 2);

        mPagesOfTheBook.add(pageOne);
        mPagesOfTheBook.add(pageTwo);
    }
}
