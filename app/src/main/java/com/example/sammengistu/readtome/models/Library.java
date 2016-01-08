package com.example.sammengistu.readtome.models;

import com.example.sammengistu.readtome.R;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Library {

    private static Library sLibrary;

    private List<Book> mMyLibrary;
    private Context mAppContext;

    /**
     * Gets all the epub files from the assests folder and saves it as a book
     *
     * @param appContext - used to get access to the files
     */
    public Library(Context appContext) {
        mMyLibrary = new ArrayList<>();

        mAppContext = appContext;
        try {
            for (File epubFile : getEpubBooksFromFolder()) {
                String cleanedName = epubFile.getName();

                if (cleanedName.contains(mAppContext.getString(R.string.epub_name))) {
                    Book book = new Book(epubFile, appContext);
                    mMyLibrary.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Goes into the proper directory folder and grabs all file names
     *
     * @return - list of all the file names
     * @throws IOException - error loading the file names
     */
    private List<File> getEpubBooksFromFolder() throws IOException {

        List<File> epubBookFiles = new ArrayList<>();

        File epubDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
            mAppContext.getString(R.string.directory_name));

        if (!epubDirectory.exists()) {
            epubDirectory.mkdir();
        } else {

            File[] files = epubDirectory.listFiles();

            for (File file : files) {
                if (!file.getName().substring(0, 1).equals(".")) {
                    epubBookFiles.add(file);
                }
            }
        }
        return epubBookFiles;
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
