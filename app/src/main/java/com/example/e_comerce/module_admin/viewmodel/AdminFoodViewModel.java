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

    public LiveData<FoodItem> getFoodById(String id) {
        return Transformations.map(foodDao.getFoodById(id), entity -> {
            if (entity == null) return null;
            return FoodMapper.toModel(entity);
        });
    }

    public void insertFood(FoodEntity food) {
        executorService.execute(() -> {
            foodDao.insertFood(food);
        });
    }

    public void addFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = FoodMapper.toEntity(food);
            foodDao.insertFood(entity);
        });
    }

    public void updateFood(FoodItem food) {
        executorService.execute(() -> {
            FoodEntity entity = FoodMapper.toEntity(food);
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

    public void deleteFood(String id) {
        executorService.execute(() -> {
            foodDao.deleteFoodById(id);
        });
    }
}