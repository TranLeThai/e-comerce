// core/remote/api/FoodApiService.java
package com.example.e_comerce.core.remote.api;

import com.example.e_comerce.core.data.model.FoodItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodApiService {
    @GET("foods")
    Call<List<FoodItem>> getFoods();
}