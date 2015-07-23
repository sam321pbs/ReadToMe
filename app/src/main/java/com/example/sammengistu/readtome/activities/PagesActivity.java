package com.example.sammengistu.readtome.activities;

import android.support.v4.app.Fragment;

import com.example.sammengistu.readtome.fragments.PageFragment;
import com.example.sammengistu.readtome.SingleFragmentActivity;


public class PagesActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new PageFragment();
    }
}
