package com.example.sammengistu.readtome.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sammengistu.readtome.R;

import java.util.ArrayList;

/**
 * Created by SamMengistu on 9/22/15.
 */
public class ChaptersDialog extends DialogFragment {

    private static final String CHAPTER_PAGE_NUMBER = "Chapter page number";
    public static final String SELECT_CHAPTER = "select chapter";
    public static final String SELECTED_CHAPTER = "selected chapter";
    private static final int CHAPTER_SELECTED = 10;

    private Integer mSelectedChapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<Integer> chapterPageNumbers = getArguments()
                .getIntegerArrayList(CHAPTER_PAGE_NUMBER);


        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.select_chapter_dialog, null);

        final ListView listView = (ListView)v.findViewById(android.R.id.list);

        listView.setAdapter(new ChapterListAdapter(chapterPageNumbers));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                mSelectedChapter = ((ChapterListAdapter) listView.getAdapter()).getItem(position);
                sendResult(CHAPTER_SELECTED);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Ok", null)
                .create();
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_CHAPTER, mSelectedChapter);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public class ChapterListAdapter extends ArrayAdapter<Integer> {

        public ChapterListAdapter(ArrayList<Integer> chapterNumbers) {
            super(getActivity(), 0, chapterNumbers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If a view wasent given to us
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.chapter_view, parent, false);
            }

            Integer a = getItem(position);

            TextView chapterLabelTextView = (TextView)
                    convertView.findViewById(R.id.chapter_number);

            chapterLabelTextView.setText("Chapter " + (position + 1));


            return convertView;
        }
    }

    public static ChaptersDialog newInstance(ArrayList<Integer> chapterPageNumbers){
        Bundle args = new Bundle();
        args.putIntegerArrayList(CHAPTER_PAGE_NUMBER, chapterPageNumbers);

        ChaptersDialog chaptersDialog = new ChaptersDialog();
        chaptersDialog.setArguments(args);

        return chaptersDialog;
    }
}
