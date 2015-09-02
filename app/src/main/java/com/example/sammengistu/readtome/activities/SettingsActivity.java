package com.example.sammengistu.readtome.activities;

import android.support.v4.app.Fragment;

import com.example.sammengistu.readtome.SingleFragmentActivity;
import com.example.sammengistu.readtome.fragments.SettingsFragment;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new SettingsFragment();
    }
}
