package com.mts.kidsapp.retrofit;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("nursery-kids-top-learning-rhymes-and-poems-videos/poems.php")
    Call<JsonObject> callApi();

    @GET("nursery-kids-top-learning-rhymes-and-poems-videos/categories.php")
    Call<JsonObject> categoriesApi();
}
