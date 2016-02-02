package com.example.sammengistu.readtome.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DeleteBookDialog extends DialogFragment {

    public static final String BOOK_TITLE = "book title";
    public static final String DELETE_BOOK = "Delete book";
    public static final int DELETE_IT = 22111;
    public static final String DELETE = "delete";
    public static final int DELETE_OPTION_YES = 12345678;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String bookTitle = getArguments().getString(BOOK_TITLE);

        return new AlertDialog.Builder(getActivity())
            .setTitle("Are you sure you want to delete this book?")
            .setMessage("* " + bookTitle + "\n \n Warning: This action is irreversible")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendResult(DELETE_OPTION_YES);
                }
            })
            .setNegativeButton("No", null)
            .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(DELETE, DELETE_IT);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static DeleteBookDialog newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(BOOK_TITLE, title);

        DeleteBookDialog deleteBookDialog = new DeleteBookDialog();
        deleteBookDialog.setArguments(args);

        return deleteBookDialog;
    }
}
