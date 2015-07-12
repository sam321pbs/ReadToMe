package com.example.sammengistu.readtome.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sammengistu.readtome.Book;
import com.example.sammengistu.readtome.Library;
import com.example.sammengistu.readtome.PageOfBook;
import com.example.sammengistu.readtome.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    private static final String TAG = "PageFragment";
    private ArrayList<PageOfBook> mPagesOfBook;
    private ImageView mTurnPage;
    private ImageView mGoBackPage;
    private ImageView mPagePicture;
    private String[] mPageWordBank;
    int pageNumber;
    private ArrayList<TableLayout> mTableLayouts;

    @Override
    public void onCreate(Bundle savedInstnaceState) {
        super.onCreate(savedInstnaceState);

        mTableLayouts = new ArrayList<TableLayout>();

        ArrayList<Book> library = Library.get(getActivity()).getMyLibrary();

        mPagesOfBook = library.get(0).getPageOfBook();

        pageNumber = 0;

        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View blankPage = inflater.inflate(R.layout.pages_fragment, container, false);

        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout1));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout2));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout3));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout4));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout5));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout6));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout7));
        mTableLayouts.add((TableLayout) blankPage.findViewById(R.id.fragment_page_tableLayout8));

        mPagePicture = (ImageView) blankPage.findViewById(R.id.page_picture);
        mPagePicture.setImageResource(mPagesOfBook.get(pageNumber).getPagePicture());

        mTurnPage = (ImageView) blankPage.findViewById(R.id.turn_page);
        mTurnPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Turn Page", Toast.LENGTH_SHORT).show();
                pageNumber++;
                setUpPageText();
                mPagePicture.setImageResource(mPagesOfBook.get(pageNumber).getPagePicture());
            }
        });
        mGoBackPage = (ImageView) blankPage.findViewById(R.id.go_back);
        mGoBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Go Back Page", Toast.LENGTH_SHORT).show();
                pageNumber--;
                setUpPageText();
                mPagePicture.setImageResource(mPagesOfBook.get(pageNumber).getPagePicture());
            }
        });

        setUpPageText();
        return blankPage;
    }


    private void setUpPageText() {
        mPageWordBank = mPagesOfBook.get(pageNumber).getPageText().split("\\s+");

        cleanUpPageText();

        int placeHolder = 0;

        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                if (mPageWordBank.length != placeHolder) {
                    TextView word = (TextView) row.getChildAt(j);
                    word.setText(mPageWordBank[placeHolder]);
                    word.setOnClickListener(onClick());
                    placeHolder++;

                } else {
                    break;
                }
            }
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                textView.setBackgroundColor(Color.YELLOW);
                Log.i(TAG, textView.getText() + "");
            }
        };
    }

    private void cleanUpPageText() {
        for (TableLayout tableLayout : mTableLayouts) {
            TableRow row = (TableRow) tableLayout.getChildAt(0);
            for (int j = 0; j < row.getChildCount(); j++) {
                    TextView word = (TextView) row.getChildAt(j);
                    word.setText("");
                    word.setBackgroundColor(Color.WHITE);
                }
            }
    }
}
