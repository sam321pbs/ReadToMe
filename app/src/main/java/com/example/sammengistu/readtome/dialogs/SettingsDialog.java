package com.example.sammengistu.readtome.dialogs;

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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsDialog extends DialogFragment {

    public static final String SETTINGS = "Settings";
    public static final String VOICE_SPEED = "Voice Speed";
    public static final int DEFAULT_NORMAL_SPEED = 20;

    public static final String SENTENCE_BY_SENTENCE_MODE = "Sentence by sentece";
    private static final String MODE = "Mode";
    private static final String VOICE_SETTINGS_SPEED = "Voice speed settings";
    private static final String TAG = "SettingsDialog";
    private boolean mReadSentenceBySentence;
    private int mVoiceSpeed;
    private TextView mVoiceSpeedNumberTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mReadSentenceBySentence = getArguments().getBoolean(MODE);
        mVoiceSpeed = getArguments().getInt(VOICE_SETTINGS_SPEED);

        final View settingsView = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_settings, null);

       TextView mSettingTitle = (TextView) settingsView.findViewById(R.id.settings_title);
        mSettingTitle.setTextColor(Color.BLACK);

        final TextView mQuestionWordByWord = (TextView)settingsView
            .findViewById(R.id.settings_read_one_at_a_time_text_view);
        mQuestionWordByWord.setTextColor(Color.BLACK);

        final TextView mVoiceSpeedTextView = (TextView)settingsView
            .findViewById(R.id.settings_voice_speed_text_view);
        mVoiceSpeedTextView.setTextColor(Color.BLACK);

        mVoiceSpeedNumberTextView = (TextView)settingsView
            .findViewById(R.id.settings_voice_speed_number_text_view);
        String voiceSpeedForView = mVoiceSpeed + "";
        mVoiceSpeedNumberTextView.setText(voiceSpeedForView);
        mVoiceSpeedNumberTextView.setTextColor(Color.BLACK);

        final Switch readSentenceBySentenceSwitch = (Switch)settingsView
            .findViewById(R.id.settings_read_by_sentence_mode);

        readSentenceBySentenceSwitch.setChecked(mReadSentenceBySentence);

        readSentenceBySentenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mReadSentenceBySentence = isChecked;
            }
        });


        final SeekBar voiceSpeed = (SeekBar)settingsView
            .findViewById(R.id.settings_change_voice_speed);
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

        Button mDefaultButton = (Button)settingsView.findViewById(R.id.settings_default_button);
        mDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSentenceBySentenceSwitch.setChecked(true);
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

    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(VOICE_SPEED, mVoiceSpeed);
        intent.putExtra(SENTENCE_BY_SENTENCE_MODE, mReadSentenceBySentence);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static SettingsDialog newInstance(int newVoiceSpeed, boolean mode){
        Bundle args = new Bundle();
        args.putBoolean(MODE, mode);
        args.putInt(VOICE_SETTINGS_SPEED, newVoiceSpeed);

        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setArguments(args);

        return settingsDialog;
    }
}
