package com.example.sammengistu.readtome.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sammengistu.readtome.Book;
import com.example.sammengistu.readtome.Library;
import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.activities.PagesActivity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyLibraryFragment extends Fragment {

    public static final String BOOK_ID = "Book Id";
    private ImageView mBookOneImage;
    private Book curiousGeorge;


    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);

        ArrayList<Book> myLibrary = Library.get(getActivity()).getMyLibrary();
        curiousGeorge =  myLibrary.get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_library, container, false);

        mBookOneImage = (ImageView) v.findViewById(R.id.curious_george_book_cover);
        mBookOneImage.setImageResource(curiousGeorge.getBookCover());

        mBookOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, curiousGeorge.getBookId());
                startActivity(intent);
            }
        });


        return v;
    }


}
