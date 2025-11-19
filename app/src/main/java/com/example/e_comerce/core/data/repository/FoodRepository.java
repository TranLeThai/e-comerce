// core/data/repository/FoodRepository.java
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
import com.example.e_comerce.core.remote.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {
    private static final String TAG = "FoodRepository";
    private static FoodRepository instance;
    private final AppDatabase db;

    private FoodRepository(Context context) {
        db = AppDatabase.getInstance(context.getApplicationContext());
    }

    public static synchronized FoodRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FoodRepository(context);
        }
        return instance;
    }

    // === GET FROM LOCAL DB ===
    public LiveData<List<FoodEntity>> getLocalFoods() {
        return db.foodDao().getAllFoods();
    }

    public FoodEntity getFoodById(String id) {
        return db.foodDao().getFoodById(id);
    }

    public LiveData<List<FoodEntity>> getFoodsByCategory(String category) {
        return db.foodDao().getFoodsByCategory(category);
    }

    public LiveData<List<String>> getAllCategories() {
        return db.foodDao().getAllCategories();
    }

//    public LiveData<List<FoodEntity>> searchFoods(String query) {
//        return db.foodDao().searchFoods(query);
//    }

    // === ADMIN OPERATIONS ===
    public void insertFood(FoodEntity food) {
        db.foodDao().insertFood(food);
    }

    public void updateFood(FoodEntity food) {
        db.foodDao().insertFood(food); // REPLACE strategy tự động update
    }

    public void deleteFood(String id) {
        new Thread(() -> {
            FoodEntity food = db.foodDao().getFoodById(id);
            if (food != null) {
                db.foodDao().deleteFood(food);
            }
        }).start();
    }

    // === FETCH FROM API & CACHE TO DB ===
    public LiveData<Boolean> fetchAndCacheFoods() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        RetrofitClient.getApiService().getAllFoods().enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodItem> foods = response.body();
                    // Lưu vào DB
                    new Thread(() -> {
                        List<FoodEntity> entities = FoodMapper.toEntityList(foods);
                        db.foodDao().deleteAllFoods(); // Xóa data cũ
                        db.foodDao().insertFoods(entities);
                        result.postValue(true);
                    }).start();
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                result.setValue(false);
            }
        });

        return result;
    }

    public LiveData<List<FoodItem>> searchFoods(String query) {
        MutableLiveData<List<FoodItem>> result = new MutableLiveData<>();

        RetrofitClient.getApiService().searchFoods(query).enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(null); // hoặc List rỗng
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e(TAG, "Search failed: " + t.getMessage());
                result.setValue(null);
            }
        });
        return result;
    }
}