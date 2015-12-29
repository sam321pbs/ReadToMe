package com.example.sammengistu.readtome.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO: Will later be used to get books from the server
public class Library {

    private static Library sLibrary;

    private List<Book> mMyLibrary;
    private List<String> mEPubFileNamesOfBooks;
    private Context mAppContext;

    public Library(Context appContext) {
        mMyLibrary = new ArrayList<>();
        mEPubFileNamesOfBooks = new ArrayList<>();

        mAppContext = appContext;

        mEPubFileNamesOfBooks.add("dave_dawson_with_the_eighth.epub");
        mEPubFileNamesOfBooks.add("geographyofbliss_onechapter.epub");
        mEPubFileNamesOfBooks.add("in_the_wonderful_land_of_hez.epub");
        mEPubFileNamesOfBooks.add("the_planet_mappers.epub");
        mEPubFileNamesOfBooks.add("too_fat_to_fight.epub");
        mEPubFileNamesOfBooks.add("the_story_of_beowulf.epub");
        mEPubFileNamesOfBooks.add("address.epub");
        mEPubFileNamesOfBooks.add("the_snowball_effect.epub");
        mEPubFileNamesOfBooks.add("stone.epub");
        mEPubFileNamesOfBooks.add("famous_givers_and_their_gifts.epub");
        mEPubFileNamesOfBooks.add("the_adventures_of_tom_sawyer.epub");
        mEPubFileNamesOfBooks.add("dracula.epub");

        Book daveDawsonWithEigth = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(0),mAppContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(0),mAppContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(0),mAppContext),
            mEPubFileNamesOfBooks.get(0),
            mAppContext);

        Book geographyOfBliss = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(1),mAppContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(1),mAppContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(1),mAppContext),
            mEPubFileNamesOfBooks.get(1),
            mAppContext);

        Book inTheWonderfulLandOfHez = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(2),mAppContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(2),mAppContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(2),mAppContext),
            mEPubFileNamesOfBooks.get(2),
            mAppContext);

        Book thePlanetMappers = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(3),mAppContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(3),mAppContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(3),mAppContext),
            mEPubFileNamesOfBooks.get(3),
            mAppContext);

        Book tooFatToFight = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(4),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(4),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(4),mAppContext),
            mEPubFileNamesOfBooks.get(4),
            mAppContext);

        Book the_story_of_beowulf = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(5),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(5),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(5),mAppContext),
            mEPubFileNamesOfBooks.get(5),
            mAppContext);

        Book address = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(6),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(6),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(6),mAppContext),
            mEPubFileNamesOfBooks.get(6),
            mAppContext);

        Book theSnowBallEffect = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(7),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(7),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(7),mAppContext),
            mEPubFileNamesOfBooks.get(7),
            mAppContext);
//
        Book stone = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(8),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(8),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(8),mAppContext),
            mEPubFileNamesOfBooks.get(8),
            mAppContext);
//
        Book famous_givers_and_their_gifts = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(9),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(9),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(9),mAppContext),
            mEPubFileNamesOfBooks.get(9),
            mAppContext);

        Book the_adventures_of_tom_sawyer = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(10),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(10),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(10),mAppContext),
            mEPubFileNamesOfBooks.get(10),
            mAppContext);

        Book dracula = new Book(
            GetBookInfo.getBookTitle(mEPubFileNamesOfBooks.get(11),appContext),
            GetBookInfo.getBookAuthor(mEPubFileNamesOfBooks.get(11),appContext),
            GetBookInfo.getBookCover(mEPubFileNamesOfBooks.get(11),mAppContext),
            mEPubFileNamesOfBooks.get(11),
            mAppContext);

        mMyLibrary.add(daveDawsonWithEigth);
        mMyLibrary.add(geographyOfBliss);
        mMyLibrary.add(inTheWonderfulLandOfHez);
        mMyLibrary.add(thePlanetMappers);
        mMyLibrary.add(tooFatToFight);
        mMyLibrary.add(the_story_of_beowulf);
        mMyLibrary.add(address);
        mMyLibrary.add(theSnowBallEffect);
        mMyLibrary.add(stone);
        mMyLibrary.add(famous_givers_and_their_gifts);
        mMyLibrary.add(the_adventures_of_tom_sawyer);
        mMyLibrary.add(dracula);

    }

    public static Library get(Context c) {
        if (sLibrary == null) {
            sLibrary = new Library(c.getApplicationContext());
        }
        return sLibrary;
    }

    public Book getBook(UUID bookId) {
        for (Book book : mMyLibrary) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> getMyLibrary() {
        return mMyLibrary;
    }

}
