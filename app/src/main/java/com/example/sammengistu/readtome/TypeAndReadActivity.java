package com.example.sammengistu.readtome;

import com.example.sammengistu.readtome.models.SingleFragmentActivity;

import android.support.v4.app.Fragment;

public class TypeAndReadActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new TypeAndReadFragment();
    }
}
