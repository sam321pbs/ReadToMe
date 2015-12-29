package com.example.sammengistu.readtome.fragments;


import com.example.sammengistu.readtome.R;
import com.example.sammengistu.readtome.activities.PagesActivity;
import com.example.sammengistu.readtome.models.Book;
import com.example.sammengistu.readtome.models.Library;

import android.content.Intent;
import android.graphics.Bitmap;
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
    private Book mDaveDawsonWithEigth;
    private Book mGeographyOfBliss;
    private Book mInTheWonderfulLandOfHez;
    private Book mThePlanetMappers;
    private Book mTooFatToFight;
    private Book the_story_of_beowulf;
    private Book mAddress;
    private Book mTheSnowBallEffect;
    private Book mWunderwelten;
    private Book famous_givers_and_their_gifts;
    private Book mThe_adventures_of_tom_sawyer;
    private Book mDracula;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);

        List<Book> myLibrary = Library.get(getActivity()).getMyLibrary();
        mDaveDawsonWithEigth = myLibrary.get(0);
        mGeographyOfBliss = myLibrary.get(1);
        mInTheWonderfulLandOfHez = myLibrary.get(2);
        mThePlanetMappers = myLibrary.get(3);
        mTooFatToFight = myLibrary.get(4);
        the_story_of_beowulf = myLibrary.get(5);
        mAddress = myLibrary.get(6);
        mTheSnowBallEffect = myLibrary.get(7);
        mWunderwelten = myLibrary.get(8);
        famous_givers_and_their_gifts = myLibrary.get(9);
        mThe_adventures_of_tom_sawyer = myLibrary.get(10);
        mDracula = myLibrary.get(11);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_library, container, false);

        ImageView bookOneImage = (ImageView) v.findViewById(R.id.book_cover_page_1);
        bookOneImage.setImageBitmap(
            Bitmap.createScaledBitmap(mDaveDawsonWithEigth.getBookCover(), 120, 180, false));

        bookOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mDaveDawsonWithEigth.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookTwoImage = (ImageView) v.findViewById(R.id.book_cover_page_2);
        bookTwoImage.setImageBitmap(
            Bitmap.createScaledBitmap(mGeographyOfBliss.getBookCover(), 120, 180, false)
        );

        bookTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mGeographyOfBliss.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookThreeImage = (ImageView) v.findViewById(R.id.book_cover_page_3);
        bookThreeImage.setImageBitmap(
            Bitmap.createScaledBitmap(mInTheWonderfulLandOfHez.getBookCover(),120,180,false)
        );

        bookThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mInTheWonderfulLandOfHez.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookFourImage = (ImageView) v.findViewById(R.id.book_cover_page_4);
        bookFourImage.setImageBitmap(
            Bitmap.createScaledBitmap(mThePlanetMappers.getBookCover(), 120, 180, false)
        );

        bookFourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mThePlanetMappers.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookFiveImage = (ImageView) v.findViewById(R.id.book_cover_page_5);
        bookFiveImage.setImageBitmap(
            Bitmap.createScaledBitmap(mTooFatToFight.getBookCover(), 120, 180, false)
        );

        bookFiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mTooFatToFight.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookSixImage = (ImageView) v.findViewById(R.id.book_cover_page_6);
        bookSixImage.setImageBitmap(
            Bitmap.createScaledBitmap(the_story_of_beowulf.getBookCover(), 120, 180, false)
        );

        bookSixImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, the_story_of_beowulf.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookSevenImage = (ImageView) v.findViewById(R.id.book_cover_page_7);
        bookSevenImage.setImageBitmap(
            Bitmap.createScaledBitmap(mAddress.getBookCover(), 120, 180, false)
        );

        bookSevenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mAddress.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookEightImage = (ImageView) v.findViewById(R.id.book_cover_page_8);
        bookEightImage.setImageBitmap(
            Bitmap.createScaledBitmap(mTheSnowBallEffect.getBookCover(), 120, 180, false)
        );

        bookEightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mTheSnowBallEffect.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookNineImage = (ImageView) v.findViewById(R.id.book_cover_page_9);
        bookNineImage.setImageBitmap(
            Bitmap.createScaledBitmap(mWunderwelten.getBookCover(), 120, 180, false)
        );

        bookNineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mWunderwelten.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookTenImage = (ImageView) v.findViewById(R.id.book_cover_page_10);
        bookTenImage.setImageBitmap(
            Bitmap.createScaledBitmap(famous_givers_and_their_gifts.getBookCover(), 120, 180, false)
        );

        bookTenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, famous_givers_and_their_gifts.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookElevenImage = (ImageView) v.findViewById(R.id.book_cover_page_11);
        bookElevenImage.setImageBitmap(
            Bitmap.createScaledBitmap(mThe_adventures_of_tom_sawyer.getBookCover(), 120, 180, false)
        );

        bookElevenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mThe_adventures_of_tom_sawyer.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageView bookTwelveImage = (ImageView) v.findViewById(R.id.book_cover_page_12);
        bookTwelveImage.setImageBitmap(
            Bitmap.createScaledBitmap(mDracula.getBookCover(), 120, 180, false)
        );

        bookTwelveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PagesActivity.class);
                intent.putExtra(BOOK_ID, mDracula.getBookId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



        return v;
    }
}
