/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mahmoudgalal.almajaltask.BuildConfig;
import com.mahmoudgalal.almajaltask.R;
import com.mahmoudgalal.almajaltask.Utils;
import com.mahmoudgalal.almajaltask.adapters.AllNewsAdapter;
import com.mahmoudgalal.almajaltask.model.NewsItem;
import com.mahmoudgalal.almajaltask.model.TokenResponse;
import com.mahmoudgalal.almajaltask.viewmodels.NewsViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllNewsFragment extends Fragment implements AllNewsAdapter.OnItemClickedListener {

    private RecyclerView recyclerView;
    private NewsViewModel viewModel;
    private AllNewsAdapter adapter ;
    private ProgressBar progressBar;
    private LinearLayoutManager llm ;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        recyclerView = root.findViewById(R.id.news_recycler_view);
        progressBar = root.findViewById(R.id.progressBar);
        llm = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration =  new DividerItemDecoration(getContext(),
                llm.getOrientation());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(llm);
       // recyclerView.addItemDecoration(dividerItemDecoration);

        init(adapter == null);
    }

    private void init(boolean forceReload){

        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.items.observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable List<NewsItem> newsItems) {
                adapter.setItems(newsItems);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),String.format("Page %d loaded ",
                        viewModel.getCurrentPage()),
                        Toast.LENGTH_LONG ).show();
            }
        });
        viewModel.errorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(getContext(),"Error:"+s,Toast.LENGTH_LONG).show();
            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                isLoading = loading;
                progressBar.setVisibility(loading?View.VISIBLE:View.GONE);

            }
        });
        viewModel.tokenRes.observe(this, new Observer<TokenResponse>() {
            @Override
            public void onChanged(@Nullable TokenResponse tokenResponse) {
                Toast.makeText(getContext(),"New Token saved..!",Toast.LENGTH_LONG).show();
                Utils.saveLatestToken(getContext().getApplicationContext(),tokenResponse.token,
                        new Date().getTime(),tokenResponse.ttl);
            }
        });


        adapter = new AllNewsAdapter(viewModel.items.getValue());
        adapter.setOnItemClickedListener(this);
        recyclerView.setAdapter(adapter);
        initRecyclerPaging();
        if(forceReload) {
            String latestToken = Utils.getSavedToken(getContext().getApplicationContext());
            if(BuildConfig.DEBUG)
                Toast.makeText(getContext(),
                        "Latest Token = "+latestToken,Toast.LENGTH_LONG).show();
            if(!Utils.isInternetConnectionExist(getContext())){
                Toast.makeText(getContext(),"No internet connection,try later .. ",
                        Toast.LENGTH_LONG).show();
                return;
            }
            viewModel.getNextNewsPage(latestToken);
        }
    }

    void initRecyclerPaging(){
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = llm.getChildCount();
                int totalItemCount = llm.getItemCount();
                int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();

                if (!isLoading)
                {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                            firstVisibleItemPosition >= 0)
                    {
                        if(!Utils.isInternetConnectionExist(getContext())){
                            Toast.makeText(getContext(),"No internet connection,try later",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        viewModel.getNextNewsPage(Utils.getSavedToken(getContext()));

                    }
                }
            }
        });
    }

    @Override
    public void onItemClicked(View view,View mainImg, NewsItem item,int position) {

        DetailsContainerFragment fragment =  new DetailsContainerFragment();
        Bundle args = new Bundle();
        args.putInt(DetailsContainerFragment.REQUESTED_ITEM_KEY,position);
        args.putParcelableArrayList(DetailsContainerFragment.ALL_ITEMS_KEY,
                new ArrayList<>(adapter.getItems()));
        args.putInt(DetailsContainerFragment.ITEM_PAGE_KEY,viewModel.getCurrentPage());
        fragment.setArguments(args);
       // createTransition(this,fragment);
        getFragmentManager().beginTransaction().replace(R.id.main_container,
                fragment,DetailsContainerFragment.class.getSimpleName()).
                addToBackStack(null)
                //.addSharedElement(mainImg,"main_img_transition")
                .commit();
    }

    private void createTransition(Fragment a,Fragment b){
        // Inflate transitions to apply
        Transition changeTransform = TransitionInflater.from(getContext()).
                inflateTransition(R.transition.change_image_transform);
        Transition explodeTransform = TransitionInflater.from(getContext()).
                inflateTransition(android.R.transition.explode);

        // Setup exit transition on first fragment
        a.setSharedElementReturnTransition(changeTransform);
        //a.setExitTransition(explodeTransform);

        // Setup enter transition on second fragment
        b.setSharedElementEnterTransition(changeTransform);
        b.setEnterTransition(explodeTransform);
    }

}
