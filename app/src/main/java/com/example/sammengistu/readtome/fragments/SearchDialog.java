package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamMengistu on 2/5/16.
 */
public class SearchDialog  extends DialogFragment {

    private static final String ALL_BOOK_FILE_NAMES = "All Book File Names";
    private List<String> mAllBookFileNames;
    private List<String> mFilteredFileNames;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mFilteredFileNames = new ArrayList<>();
        mAllBookFileNames = getArguments()
            .getStringArrayList(ALL_BOOK_FILE_NAMES);

        View searchView = getActivity().getLayoutInflater()
            .inflate(R.layout.search_dialog, null);

        final ListView listView = (ListView) searchView.findViewById(android.R.id.list);

        EditText typedTitleEditText = (EditText)searchView.findViewById(R.id.dialog_search_edit_text);

        typedTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //TODO: set Up algorithm and update list


                listView.setAdapter(new SearchListAdapter(mAllBookFileNames));
            }
        });

        return new AlertDialog.Builder(getActivity())
            .setView(searchView)
            .setPositiveButton("Open book", null)
            .setNegativeButton("Cancel", null)
            .create();
    }

    public class SearchListAdapter extends ArrayAdapter<String> {

        public SearchListAdapter(List<String> searchLabel) {
            super(getActivity(), 0, searchLabel);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If a view wasn't given to us
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.chapter_view, parent, false);
            }

            TextView chapterLabelTextView = (TextView)
                convertView.findViewById(R.id.chapter_number);

            String labelForChapter = mFilteredFileNames.get(position);

            chapterLabelTextView.setText(labelForChapter);

            return convertView;
        }
    }

    public static SearchDialog newInstance(List<String > allBookFileNames){
        Bundle args = new Bundle();
        args.putStringArrayList(ALL_BOOK_FILE_NAMES, (ArrayList<String>) allBookFileNames);

        SearchDialog searchDialog = new SearchDialog();
        searchDialog.setArguments(args);

        return searchDialog;
    }
}
