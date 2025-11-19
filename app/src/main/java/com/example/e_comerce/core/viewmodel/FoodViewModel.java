package com.example.e_comerce.core.viewmodel;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.data.repository.FoodRepository;
import com.example.e_comerce.core.remote.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodViewModel extends AndroidViewModel {

    private final FoodRepository repository;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = FoodRepository.getInstance(application);
    }

    // === LOCAL DB ===
    public LiveData<List<FoodEntity>> getLocalFoods() {
        return repository.getLocalFoods();
    }

    public LiveData<List<FoodEntity>> getFoodsByCategory(String category) {
        return repository.getFoodsByCategory(category);
    }

    public LiveData<List<String>> getAllCategories() {
        return repository.getAllCategories();
    }

    // === API FETCH & CACHE ===
    public LiveData<Boolean> fetchAndCacheFoods() {
        return repository.fetchAndCacheFoods();
    }

    // === SEARCH ONLINE ===
    public LiveData<List<FoodItem>> searchFoodsOnline(String query) {
        MutableLiveData<List<FoodItem>> result = new MutableLiveData<>();

        RetrofitClient.getApiService().searchFoods(query).enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Luôn dùng postValue khi đang thread khác
                    result.postValue(response.body());
                } else {
                    // Nếu API trả về lỗi, trả list rỗng thay vì null để tránh crash UI
                    result.postValue(Collections.emptyList());
                    Log.e(TAG, "Search API error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e(TAG, "Search API failed: " + t.getMessage());
                t.printStackTrace();
                result.postValue(Collections.emptyList());
            }
        });

        return result;
    }

    // === ADMIN OPERATIONS ===
    public void insertFood(FoodEntity food) {
        repository.insertFood(food);
    }

    public void updateFood(FoodEntity food) {
        repository.updateFood(food);
    }

    public void deleteFood(String id) {
        repository.deleteFood(id);
    }
}
