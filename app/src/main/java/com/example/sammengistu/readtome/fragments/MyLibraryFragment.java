package com.example.sammengistu.readtome.fragments;


import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.activities.PagesActivity;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyLibraryFragment extends Fragment {

    public static final String BOOK_ID = "Book Id";
    private Book mThingsFallApart;
    private Book mThePlanetMappers;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);

        List<Book> myLibrary = Library.get(getActivity()).getMyLibrary();
        mThingsFallApart = myLibrary.get(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_library, container, false);

        ImageView bookOneImage = (ImageView) v.findViewById(R.id.things_fall_apart_book_cover);
        bookOneImage.setImageResource(mThingsFallApart.getBookCover());

        bookOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mThingsFallApart.getBookId());
                startActivity(intent);
            }
        });

        return v;
    }


}
