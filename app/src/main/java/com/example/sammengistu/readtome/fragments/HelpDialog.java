package com.example.sammengistu.readtome.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.example.sammengistu.readtome.R;

/**
 * Created by SamMengistu on 9/15/15.
 */
public class HelpDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_help, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Okay", null)
                .create();
    }
}
