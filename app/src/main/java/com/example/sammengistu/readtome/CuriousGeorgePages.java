package com.example.sammengistu.readtome;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/11/15.
 */
public class CuriousGeorgePages {

    public static ArrayList<PageOfBook> mPagesOfTheBook = new ArrayList<PageOfBook>();

    public static ArrayList<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private static void addPages() {

        PageOfBook pageOne = new PageOfBook(R.drawable.curious_geroge_page_1,
                "This is George. He lived in Africa. He was very happy." +
                        " But he had one fault. He was too curious.",
                1);

        PageOfBook pageTwo = new PageOfBook(R.drawable.curious_george_page_2,
                "One day George saw a man. He had on a large yellow straw hat." +
                " The man saw George, too. What a nice little monkey, he thought," +
                " I would like to take him home with me. The man put his hat on the ground, " +
                "and of course George was curious. " +
                "He came down from the tree " +
                "to look at the large yellow hat.", 2);

        mPagesOfTheBook.add(pageOne);
        mPagesOfTheBook.add(pageTwo);
    }
}
