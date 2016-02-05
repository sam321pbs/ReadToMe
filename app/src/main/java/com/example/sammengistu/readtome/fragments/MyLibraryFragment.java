package com.example.sammengistu.readtome.fragments;


import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.activities.PagesActivity;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.GetBookInfo;
import com.example.sammengistu.readtome.models.Library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLibraryFragment extends Fragment {

    public static final String BOOK_ID = "Book Id";
    public static final String LIBRARY_PAGE_NUMBER = "Library page number";
    private final int GET_DELETE_OPTION = 2;

    private ImageView mBookOneImage;
    private ImageView mBookTwoImage;
    private ImageView mBookThreeImage;
    private ImageView mBookFourImage;
    private ImageView mBookFiveImage;
    private ImageView mBookSixImage;
    private ImageView mBookSevenImage;
    private ImageView mBookEightImage;
    private ImageView mBookNineImage;

    private TextView mBookCover1Text;
    private TextView mBookCover2Text;
    private TextView mBookCover3Text;
    private TextView mBookCover4Text;
    private TextView mBookCover5Text;
    private TextView mBookCover6Text;
    private TextView mBookCover7Text;
    private TextView mBookCover8Text;
    private TextView mBookCover9Text;

    private List<LibraryPage> mLibraryPages;

    private View mMyLibraryView;

    private List<ImageView> mBookImageViews;
    private List<TextView> mBookCoverTextViews;
    private List<String> mAllBookNames;
    private List<Book> mMyLibraryBooks;

    private int mLibraryPage;

    private boolean mShowErrorToast;

    private ProgressDialog mProgressDialogLoadingBookCovers = null;

    private List<Bitmap> mBitmaps = new ArrayList<>();

    private boolean showErrorDialog;

    private int MAX_NUMBER_OF_BOOKS_PER_PAGE = 9;

    private int bookToDelete = Integer.MAX_VALUE;


    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);

        showErrorDialog = false;

        mShowErrorToast = getActivity().getIntent().getBooleanExtra(PageFragment.ERROR_MESSAGE, false);

        mLibraryPage = getActivity().getIntent().getIntExtra(MyLibraryFragment.LIBRARY_PAGE_NUMBER, 0);

        mMyLibraryBooks = Library.get(getActivity()).getMyLibrary();
        mBookImageViews = new ArrayList<>();
        mBookCoverTextViews = new ArrayList<>();
        mAllBookNames = new ArrayList<>();

        mLibraryPages = new ArrayList<>();

        setUpLibraryPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMyLibraryView = inflater.inflate(R.layout.my_library, container, false);

        ImageView prevButton = (ImageView) mMyLibraryView.findViewById(R.id.my_library_prev_page);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mLibraryPage > -1 && mLibraryPage - 1 != -1) {

                    mLibraryPage--;
                    new LoadBookCoversFromEpubFiles().execute();
                }
            }
        });

        ImageView nextButton = (ImageView) mMyLibraryView.findViewById(R.id.my_library_next_page);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLibraryPage < mLibraryPages.size() && mLibraryPage + 1 != mLibraryPages.size()) {

                    mLibraryPage++;
                    new LoadBookCoversFromEpubFiles().execute();
                }
            }
        });


        initializeImageViews();
        initializeTextViews();
        addImageViewsToList();
        addTextViewsToList();

        new LoadBookCoversFromEpubFiles().execute();

        if (mShowErrorToast) {
            Toast.makeText(getActivity(), R.string.error_loading_book, Toast.LENGTH_SHORT).show();
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

    }

    private void initializeTextViews() {
        mBookCover1Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_1_textview);
        mBookCover2Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_2_textview);
        mBookCover3Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_3_textview);
        mBookCover4Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_4_textview);
        mBookCover5Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_5_textview);
        mBookCover6Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_6_textview);
        mBookCover7Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_7_textview);
        mBookCover8Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_8_textview);
        mBookCover9Text = (TextView) mMyLibraryView.findViewById(R.id.book_cover_page_9_textview);
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

    }

    private void addTextViewsToList() {

        mBookCoverTextViews.add(mBookCover1Text);
        mBookCoverTextViews.add(mBookCover2Text);
        mBookCoverTextViews.add(mBookCover3Text);
        mBookCoverTextViews.add(mBookCover4Text);
        mBookCoverTextViews.add(mBookCover5Text);
        mBookCoverTextViews.add(mBookCover6Text);
        mBookCoverTextViews.add(mBookCover7Text);
        mBookCoverTextViews.add(mBookCover8Text);
        mBookCoverTextViews.add(mBookCover9Text);

    }

    /**
     * Sets the images of each image view in the library with the appropriate book cover
     * along with the proper onClickListener
     *
     * setUpBookImageViews() must be called before to ensure mBitMaps has the correct book covers.
     * If you run out of books it sets the book cover to a transparent
     * image with an empty onClickListener()
     *
     * If a book doesn't have a book cover it will show its title in a text view
     */
    private void setImages() {

        int libraryBookCounter = mLibraryPages.get(mLibraryPage).getStartNumber();
        int bookCoverCounter = 0;

        for (int i = 0; i < mBookImageViews.size(); i++) {
            ImageView currentImageView = mBookImageViews.get(i);
            TextView currentTextView = mBookCoverTextViews.get(i);

            currentTextView.setText("");

            if (libraryBookCounter < mMyLibraryBooks.size()) {
                final Book currentBook = mMyLibraryBooks.get(
                    libraryBookCounter);

                if (mBitmaps.get(bookCoverCounter) == null) {

                    currentTextView.setVisibility(View.VISIBLE);
                    currentImageView.setVisibility(View.INVISIBLE);

                    currentTextView.setTextColor(Color.BLACK);
                    try {
                        currentTextView.setBackgroundColor(Color.parseColor(
                            getActivity().getString(R.string.book_cover_backgroung_color)));
                    } catch (NullPointerException e){

                    }

                    currentTextView.setText(
                        GetBookInfo.getBookTitle(
                            currentBook.getEPubFile())
                    );
                } else {
                    currentTextView.setVisibility(View.INVISIBLE);
                    currentImageView.setVisibility(View.VISIBLE);

                    currentImageView.setImageBitmap(mBitmaps.get(bookCoverCounter));
                }

                View.OnClickListener onClickListenerLoadBook = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PagesActivity.class);
                        intent.putExtra(BOOK_ID, currentBook.getBookId());
                        intent.putExtra(LIBRARY_PAGE_NUMBER, mLibraryPage);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                };

                final int finalLibraryBookCounter = libraryBookCounter;

                View.OnLongClickListener onLongClickListenerDeleteBook = new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String bookTitle = GetBookInfo.getBookTitle(currentBook.getEPubFile());

                        bookToDelete = finalLibraryBookCounter;

                        DeleteBookDialog deleteBookDialog = DeleteBookDialog.newInstance(bookTitle);
                        deleteBookDialog.setTargetFragment(MyLibraryFragment.this,
                            GET_DELETE_OPTION);

                        android.support.v4.app.FragmentManager fm =
                            getActivity().getSupportFragmentManager();

                        deleteBookDialog.show(fm, DeleteBookDialog.DELETE_BOOK);
                        return false;
                    }
                };

                currentImageView.setOnClickListener(onClickListenerLoadBook);
                currentTextView.setOnClickListener(onClickListenerLoadBook);

                currentImageView.setOnLongClickListener(onLongClickListenerDeleteBook);
                currentTextView.setOnLongClickListener(onLongClickListenerDeleteBook);

                libraryBookCounter++;
                bookCoverCounter++;

            } else {
                currentTextView.setVisibility(View.INVISIBLE);
                currentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
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

        if (mMyLibraryBooks.size() != 0) {
            for (int i = 0; i < MAX_NUMBER_OF_BOOKS_PER_PAGE; i++) {

                Book currentBook = mMyLibraryBooks.get(
                    libraryBookCounter);

                Bitmap bitmap = null;

                try {
                    bitmap = GetBookInfo.getBookCover(
                        currentBook.getEPubFile());

                    mBitmaps.add(Bitmap.createScaledBitmap(bitmap, 140, 220, false));

                } catch (NullPointerException e) {
                    mBitmaps.add(bitmap);
                }

                //If you run out of books break out of loop
                if (libraryBookCounter != mMyLibraryBooks.size() - 1) {
                    libraryBookCounter++;
                } else {
                    break;
                }
            }
        } else {
            showErrorDialog = true;
        }
    }

    /**
     * Creates pages for the library
     * As your clicking between the library pages it tells you which book from the library
     * to start on.
     * startBookNumber is the book in the library to start filling the image views with
     */
    private void setUpLibraryPage() {
        int startBookNumber = 0;
        int endBookNumber = 0;

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
                }

                startBookNumber = iHolder;

                if (!makeEndPage) {
                    makeEndPage = true;
                }
            }
        }

        libraryPage = new LibraryPage(startBookNumber);
        mLibraryPages.add(libraryPage);
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
            if (mProgressDialogLoadingBookCovers != null) {
                try {
                    mProgressDialogLoadingBookCovers.dismiss();
                } catch (IllegalArgumentException e) {

                }
            }

            if (showErrorDialog) {

                new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.error_title)
                    .setMessage(R.string.error_connected_to_computer_or_no_books)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

                showErrorDialog = false;
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
            mProgressDialogLoadingBookCovers = ProgressDialog.show(
                getActivity(), getActivity().getString(R.string.loading_title),
                getActivity().getString(R.string.loading_message), true, false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_library, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
            (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
            (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help_library:

                LibraryHelpDialog libraryHelpDialog = new LibraryHelpDialog();
                libraryHelpDialog.show(getActivity().getSupportFragmentManager(),
                    "LibraryDialog");
                return true;
            case R.id.menu_search_book:

                SearchDialog searchDialog = SearchDialog.newInstance(getAllBookFileNames());
                searchDialog.show(getActivity().getSupportFragmentManager(), "SearchDialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_DELETE_OPTION){
            int deleteYes = data.getIntExtra(DeleteBookDialog.DELETE, 0);

            if (deleteYes == DeleteBookDialog.DELETE_IT){
                deleteSelectedBook();
            }
        }
    }

    private void deleteSelectedBook() {
        File fileToDelete = new File(mMyLibraryBooks
            .get(bookToDelete).getEPubFile().getAbsolutePath());

        if (fileToDelete.delete()){

            mMyLibraryBooks.remove(bookToDelete);
            new LoadBookCoversFromEpubFiles().execute();
        }
    }

    private List<String> getAllBookFileNames(){
        List<String> allBookNames = new ArrayList<>();
        for (Book book : mMyLibraryBooks){
            allBookNames.add(book.getEPubFile().getName());
        }

       return allBookNames;
    }
}
