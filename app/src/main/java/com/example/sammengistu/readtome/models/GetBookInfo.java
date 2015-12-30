package com.example.sammengistu.readtome.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by SamMengistu on 12/23/15.
 */
public class GetBookInfo {

    private static nl.siegmann.epublib.domain.Book mCurrentBook;

    private static void setUpFile(String epubFileName, Context appContext) {
        AssetManager assetManager = appContext.getAssets();

        // find InputStream for book

        InputStream epubInputStream;

        try {
            epubInputStream = assetManager

                .open(epubFileName);


            // Load Book from inputStream

            mCurrentBook = (new EpubReader()).readEpub(epubInputStream);
        } catch (IOException e) {

        }
    }

    public static Bitmap getBookCover(String epubFileName, Context appContext) {

        setUpFile(epubFileName, appContext);

        //TODO: make standard image
        Bitmap coverImage = null;
        try {

            coverImage = BitmapFactory.decodeStream(mCurrentBook.getCoverImage()

                .getInputStream());

        } catch (IOException | NullPointerException e) {
            Log.i("BookCover", "BookCover was null");

        }
        return coverImage;
    }

    public static String getBookAuthor(String epubFileName, Context appContext) {
        setUpFile(epubFileName, appContext);

        String author = "";

        author = mCurrentBook.getMetadata().getAuthors() + " ";

        return author;
    }

    public static String getBookTitle(String epubFileName, Context appContext) {
        setUpFile(epubFileName, appContext);

        String title = "";

        title = mCurrentBook.getTitle() + " ";

        return title;
    }
}
