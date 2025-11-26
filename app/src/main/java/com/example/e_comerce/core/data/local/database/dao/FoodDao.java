package com.example.e_comerce.core.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import java.util.List;

@Dao
public interface FoodDao {


    @Query("DELETE FROM foods")
    void deleteAllFoods();

    @Query("SELECT * FROM foods")
    LiveData<List<FoodEntity>> getAllFoods();

    @Query("SELECT * FROM foods WHERE id = :id LIMIT 1")
    LiveData<FoodEntity> getFoodById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFood(FoodEntity food);

    @Query("DELETE FROM foods WHERE id = :id")
    void deleteFoodById(String id);

    @Delete
    void deleteFood(FoodEntity food);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoods(List<FoodEntity> foods);

    // 2. Lấy theo danh mục
    @Query("SELECT * FROM foods WHERE category = :category")
    LiveData<List<FoodEntity>> getFoodsByCategory(String category);

    // 3. Lấy tất cả danh mục
    @Query("SELECT DISTINCT category FROM foods")
    LiveData<List<String>> getAllCategories();

    // 4. Tìm kiếm món ăn

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%'")
    LiveData<List<FoodEntity>> searchFoods(String query);

    // 5. Đếm số lượng
    @Query("SELECT COUNT(*) FROM foods")
    int getFoodCount();
}