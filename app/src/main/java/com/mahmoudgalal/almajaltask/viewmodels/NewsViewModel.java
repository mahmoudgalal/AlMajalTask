/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import com.mahmoudgalal.almajaltask.NetworkingManager;

import com.mahmoudgalal.almajaltask.model.NewsItem;
import com.mahmoudgalal.almajaltask.model.ServerResponse;
import com.mahmoudgalal.almajaltask.model.TokenResponse;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewsViewModel extends ViewModel implements NetworkingManager.OnCallListener {

    private static final String TAG = NewsViewModel.class.getSimpleName();

    private int currentPage = 0;
    private NetworkingManager networkingManager =  new NetworkingManager(this);
    private Executor backgroundExecutor = Executors.newSingleThreadExecutor();

    public final MutableLiveData<Boolean> loading =  new MutableLiveData<>();
    public final MutableLiveData<String> errorMsg =  new MutableLiveData<>();
    public final MutableLiveData<List<NewsItem>> items = new MutableLiveData<>();
    public final MutableLiveData<TokenResponse> tokenRes = new MutableLiveData<>();

    @Override
    public void onDataAvailable(int callType, Object data) {
            if(callType == CALL_TYPE_NEWS){
                ServerResponse response = (ServerResponse) data;
                List<NewsItem> newsItems = response.data;
                if(newsItems == null || newsItems.isEmpty()) {//end of pages or error
                    loading.setValue(false);
                    return;
                }
                resolveDiffs(newsItems);

            }else {//token
                TokenResponse response = (TokenResponse) data;
                tokenRes.setValue(response);
                Log.d(TAG,"Requesting with new token:"+response.token);
                networkingManager.getNewsPage(response.token,currentPage);
            }
    }

    @Override
    public void onError(int callType, String msg, Object extra) {
        loading.setValue(false);
        String prefix = "";
        if(callType == CALL_TYPE_NEWS){
            prefix = "News";
        }else {//token
            prefix = "Token";
        }
        errorMsg.setValue(" "+prefix+" : "+msg);
    }

    private void resolveDiffs(final List<NewsItem> pageItems){
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<NewsItem> allItems = items.getValue();
                //List<NewsItem> mergedItems = new ArrayList<>();
                if(allItems != null && !allItems.isEmpty()){
                    for (final NewsItem item:allItems ) {
                        pageItems.removeIf(i -> i.id .equalsIgnoreCase(item.id));
                    }
                    allItems.addAll(pageItems);
                    items.postValue(allItems);
                }else
                    items.postValue(pageItems);
                ++currentPage;
                loading.postValue(false);
            }
        });
    }

    public int getCurrentPage() {
        return currentPage;
    }


    public void getNextNewsPage(String token){
        loading.setValue(true);
        if(token == null)
             networkingManager.getToken();
        else
            networkingManager.getNewsPage(token,currentPage);
    }
}
