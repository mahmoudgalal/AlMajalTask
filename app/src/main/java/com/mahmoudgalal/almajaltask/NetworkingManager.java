/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask;

import com.mahmoudgalal.almajaltask.api.NewsApi;
import com.mahmoudgalal.almajaltask.api.ServiceTokenGenerator;
import com.mahmoudgalal.almajaltask.model.ServerResponse;
import com.mahmoudgalal.almajaltask.model.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkingManager {

    private Retrofit retrofit ;

    private  OnCallListener onCallListener ;
    public interface OnCallListener{
       int CALL_TYPE_TOKEN = 0;
       int CALL_TYPE_NEWS = 1;

       void onDataAvailable(int callType,Object data);
       void onError(int callType,String msg,Object extra);
    }

    public NetworkingManager(OnCallListener onCallListener){
        this.onCallListener = onCallListener;
        retrofit = new Retrofit.Builder()
                .baseUrl(Utils.NEWS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void getToken(){
        ServiceTokenGenerator tokenGenerator = retrofit.create(ServiceTokenGenerator.class);
       // Map<String,String> params = new HashMap<>();
        //params.put("app_key","demo");params.put("app_secret","12345678");
        String params = "app_key=demo&app_secret=12345678";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),params);
        Call<TokenResponse>  token = tokenGenerator.getToken(requestBody);
        token.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(onCallListener != null){
                    if(response.isSuccessful())
                    onCallListener.onDataAvailable(OnCallListener.CALL_TYPE_TOKEN,
                            response.body());
                    else
                        onCallListener.onError(OnCallListener.CALL_TYPE_TOKEN,
                                response.message(),response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                if(onCallListener != null)
                onCallListener.onError(OnCallListener.CALL_TYPE_TOKEN,
                        t.getMessage(),t);
            }
        });
    }

    public void getNewsPage(String token,int page){
        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<ServerResponse> serverResponseCall = newsApi.getNewsPage(token,page);
        serverResponseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(onCallListener != null){
                    if(response.isSuccessful())
                    onCallListener.onDataAvailable(OnCallListener.CALL_TYPE_NEWS,
                            response.body());
                    else
                        onCallListener.onError(OnCallListener.CALL_TYPE_NEWS,
                                response.message(),
                                response.code());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if(onCallListener != null){
                    onCallListener.onError(OnCallListener.CALL_TYPE_NEWS,t.getMessage(),t);
                }
            }
        });
    }
}
