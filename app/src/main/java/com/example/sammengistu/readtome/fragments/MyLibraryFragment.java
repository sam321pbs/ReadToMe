package com.example.sammengistu.readtome.fragments;


import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.activities.PagesActivity;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.GetBookInfo;
import com.example.sammengistu.readtome.models.Library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLibraryFragment extends Fragment {

    public static final String BOOK_ID = "Book Id";

    private Button mNextButton;
    private Button mPrevButton;

    private ImageView mBookOneImage;
    private ImageView mBookTwoImage;
    private ImageView mBookThreeImage;
    private ImageView mBookFourImage;
    private ImageView mBookFiveImage;
    private ImageView mBookSixImage;
    private ImageView mBookSevenImage;
    private ImageView mBookEightImage;
    private ImageView mBookNineImage;
    private ImageView mBookTenImage;
    private ImageView mBookElevenImage;
    private ImageView mBookTwelveImage;

    private List<LibraryPage> mLibraryPages;

    private View mMyLibraryView;

    private List<ImageView> mBookImageViews;

    private List<Book> mMyLibraryBooks;

    private int mLibraryPage;

    private boolean mShowErrorToast;

    private ProgressDialog pd = null;

    private List<Bitmap> mBitmaps = new ArrayList<>();

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);

        mShowErrorToast = getActivity().getIntent().getBooleanExtra(PageFragment.ERROR_MESSAGE, false);

        mLibraryPage = 0;

        mMyLibraryBooks = Library.get(getActivity()).getMyLibrary();
        mBookImageViews = new ArrayList<>();

        mLibraryPages = new ArrayList<>();

        setUpLibraryPage();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMyLibraryView = inflater.inflate(R.layout.my_library, container, false);

        mPrevButton = (Button) mMyLibraryView.findViewById(R.id.my_library_prev_page);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mLibraryPage > -1 && mLibraryPage - 1 != -1) {

                    mLibraryPage--;
                    new LoadBookCoversFromEpubFiles().execute();
                }
            }
        });

        mNextButton = (Button) mMyLibraryView.findViewById(R.id.my_library_next_page);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLibraryPage < mLibraryPages.size() && mLibraryPage + 1 != mLibraryPages.size()) {

                    mLibraryPage++;
                    new LoadBookCoversFromEpubFiles().execute();
                }

                Log.i("LibraryPage", "LibraryPage  = " + mLibraryPage);
                Log.i("LibraryPage", "LibraryPage size = " + mLibraryPages.size());
                Log.i("Library", "Library size = " + mMyLibraryBooks.size());
            }
        });


        initializeImageViews();
        addImageViewsToList();
        new LoadBookCoversFromEpubFiles().execute();

        if (mShowErrorToast) {
            Toast.makeText(getActivity(), "Error loading book", Toast.LENGTH_SHORT).show();
        }

        return mMyLibraryView;
    }

    private void initializeImageViews() {
        mBookOneImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_1);
        mBookTwoImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_2);
        mBookThreeImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_3);
        mBookFourImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_4);
        mBookFiveImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_5);
        mBookSixImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_6);
        mBookSevenImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_7);
        mBookEightImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_8);
        mBookNineImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_9);
        mBookTenImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_10);
        mBookElevenImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_11);
        mBookTwelveImage = (ImageView) mMyLibraryView.findViewById(R.id.book_cover_page_12);
    }

    private void addImageViewsToList() {

        mBookImageViews.add(mBookOneImage);
        mBookImageViews.add(mBookTwoImage);
        mBookImageViews.add(mBookThreeImage);
        mBookImageViews.add(mBookFourImage);
        mBookImageViews.add(mBookFiveImage);
        mBookImageViews.add(mBookSixImage);
        mBookImageViews.add(mBookSevenImage);
        mBookImageViews.add(mBookEightImage);
        mBookImageViews.add(mBookNineImage);
        mBookImageViews.add(mBookTenImage);
        mBookImageViews.add(mBookElevenImage);
        mBookImageViews.add(mBookTwelveImage);
    }

    /**
     * Sets the images of each image view in the library view with the appropritate book cover
     * along with the proper onClickListener
     *
     * setUpBookImageViews() must be called before so ensure mBitMaps has the correct book covers a
     * if you run out of books it sets the book cover to a trasparent
     * image with an empty onClickListener()
     */
    private void setImages() {

        int libraryBookCounter = mLibraryPages.get(mLibraryPage).getStartNumber();
        int bookCoverCounter = 0;

        for (ImageView currentImageView : mBookImageViews) {

            Log.i("Counter", "Counter 1 = " + libraryBookCounter);
            Log.i("Counter", "Counter 2 = " + bookCoverCounter);

            if (libraryBookCounter < mMyLibraryBooks.size()) {
                final Book currentBook = mMyLibraryBooks.get(
                    libraryBookCounter);

                currentImageView.setImageBitmap(mBitmaps.get(bookCoverCounter));
                currentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PagesActivity.class);
                        intent.putExtra(BOOK_ID, currentBook.getBookId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                libraryBookCounter++;
                bookCoverCounter++;

            } else {
                currentImageView.setImageResource(android.R.color.transparent);
                currentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    /**
     * Gets the book cover from the the epub file
     */
    private void setUpBookImageViews() {

        mBitmaps.clear();

        int libraryBookCounter = mLibraryPages.get(mLibraryPage).getStartNumber();

        for (int i = 0; i < 12; i++) {

            Log.i("Counter", libraryBookCounter + "");

            Book currentBook = mMyLibraryBooks.get(
                libraryBookCounter);

            mBitmaps.add(Bitmap.createScaledBitmap(GetBookInfo.getBookCover(
                currentBook.getEPubFileName(), getActivity()), 120, 180, false));

            //If you run out of books break out of loop
            if (libraryBookCounter != mMyLibraryBooks.size() - 1) {
                libraryBookCounter++;
            } else {
                break;
            }
        }
    }

    /**
     * Creates pages for the library
     * As your swiping between the library pages it tells you which book from the library
     * to start on.
     * startBookNumber is the book in the library to start filling the image views with
     */
    private void setUpLibraryPage() {
        int startBookNumber = 0;
        int endBookNumber = 0;
        int MAX_NUMBER_OF_BOOKS_PER_PAGE = 12;

        LibraryPage firstPage = new LibraryPage(0);
        mLibraryPages.add(firstPage);

        LibraryPage libraryPage;

        boolean makeEndPage = false;

        int iHolder;

        for (iHolder = 1; iHolder < mMyLibraryBooks.size(); iHolder++) {

            if (iHolder % MAX_NUMBER_OF_BOOKS_PER_PAGE == 0) {

                if (makeEndPage) {
                    endBookNumber = startBookNumber - 1;
                }

                if (endBookNumber != 0) {
                    libraryPage = new LibraryPage(startBookNumber);
                    mLibraryPages.add(libraryPage);

                    Log.i("LibraryPage", "Start page = " + startBookNumber);
                    Log.i("LibraryPage", "End page = " + endBookNumber);
                }

                startBookNumber = iHolder;

                if (!makeEndPage) {
                    makeEndPage = true;
                }
            }
        }

        libraryPage = new LibraryPage(startBookNumber);
        mLibraryPages.add(libraryPage);

        Log.i("LibraryPage", "Start page = " + startBookNumber);
        Log.i("LibraryPage", "End page = " + endBookNumber);
    }

    /**
     * Used to tell you where to start loading books from the library
     */
    private class LibraryPage {
        private int mStartNumber;

        public LibraryPage(int startNumber) {
            mStartNumber = startNumber;
        }

        public int getStartNumber() {
            return mStartNumber;
        }
    }

    /**
     * Loads books that are needed for the page
     * Helps prevent running out of memory by loading the books that are needed to fill the page
     */
    private class LoadBookCoversFromEpubFiles extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            setImages();
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            setUpBookImageViews();
            return "";
        }

        @Override
        protected void onPreExecute() {
            // Show the ProgressDialog on this thread
            pd = ProgressDialog.show(getActivity(), "Loading Books", "Loading...", true, false);
        }
    }
}
