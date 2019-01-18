/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mahmoudgalal.almajaltask.fragments.NewsDetailsFragment;

import java.util.List;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    List<NewsDetailsFragment> items ;
    public NewsPagerAdapter(FragmentManager fm,List<NewsDetailsFragment> items) {
        super(fm);
        this.items =items ;
    }

    @Override
    public Fragment getItem(int position) {
        return  items.get(position);
    }

    @Override
    public int getCount() {
        return items != null?items.size():0;
    }
}
