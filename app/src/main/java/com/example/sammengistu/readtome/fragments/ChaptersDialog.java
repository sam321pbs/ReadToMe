package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

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

import java.util.ArrayList;
import java.util.List;

public class ChaptersDialog extends DialogFragment {

    public static final String SELECT_CHAPTER = "select chapter";
    public static final String SELECTED_CHAPTER = "selected chapter";

    private static final String CHAPTER_PAGE_NUMBER = "Chapter page number";
    private static final int CHAPTER_SELECTED = 10;
    private Integer mSelectedChapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<Integer> chapterPageNumbers = getArguments()
            .getIntegerArrayList(CHAPTER_PAGE_NUMBER);

        View v = getActivity().getLayoutInflater()
            .inflate(R.layout.select_chapter_dialog, null);

        final ListView listView = (ListView) v.findViewById(android.R.id.list);

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

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_CHAPTER, mSelectedChapter);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public class ChapterListAdapter extends ArrayAdapter<Integer> {

        public ChapterListAdapter(List<Integer> chapterNumbers) {
            super(getActivity(), 0, chapterNumbers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If a view wasent given to us
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.chapter_view, parent, false);
            }

            TextView chapterLabelTextView = (TextView)
                convertView.findViewById(R.id.chapter_number);

            String labelForChapter = "Chapter " + (position + 1);

            chapterLabelTextView.setText(labelForChapter);


            return convertView;
        }
    }

    public static ChaptersDialog newInstance(List<Integer> chapterPageNumbers) {
        Bundle args = new Bundle();
        args.putIntegerArrayList(CHAPTER_PAGE_NUMBER, (ArrayList<Integer>) chapterPageNumbers);

        ChaptersDialog chaptersDialog = new ChaptersDialog();
        chaptersDialog.setArguments(args);

        return chaptersDialog;
    }
}
