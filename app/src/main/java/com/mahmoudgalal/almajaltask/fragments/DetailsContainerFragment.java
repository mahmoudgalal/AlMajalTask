/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudgalal.almajaltask.R;
import com.mahmoudgalal.almajaltask.adapters.NewsPagerAdapter;
import com.mahmoudgalal.almajaltask.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsContainerFragment extends Fragment {

    private ViewPager viewPager;
    private NewsPagerAdapter adapter ;
    private List<NewsDetailsFragment> allChilds ;

    public static final String REQUESTED_ITEM_KEY = "DetailsContainerFragment_REQUESTED_ITEM_KEY";
    public static final String ALL_ITEMS_KEY = "DetailsContainerFragment_ALL_ITEMS_KEY";
    public static final String ITEM_PAGE_KEY = "DetailsContainerFragment_ITEM_PAGE_KEY";

    private int currentFetchedPage = -1 ;
    int selectedItemIndex = -1;
    private List<NewsItem> allItems ;

    public DetailsContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        allChilds = new ArrayList<>();
        viewPager = root.findViewById(R.id.pager);
        adapter =  new NewsPagerAdapter(getChildFragmentManager(),allChilds);

        viewPager.setAdapter(adapter);
        OverScrollDecoratorHelper.setUpOverScroll(viewPager);

        Bundle args = getArguments();
        allItems = args.getParcelableArrayList(ALL_ITEMS_KEY);
        selectedItemIndex  = args.getInt(REQUESTED_ITEM_KEY);
        currentFetchedPage = args.getInt(ITEM_PAGE_KEY);

        init();;
    }

    private void init(){
        allChilds.clear();
        if(allItems !=null && !allItems.isEmpty()) {
            for (NewsItem newsItem : allItems) {
                NewsDetailsFragment fragment = new NewsDetailsFragment();
                Bundle args = new Bundle();
                args.putParcelable(NewsDetailsFragment.ITEM_KEY, newsItem);
                fragment.setArguments(args);
                allChilds.add(fragment);
            }
            adapter.notifyDataSetChanged();
            viewPager.setCurrentItem(selectedItemIndex);
        }
    }
}
