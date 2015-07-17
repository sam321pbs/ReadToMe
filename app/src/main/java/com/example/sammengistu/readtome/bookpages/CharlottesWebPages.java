package com.example.sammengistu.readtome.bookpages;

import com.example.sammengistu.readtome.MakeAPage;
import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 7/16/15.
 */
public class CharlottesWebPages implements MakeAPage {
    public static ArrayList<PageOfBook> mPagesOfTheBook = new ArrayList<PageOfBook>();

    public  ArrayList<PageOfBook> getPagesOfTheBook() {
        addPages();
        return mPagesOfTheBook;
    }

    private static void addPages() {

        PageOfBook pageOne = new PageOfBook(R.drawable.curious_geroge_page_1,
                        "Before Breakfast... " +
                        "Where's Papa going with that ax?\" said Fern to her mother as they were " +
                        "setting the table for breakfast. " +
                        "\"Out to the hoghouse,\" replied Mrs. Arable. \"Some pigs were born last " +
                        "night.\" " +
                        "\"I don't see why he needs an ax,\" continued Fern, who was only eight. " +
                        "\"Well,\" said her mother, \"one of the pigs is a runt. It's very small " +
                        "and weak, and it will never amount to anything. So your father has " +
                        "decided to do away with it.\" " +
                        "\"Do away with it?\" shrieked Fern. \"You mean kill it? Just because it's " +
                        "smaller than the others?\" " +
                        "Mrs. Arable put a pitcher of cream on the table. \"Don't yell, Fern!\" " +
                        "she said. \"Your father is right. The pig would probably die anyway.\" " +
                        "Fern pushed a chair out of the way and ran outdoors. The grass was " +
                        "wet " +
                        "and the earth smelled of springtime. Fern's sneakers were sopping by " +
                        "the time she caught up with her father. " +
                        "\"Please don't kill it!\" she sobbed. \"It's unfair.\"",
                1);

        PageOfBook pageTwo = new PageOfBook(R.drawable.curious_george_page_2,
                "Mr. Arable stopped walking.\n" +
                        "\"Fern,\" he said gently, \"you will have to learn to control yourself.\" " +
                        "\"Control myself?\" yelled Fern. \"This is a matter of life and death, and " +
                        "you talk about _controlling myself.\" Tears ran down her cheeks and she " +
                        "took hold of the ax and tried to pull it out of her father's hand. " +
                        "\"Fern,\" said Mr. Arable, \"I know more about raising a litter of pigs " +
                        "than you do. A weakling makes trouble. Now run along!\" " +
                        "\"But it's unfair,\" cried Fern. \"The pig couldn't help being born small, " +
                        "could it? If I had been very small at birth, would you have killed me?\" " +
                        "Mr. Arable smiled. \"Certainly not,\" he said, looking down at his " +
                        "daughter with love. \"But this is different. A little girl is one " +
                        "thing, a little runty pig is another.\" " +
                        "\"I see no difference,\" replied Fern, still hanging on to the ax. \"This " +
                        "is the most terrible case of injustice I ever heard of.\" " +
                        "A queer look came over John Arable's face. He seemed almost ready " +
                        "to " +
                        "cry himself. " +
                        "\"All right,\" he said. \"You go back to the house and I will bring the " +
                        "runt when I come in. I'll let you start it on a bottle, like a baby. " +
                        "Then you'll see what trouble a pig can be.\" " +
                        "When Mr. Arable returned to the house half an hour later, he carried a", 2);

        mPagesOfTheBook.add(pageOne);
        mPagesOfTheBook.add(pageTwo);
    }
}
