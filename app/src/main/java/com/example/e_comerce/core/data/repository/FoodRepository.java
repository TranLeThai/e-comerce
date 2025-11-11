package com.example.e_comerce.core.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.remote.api.FoodApiService;
import com.example.e_comerce.core.remote.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {

    private static FoodRepository instance;
    private final FoodApiService apiService;
    private final AppDatabase db;
    private final MutableLiveData<List<FoodItem>> foodListLiveData = new MutableLiveData<>();

    private FoodRepository(Context context) {
        db = AppDatabase.getInstance(context.getApplicationContext());
        apiService = RetrofitClient.getApiService();
    }

    public static synchronized FoodRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FoodRepository(context);
        }
        return instance;
    }

    public LiveData<List<FoodItem>> getFoods() {
        // 1. Lấy cache từ Room
        db.foodDao().getAllFoods().observeForever(entities -> {
            if (entities != null && !entities.isEmpty()) {
                List<FoodItem> cached = FoodMapper.toModelList(entities);
                foodListLiveData.postValue(cached);
            }
        });

        // 2. Gọi API + lưu cache
        apiService.getFoods().enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodItem> foods = response.body();
                    foodListLiveData.postValue(foods);

                    // Lưu vào Room
                    List<FoodEntity> entities = FoodMapper.toEntityList(foods);
                    new Thread(() -> db.foodDao().insertAll(entities)).start();
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                // Dùng cache nếu API lỗi
            }
        });

        return foodListLiveData;
    }
}