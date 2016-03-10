package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by SamMengistu on 3/10/16.
 */
public class TypeAndReadHelpDialog  extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater()
            .inflate(R.layout.type_and_read_help_dialog, null);
        return new AlertDialog.Builder(getActivity())
            .setView(v)
            .setPositiveButton("Okay", null)
            .create();
    }
}
