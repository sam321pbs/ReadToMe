package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SelectPageDialog extends DialogFragment {

    public static final String SELECT_PAGE = "select Page";
    public static final String SELECTED_PAGE = "Selected Page";
    public static final int PAGE_SELECTED = 6006;

    private static final String CURRENT_PAGE_NUMBER = "Current Page";
    private static final String MAX_NUMBER_OF_PAGES = "max pages";

    final String[] selectedPageNumber = {""};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View selectPageDialog = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_select_page, null);

        int currentPageNumber = getArguments().getInt(CURRENT_PAGE_NUMBER, 0);
        final int max = getArguments().getInt(MAX_NUMBER_OF_PAGES, 0);

        EditText selectedPage = (EditText) selectPageDialog
            .findViewById(R.id.select_page_dialog_page_number_selected_by_user);
        String currentPageNumberForView = currentPageNumber + "";
        selectedPage.setText(currentPageNumberForView);
        selectedPage.setTextColor(Color.BLACK);

        selectedPage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedPageNumber[0] = s.toString();
            }
        });

        return new AlertDialog.Builder(
                getActivity())
                .setView(selectPageDialog)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (isNumeric(selectedPageNumber[0])) {
                            if (Integer.parseInt(selectedPageNumber[0]) > max - 1 ||
                                Integer.parseInt(selectedPageNumber[0]) < 0) {
                                Toast.makeText(getActivity(), "Invalid Page Number",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                sendResult(PAGE_SELECTED);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Invalid Page Number",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SELECTED_PAGE, Integer.parseInt(selectedPageNumber[0]));

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static SelectPageDialog newInstance(int pageNumber, int maxPages){
        Bundle args = new Bundle();
        args.putInt(CURRENT_PAGE_NUMBER, pageNumber);
        args.putInt(MAX_NUMBER_OF_PAGES, maxPages);

        SelectPageDialog dialog = new SelectPageDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public static boolean isNumeric(String str)
    {
        try {
            int number = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
