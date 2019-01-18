/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.api;


import com.mahmoudgalal.almajaltask.model.ServerResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("./")
    Call<ServerResponse> getNewsPage(@Query("token") String token, @Query("page") int page);
}
