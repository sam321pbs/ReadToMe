package com.example.sammengistu.readtome.fragments;

import com.example.sammengistu.readtome.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by SamMengistu on 3/9/16.
 */
public class SettingsDialogTypeAndRead extends DialogFragment {

    public static final String VOICE_SPEED = "Voice Speed";
    public static final int DEFAULT_NORMAL_SPEED = 20;

    private static final String VOICE_SETTINGS_SPEED = "Voice speed settings";
    private static final String TAG = "SettingsDialog";

    private int mVoiceSpeed;
    private TextView mVoiceSpeedNumberTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mVoiceSpeed = getArguments().getInt(VOICE_SETTINGS_SPEED);

        final View settingsView = getActivity().getLayoutInflater()
            .inflate(R.layout.type_and_read_setting_dialog, null);

        TextView mSettingTitle = (TextView) settingsView.findViewById(R.id.settings_title_dialog);
        mSettingTitle.setTextColor(Color.BLACK);

        final TextView mVoiceSpeedTextView = (TextView) settingsView
            .findViewById(R.id.settings_voice_speed_text_view_dialog);
        mVoiceSpeedTextView.setTextColor(Color.BLACK);

        mVoiceSpeedNumberTextView = (TextView) settingsView
            .findViewById(R.id.settings_voice_speed_number_text_view_dialog);
        String voiceSpeedForView = mVoiceSpeed + "";
        mVoiceSpeedNumberTextView.setText(voiceSpeedForView);
        mVoiceSpeedNumberTextView.setTextColor(Color.BLACK);

        final SeekBar voiceSpeed = (SeekBar) settingsView
            .findViewById(R.id.settings_change_voice_speed_dialog);
        voiceSpeed.setProgress(mVoiceSpeed);
        voiceSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVoiceSpeed = progress;
                String progressForView = progress + "";
                mVoiceSpeedNumberTextView.setText(progressForView);
                mVoiceSpeedNumberTextView.setTextColor(Color.BLACK);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button mDefaultButton = (Button) settingsView.findViewById(R.id.settings_default_button_dialog);
        mDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceSpeed.setProgress(DEFAULT_NORMAL_SPEED);
            }
        });


        return new AlertDialog.Builder(getActivity())
            .setView(settingsView)
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendResult(Activity.RESULT_OK);
                }
            })
            .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(VOICE_SPEED, mVoiceSpeed);

        getTargetFragment()
            .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static SettingsDialogTypeAndRead newInstance(int newVoiceSpeed) {
        Bundle args = new Bundle();
        args.putInt(VOICE_SETTINGS_SPEED, newVoiceSpeed);

        SettingsDialogTypeAndRead settingsDialog = new SettingsDialogTypeAndRead();
        settingsDialog.setArguments(args);

        return settingsDialog;
    }
}

