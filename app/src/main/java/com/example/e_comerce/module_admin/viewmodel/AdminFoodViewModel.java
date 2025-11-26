package com.example.e_comerce.module_admin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.database.dao.FoodDao;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper; // Import Mapper
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.ArrayList;
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

        // SỬA: Dùng FoodMapper để chuyển đổi Entity -> Model
        // Không còn lỗi getImageResId hay ép kiểu sai nữa
        allFoods = Transformations.map(foodDao.getAllFoods(), entities -> {
            List<FoodItem> models = new ArrayList<>();
            if (entities != null) {
                for (FoodEntity entity : entities) {
                    models.add(FoodMapper.toModel(entity));
                }
            }
            return models;
        });
    }

    public LiveData<List<FoodItem>> getAllFoods() {
        return allFoods;
    }

    // SỬA: Dùng Mapper cho hàm lấy chi tiết
    public LiveData<FoodItem> getFoodById(String id) {
        return Transformations.map(foodDao.getFoodById(id), entity -> {
            if (entity == null) return null;
            return FoodMapper.toModel(entity);
        });
    }

    // SỬA: Hàm thêm món (Dùng Mapper)
    public void insertFood(FoodEntity food) { // Giữ nguyên nhận FoodEntity từ Activity
        executorService.execute(() -> {
            foodDao.insertFood(food);
        });
    }

    // Nếu bạn muốn truyền FoodItem vào thì dùng hàm này:
    public void addFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = FoodMapper.toEntity(food);
            foodDao.insertFood(entity);
        });
    }

    // SỬA: Hàm cập nhật
    public void updateFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = FoodMapper.toEntity(food);
            // QUAN TRỌNG: Mapper toEntity thường tạo object mới không có ID.
            // Khi update phải set lại ID cũ để Room biết đường ghi đè.
            try {
                if (food.getId() != null) {
                    entity.setId(Integer.parseInt(food.getId()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            foodDao.insertFood(entity); // Insert với ID cũ = Update
        });
    }

    // Hàm xóa theo ID (Khuyên dùng)
    public void deleteFood(String id) {
        executorService.execute(() -> {
            foodDao.deleteFoodById(id);
        });
    }
}