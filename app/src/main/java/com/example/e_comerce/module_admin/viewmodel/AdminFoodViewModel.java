package com.example.e_comerce.module_admin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.database.dao.FoodDao;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminFoodViewModel extends AndroidViewModel {

    private final FoodDao foodDao;
    private final ExecutorService executorService;
    private final LiveData<List<FoodItem>> allFoods;

    public AdminFoodViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        foodDao = db.foodDao();
        executorService = Executors.newSingleThreadExecutor();

        // Chuyển đổi danh sách Entity -> Model cho RecyclerView
        allFoods = Transformations.map(foodDao.getAllFoods(), entities -> {
            // (Bạn có thể thêm code convert list ở đây nếu cần)
            // Để đơn giản, giả sử Adapter của bạn xử lý list này hoặc convert tương tự bên dưới
            java.util.List<FoodItem> models = new java.util.ArrayList<>();
            for (FoodEntity entity : entities) {
                models.add(new FoodItem(
                        entity.getId(), entity.getName(), (long) entity.getPrice(),
                        entity.getImageResId(), entity.getCategory()
                ));
            }
            return models;
        });
    }

    public LiveData<List<FoodItem>> getAllFoods() {
        return allFoods;
    }

    // === HÀM QUAN TRỌNG ĐỂ LẤY CHI TIẾT MÓN ĂN ===
    public LiveData<FoodItem> getFoodById(String id) {
        return Transformations.map(foodDao.getFoodById(id), entity -> {
            if (entity == null) return null;
            return new FoodItem(
                    entity.getId(),
                    entity.getName(),
                    (long) entity.getPrice(),
                    entity.getImageResId(),
                    entity.getCategory()
            );
        });
    }

    public void addFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = new FoodEntity(
                    food.getId(), food.getName(), food.getPrice(),
                    food.getImageResId(), food.getCategory()
            );
            foodDao.insertFood(entity);
        });
    }

    public void updateFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = new FoodEntity(
                    food.getId(), food.getName(), food.getPrice(),
                    food.getImageResId(), food.getCategory()
            );
            foodDao.insertFood(entity);
        });
    }

    public void deleteFood(FoodEntity food) { // Cần nhận FoodEntity hoặc ID để xóa
        executorService.execute(() -> foodDao.deleteFood(food));
    }
    public void deleteFood(String id) {
        executorService.execute(() -> {
            // Gọi hàm xóa theo ID vừa thêm ở Bước 1
            foodDao.deleteFoodById(id);
        });
    }
}