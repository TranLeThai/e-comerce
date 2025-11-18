// core/remote/ApiService.java
package com.example.e_comerce.core.remote;

import com.example.e_comerce.core.data.model.FoodItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService { // ĐỔI TÊN TỪ FoodApiService → ApiService

    @GET("api/foods")
    Call<List<FoodItem>> getAllFoods();

    @GET("api/foods/category/{category}")
    Call<List<FoodItem>> getFoodsByCategory(@Path("category") String category);

    @GET("api/foods/{id}")
    Call<FoodItem> getFoodById(@Path("id") String id);

    @GET("api/foods/search")
    Call<List<FoodItem>> searchFoods(@Query("q") String query);

    @POST("api/foods")
    Call<FoodItem> addFood(@Body FoodItem food);

    @PUT("api/foods/{id}")
    Call<FoodItem> updateFood(@Path("id") String id, @Body FoodItem food);

    @DELETE("api/foods/{id}")
    Call<Void> deleteFood(@Path("id") String id);
}