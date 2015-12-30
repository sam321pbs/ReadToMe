package com.example.sammengistu.readtome.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class Library {

    private static Library sLibrary;

    private List<Book> mMyLibrary;
    private Context mAppContext;

    public Library(Context appContext) {
        mMyLibrary = new ArrayList<>();

        mAppContext = appContext;

        try {
            for (String name: getEpubBooksFromFolder()){
                String cleanedName = name.replaceAll(" ", "");

                if (cleanedName.contains(".epub")) {
                    Log.i("Test", name);
                    Book book = new Book(cleanedName, appContext);

                    mMyLibrary.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getEpubBooksFromFolder() throws IOException {
        AssetManager assetManager = mAppContext.getAssets();
        String[] files = assetManager.list("");
        return Arrays.asList(files);
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
