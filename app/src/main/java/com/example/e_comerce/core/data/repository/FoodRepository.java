package com.example.e_comerce.core.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {

    private static final String TAG = "FoodRepository";
    private static FoodRepository instance;

    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private FoodRepository(Context context) {
        db = AppDatabase.getInstance(context.getApplicationContext());
    }

    public static synchronized FoodRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FoodRepository(context);
        }
        return instance;
    }

    // === LOCAL DB (Đã sửa trả về LiveData) ===
    public LiveData<List<FoodEntity>> getLocalFoods() {
        return db.foodDao().getAllFoods();
    }

    public LiveData<FoodEntity> getFoodById(String id) {
        return db.foodDao().getFoodById(id);
    }

    public LiveData<List<FoodEntity>> getFoodsByCategory(String category) {
        return db.foodDao().getFoodsByCategory(category);
    }

    public LiveData<List<String>> getAllCategories() {
        return db.foodDao().getAllCategories();
    }

    // === ADMIN ===
    public void insertFood(FoodEntity food) {
        executor.execute(() -> db.foodDao().insertFood(food));
    }

    public void updateFood(FoodEntity food) {
        executor.execute(() -> db.foodDao().insertFood(food));
    }

    public void deleteFood(String id) {
        executor.execute(() -> {
            // LƯU Ý: Phải đảm bảo bạn đã thêm deleteFoodById vào FoodDao
            db.foodDao().deleteFoodById(id);
        });
    }

    // === FETCH API & CACHE LOCAL (PHẦN BẠN ĐANG THIẾU) ===
    public LiveData<Boolean> fetchAndCacheFoods() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        // Kiểm tra xem RetrofitClient có null không để tránh crash nếu chưa config mạng
        if (RetrofitClient.getApiService() == null) {
            result.postValue(false);
            return result;
        }

        RetrofitClient.getApiService().getAllFoods().enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodItem> foods = response.body();
                    executor.execute(() -> {
                        // Mapper convert từ Model -> Entity
                        List<FoodEntity> entities = FoodMapper.toEntityList(foods);
                        db.foodDao().deleteAllFoods();
                        db.foodDao().insertFoods(entities);
                        result.postValue(true);
                    });
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                result.postValue(false);
            }
        });

        return result;
    }

    // === SEARCH ONLINE ===
    public LiveData<List<FoodItem>> searchFoods(String query) {
        MutableLiveData<List<FoodItem>> result = new MutableLiveData<>();

        if (RetrofitClient.getApiService() == null) {
            result.postValue(null);
            return result;
        }

        RetrofitClient.getApiService().searchFoods(query).enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e(TAG, "Search failed: " + t.getMessage());
                result.postValue(null);
            }
        });

        return result;
    }
}