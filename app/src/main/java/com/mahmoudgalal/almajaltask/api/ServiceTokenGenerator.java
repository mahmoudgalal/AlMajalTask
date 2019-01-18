/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.api;

import com.mahmoudgalal.almajaltask.model.TokenResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public interface ServiceTokenGenerator {
    @POST("./")
    Call<TokenResponse> getToken(@Body RequestBody params);
}
