package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchDialog  extends DialogFragment {

    private static final String ALL_BOOK_FILE_NAMES = "All Book File Names";
    public static final String SELECTED_BOOK = "selected book";
    private static final int BOOK_SELECTED = 1010;
    private List<String> mAllBookFileNames;
    private List<String> mFilteredFileNames;
    private int mBookPosition;

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

                mFilteredFileNames.clear();

                for (String title : mAllBookFileNames){
                    if (doesItContainWord(title, s.toString())){
                        mFilteredFileNames.add(title);
                    }
                }

                listView.setAdapter(new SearchListAdapter(mFilteredFileNames));
            }
        });

        final TextView selectedTitle = (TextView)searchView
            .findViewById(R.id.dialog_selected_book_box);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTitle.setText(mFilteredFileNames
                    .get(position).replaceAll(".epub", ""));
                mBookPosition = position;
            }
        });
        return new AlertDialog.Builder(getActivity())
            .setView(searchView)
            .setPositiveButton("Open book", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendResult(BOOK_SELECTED, mFilteredFileNames.get(mBookPosition));
                }
            })
            .setNegativeButton("Cancel", null)
            .create();
    }

    private boolean doesItContainWord(String title , String s){

        return title.toLowerCase().contains(s.toLowerCase());
    }

    private void sendResult(int resultCode, String title) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_BOOK, title);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, intent);
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

            String removeEPub = getActivity().getString(R.string.epub_name);

            String labelForChapter = mFilteredFileNames.get(position);

            chapterLabelTextView.setText(labelForChapter
                .replaceAll(removeEPub, ""));

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
