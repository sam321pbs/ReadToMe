package com.example.sammengistu.readtome;

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


public class TestAliceAdventure extends
    ActivityInstrumentationTestCase2<MyLibraryActivity> {

    private Solo mSolo;
    MyLibraryActivity mMyLibraryActivity;
    EPubFileConverterToBook mEPubBookAliceAdventure;

    List<PageOfBook> mPageOfBooksAliceAdventure;

    List<String> mChapterNamesAliceAdventure;

    private Context mContext;

    private String mBookText = "They were indeed a queer-looking party that assembled on the bank—the birds with draggled feathers, the animals with their fur clinging close to them, and all dripping wet," +
        " cross, and uncomfortable. The first question of course was, how to get dry again: they had a consultation about this, and after a few minutes it seemed quite natural to Alice to find herself talking familiarly with them," +
        " as if she had known them all her life. Indeed, she had quite a long argument with the Lory, who at last turned sulky, and would only say, 'I am older than you, and must know better';" +
        " and this Alice would not allow without knowing how old it was, and, as the Lory positively refused to tell its age, there was no more to be said. At last the Mouse, who seemed to be a person of authority among them, called out, 'Sit down, all of you," +
        " and listen to me! I'll soon make you dry enough!' They all sat down at once, in a large ring, with the Mouse in the middle. Alice kept her eyes anxiously fixed on it, for she felt sure she would catch a bad cold if she did not get dry very soon. " +
        "'Ahem!' said the Mouse with an important air, 'are you all ready? This is the driest thing I know. Silence all round, if you please! \"William the Conqueror, whose cause was favoured by the pope, was soon submitted to by the English, who wanted leaders, " +
        "and had been of late much accustomed to usurpation and conquest. Edwin and Morcar, the earls of Mercia and Northumbria—\"''Ugh!' said the Lory, with a shiver. 'I beg your pardon!' said the Mouse, frowning, but very politely: 'Did you speak?' 'Not I!' said the Lory hastily. 'I thought you did,' said the Mouse. '—I " +
        "proceed. \"Edwin and Morcar, the earls of Mercia and Northumbria, declared for him: and even Stigand, the patriotic archbishop of Canterbury, found it advisable—\"' 'Found what?' said the Duck. 'Found it,' the Mouse replied rather crossly: 'of course you know what \"it\" means.' 'I know what \"it\" means well enough, when I find a thing,' " +
        "said the Duck: 'it's generally a frog or a worm. The question is, what did the archbishop find?' The Mouse did not notice this question, but hurriedly went on, '\"—found it advisable to go with Edgar Atheling to meet William and offer him the crown. William's conduct " +
        "at first was moderate. But the insolence of his Normans—\" How are you getting on now, my dear?' it continued, turning to Alice as it spoke.";

    private String mPageBeforeChapter = "come back again, and we won't talk about cats or dogs either, if you don't like them!' When the Mouse heard this, it turned round and swam slowly back to her: its face was quite pale (with passion, Alice thought)," +
        " and it said in a low trembling voice, 'Let us get to the shore, and then I'll tell you my history, and you'll understand why it is I hate cats and dogs.' " +
        "It was high time to go, for the pool was getting quite crowded with the birds and animals that had fallen into it: there were a Duck and a Dodo, a Lory and an Eaglet, and several other curious creatures. Alice led the way, and the whole party swam to the shore.";

    public TestAliceAdventure() {
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
            if (book.getEPubFile().getName().equals("alice's_adventures.epub")){
                mEPubBookAliceAdventure = new EPubFileConverterToBook(mContext,
                    book.getEPubFile());
            }
        }
        super.setUp();
    }


    @Override
    public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    public void testGetChapterNamesForAlice() throws Exception {
        mChapterNamesAliceAdventure = new ArrayList<>();
        mPageOfBooksAliceAdventure = mEPubBookAliceAdventure.getPagesOfTheBook();
        for (PageOfBook pageOfBook : mPageOfBooksAliceAdventure){
            if (!pageOfBook.getChapterOfBook().equals(PageOfBook.PAGE_HAS_NO_CHAPTER)){
                mChapterNamesAliceAdventure.add(pageOfBook.getChapterOfBook());
            }
        }

        assertEquals(mChapterNamesAliceAdventure.get(0), "ALICE'S ADVENTURES IN WONDERLAND");
        assertEquals(mChapterNamesAliceAdventure.get(1), "THE MILLENNIUM FULCRUM EDITION 3.0");
        assertEquals(mChapterNamesAliceAdventure.get(2), "CHAPTER I. Down the Rabbit-Hole");
        assertEquals(mChapterNamesAliceAdventure.get(3), "CHAPTER II. The Pool of Tears");
        assertEquals(mChapterNamesAliceAdventure.get(4), "CHAPTER III. A Caucus-Race and a Long Tale");
        assertEquals(mChapterNamesAliceAdventure.get(5), "CHAPTER IV. The Rabbit Sends in a Little Bill");
        assertEquals(mChapterNamesAliceAdventure.get(6), "CHAPTER V. Advice from a Caterpillar");
    }

    public void testTwoPages() throws Exception {
        mPageOfBooksAliceAdventure = mEPubBookAliceAdventure.getPagesOfTheBook();
        StringBuilder pageTextsb = new StringBuilder();

        int countPages = 0;
        boolean temp = false;

        for (PageOfBook pageOfBook : mPageOfBooksAliceAdventure){
            if (pageOfBook.getChapterOfBook()
                .equals("CHAPTER III. A Caucus-Race and a Long Tale") || temp){
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

    public void testPageBefore() throws Exception {
        mPageOfBooksAliceAdventure = mEPubBookAliceAdventure.getPagesOfTheBook();

        int countPages = 0;
        boolean temp = false;

        for (int i = 0; i < mPageOfBooksAliceAdventure.size(); i ++){

            if (mPageOfBooksAliceAdventure.get(i).getChapterOfBook()
                .equals("CHAPTER III. A Caucus-Race and a Long Tale") || temp){

                countPages = i - 1;
                break;
            }
        }

        StringBuilder twohundredWords = new StringBuilder(
            );
        StringBuilder actualsb = new StringBuilder();

        String pageTextArrays [] = mPageOfBooksAliceAdventure.get(countPages).getPageText().split(" ");
        String [] actualArray = mPageBeforeChapter.split(" ");

        for (int i = pageTextArrays.length; i > 120; i--){
            twohundredWords.append(pageTextArrays[i] + " ");
            actualsb.append(actualArray[i] + " ");
        }

        assertEquals(actualsb.toString(), twohundredWords.toString());
    }
}