package com.example.sammengistu.readtome.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by SamMengistu on 12/23/15.
 */
public class GetBookInfo {

    private static nl.siegmann.epublib.domain.Book mCurrentBook;


    private static void setUpFile(File epubFile) {

        // find InputStream for book

        FileInputStream epubInputStream;

        try {
            epubInputStream = new FileInputStream(epubFile);


            // Load Book from inputStream

            mCurrentBook = (new EpubReader()).readEpub(epubInputStream);
        } catch (IOException e) {

        }
    }

    public static Bitmap getBookCover(File epubFile) {

        setUpFile(epubFile);

        Bitmap coverImage = null;

        try {
            coverImage = BitmapFactory.decodeStream(mCurrentBook.getCoverImage()

                .getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coverImage;
    }

    public static String getBookAuthor(File epubFile) {
        setUpFile(epubFile);

        String author = "";

        author = mCurrentBook.getMetadata().getAuthors() + " ";

        return author;
    }

    public static String getBookTitle(File epubFile) {
        setUpFile(epubFile);

        String title = "";

        title = mCurrentBook.getTitle() + " ";

        return title;
    }
}
