/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mahmoudgalal.almajaltask.fragments.AllNewsFragment;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if(bar != null){

            bar.setDisplayHomeAsUpEnabled(false);
            bar.setDisplayShowHomeEnabled(false);
            bar.setHomeButtonEnabled(true);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        AllNewsFragment fragment = new AllNewsFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_container,fragment,
                        AllNewsFragment.class.getSimpleName()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackStackChanged() {
       int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
       boolean canGoBack = stackHeight > 0;
       getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
       getSupportActionBar().setDisplayShowHomeEnabled(canGoBack);
    }
}
