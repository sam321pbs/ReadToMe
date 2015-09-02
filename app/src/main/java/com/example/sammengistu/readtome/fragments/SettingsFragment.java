package com.example.sammengistu.readtome.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sammengistu.readtome.R;

/**
 * Created by SamMengistu on 9/2/15.
 */
public class SettingsFragment extends Fragment {

    private static final int NORMAL_SPEED = 20;

    private TextView mSettingTitle;
    private TextView mQuestionWordByWord;
    private TextView mVoiceSpeedTextView;
    private Switch mReadSentenceBySentence;
    private SeekBar mVoiceSpeed;
    private Button mDefaultButton;

    private boolean readSentenceBySentence;
    private int voiceSpeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readSentenceBySentence = false;
        voiceSpeed = NORMAL_SPEED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSettingTitle = (TextView)settingsView.findViewById(R.id.settings_title);
        mSettingTitle.setTextColor(Color.BLACK);

        mQuestionWordByWord = (TextView)settingsView.findViewById(R.id.settings_read_one_at_a_time_text_view);
        mQuestionWordByWord.setTextColor(Color.BLACK);

        mVoiceSpeedTextView = (TextView)settingsView.findViewById(R.id.settings_voice_speed_text_view);
        mVoiceSpeedTextView.setTextColor(Color.BLACK);

        mReadSentenceBySentence = (Switch)settingsView.findViewById(R.id.settings_read_by_sentence_mode);
        mReadSentenceBySentence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                readSentenceBySentence = isChecked;
            }
        });


        mVoiceSpeed = (SeekBar)settingsView.findViewById(R.id.settings_change_voice_speed);
        mVoiceSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                voiceSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDefaultButton = (Button)settingsView.findViewById(R.id.settings_default_button);
        mDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReadSentenceBySentence.setChecked(false);
                mVoiceSpeed.setProgress(NORMAL_SPEED);
            }
        });

        return settingsView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                Intent settingInfo = new Intent();
//                settingInfo.putExtra("Voice speed", voiceSpeed);
//                settingInfo.putExtra("Sentence by Sentence", readSentenceBySentence);
//
//                getActivity().setResult(3, settingInfo);


                NavUtils.navigateUpFromSameTask(getActivity());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
