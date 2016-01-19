package com.example.sammengistu.readtome.test.testtableofcontents;

import com.example.sammengistu.readtome.activities.MyLibraryActivity;
import com.example.sammengistu.readtome.bookpages.EPubFileConverterToBook;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;
import com.example.sammengistu.readtome.models.PageOfBook;
import com.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestTableOfContentsSenseAndSensibility extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    String mBookText = "The family of Dashwood had long been settled in Sussex. Their estate was large, and their residence was at Norland Park, in the centre of their property, where, for many generations, they had lived in so respectable a manner as to engage the general good opinion of their surrounding acquaintance. The late owner of this estate was a single man, who lived to a very advanced age, and who for many years of his life, had a constant companion and housekeeper in his sister. But her death, which happened ten years before his own, produced a great alteration in his home; for to supply her loss, he invited and received into his house the family of his nephew Mr. Henry Dashwood, the legal inheritor of the Norland estate, and the person to whom he intended to bequeath it. In the society of his nephew and niece, and their children," +
        " the old Gentleman's days were comfortably spent. His attachment to them all increased. The constant attention of Mr. and Mrs. Henry Dashwood to his wishes, which proceeded not merely from interest, but from goodness of heart, gave him every degree of solid comfort which his age could receive; and the cheerfulness of the children added a relish to his existence. By a former marriage, Mr. Henry Dashwood had one son: by his present lady, three daughters. The son, a steady respectable young man, was amply provided for by the fortune of his mother, which had been large, and half of which devolved on him on his coming of age. By his own marriage, likewise, which happened soon afterwards, he added to his wealth. To him therefore the succession to the Norland estate was not so really important as to his sisters; for their fortune, independent of what might arise to them from their father's" +
        " inheriting that property, could be but small. Their mother had nothing, and their father only seven thousand pounds in his own disposal; for the remaining moiety of his first wife's fortune was also secured to her child, and he had only a life-interest in it. The old gentleman died: his will was read, and like almost every other will, gave as much disappointment as pleasure. He was neither so unjust, nor so ungrateful, as to leave his estate from his nephew;—but he left it to him on such terms as destroyed half the value of the bequest. Mr. Dashwood had wished for it more for the sake of his wife and daughters than for himself or his" +
        " son;—but to his son, and his son's son, a child of four years old, it was secured, in such a way, as to leave to himself no power of providing for those who were most dear to him, and who most needed a provision by any charge on the estate, or by any sale of its valuable woods. The whole was tied up for the benefit of this child, who, in occasional visits with his father" +
        " and mother at Norland, had so far gained on the affections of his uncle, by such attractions as";
    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookSenseAndSensibility;

    List<PageOfBook> mPageOfBooksSenseAndSensiblity;

    List<String> mChapterNamesSenseAndSensibility;

    private Context mContext;

    public TestTableOfContentsSenseAndSensibility() {
        super(MyLibraryActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        mMyLibraryActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        mSolo = new Solo(getInstrumentation(), getActivity());
        Library library = new Library(getActivity());
        List<Book> allBooks = library.getMyLibrary();

        for (Book book : allBooks){
            Log.i("Test", "File name = " + book.getEPubFile().getName());
            if (book.getEPubFile().getName().equals("sense_and_sensibility.epub")){
                mEPubBookSenseAndSensibility = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }

        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForSenseAndSensibility() throws Exception {
        mChapterNamesSenseAndSensibility = new ArrayList<>();
        mPageOfBooksSenseAndSensiblity = mEPubBookSenseAndSensibility.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksSenseAndSensiblity){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesSenseAndSensibility.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesSenseAndSensibility.get(0), "SENSE AND SENSIBILITY");
        assertEquals(mChapterNamesSenseAndSensibility.get(1), "(1811)");
        assertEquals(mChapterNamesSenseAndSensibility.get(2), "CHAPTER 1");
        assertEquals(mChapterNamesSenseAndSensibility.get(3), "CHAPTER 2");
        assertEquals(mChapterNamesSenseAndSensibility.get(4), "CHAPTER 3");
        assertEquals(mChapterNamesSenseAndSensibility.get(5), "CHAPTER 4");
        assertEquals(mChapterNamesSenseAndSensibility.get(6), "CHAPTER 5");
    }

    public void testFirst50Words()throws Exception {
        mPageOfBooksSenseAndSensiblity = mEPubBookSenseAndSensibility.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        for (PageOfBook pageOfBook : mPageOfBooksSenseAndSensiblity){
            if (pageOfBook.getChapterOfBook().equals("CHAPTER 1")){
                pageTextsb.append(pageOfBook.getPageText());
                break;
            }
        }

        StringBuilder fiftyWords = new StringBuilder();
        StringBuilder actualsb = new StringBuilder();

        String pageTextArrays [] = pageTextsb.toString().split(" ");
        String [] actualArray = mBookText.split(" ");

        for (int i = 0; i < 50; i++){
            fiftyWords.append(pageTextArrays[i] + " ");
            actualsb.append(actualArray[i] + " ");
        }


        assertEquals(actualsb.toString(), fiftyWords.toString());
    }

    public void testFirstTwoPagesOfBook()throws Exception {
        mPageOfBooksSenseAndSensiblity = mEPubBookSenseAndSensibility.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        int countPages = 0;
        boolean temp = false;

        for (PageOfBook pageOfBook : mPageOfBooksSenseAndSensiblity){
            if (pageOfBook.getChapterOfBook().equals("CHAPTER 1") || temp){
                temp = true;
                pageTextsb.append(pageOfBook.getPageText());
                countPages++;
                if (countPages == 2){
                    break;
                }
            }
        }

        StringBuilder twohundredWords = new StringBuilder();
        StringBuilder actualsb = new StringBuilder();

        String pageTextArrays [] = pageTextsb.toString().split(" ");
        String [] actualArray = mBookText.split(" ");

        for (int i = 0; i < 199; i++){
            twohundredWords.append(pageTextArrays[i] + " ");
            actualsb.append(actualArray[i] + " ");
        }


        assertEquals(actualsb.toString(), twohundredWords.toString());
    }
}





















