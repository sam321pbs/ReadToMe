package com.example.sammengistu.readtome.activities;

import com.example.sammengistu.readtome.fragments.TypeAndReadFragment;
import com.example.sammengistu.readtome.models.SingleFragmentActivity;

import android.support.v4.app.Fragment;

public class TypeAndReadActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new TypeAndReadFragment();
    }
}
