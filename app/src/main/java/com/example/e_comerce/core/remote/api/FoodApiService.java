// core/remote/api/FoodApiService.java
package com.example.e_comerce.core.remote.api;

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

public interface FoodApiService {

    // === GET ALL FOODS ===
    @GET("api/foods")
    Call<List<FoodItem>> getAllFoods();

    // === GET FOODS BY CATEGORY ===
    @GET("api/foods/category/{category}")
    Call<List<FoodItem>> getFoodsByCategory(@Path("category") String category);

    // === GET SINGLE FOOD ===
    @GET("api/foods/{id}")
    Call<FoodItem> getFoodById(@Path("id") String id);

    // === SEARCH FOODS ===
    @GET("api/foods/search")
    Call<List<FoodItem>> searchFoods(@Query("q") String query);

    // === ADD NEW FOOD (Admin only) ===
    @POST("api/foods")
    Call<FoodItem> addFood(@Body FoodItem food);

    // === UPDATE FOOD (Admin only) ===
    @PUT("api/foods/{id}")
    Call<FoodItem> updateFood(@Path("id") String id, @Body FoodItem food);

    // === DELETE FOOD (Admin only) ===
    @DELETE("api/foods/{id}")
    Call<Void> deleteFood(@Path("id") String id);
}