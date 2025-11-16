// core/data/local/database/dao/FoodDao.java
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

    // ========== BASIC QUERIES ==========

    /**
     * Lấy tất cả món ăn (cho HomeFragment)
     * @return LiveData list để tự động update UI
     */
    @Query("SELECT * FROM foods")
    LiveData<List<FoodEntity>> getAllFoods();

    /**
     * Lấy 1 món ăn theo ID (cho FoodDetailActivity)
     * @param id ID của món ăn
     * @return FoodEntity hoặc null nếu không tìm thấy
     */
    @Query("SELECT * FROM foods WHERE id = :id LIMIT 1")
    FoodEntity getFoodById(String id);

    // ========== FILTER BY CATEGORY ==========

    /**
     * Lọc món ăn theo category (cho CategoryFragment/Tab)
     * @param category Tên category (VD: "Pizza", "Burger")
     * @return LiveData list món ăn thuộc category đó
     */
    @Query("SELECT * FROM foods WHERE category = :category")
    LiveData<List<FoodEntity>> getFoodsByCategory(String category);

    /**
     * Lấy danh sách tất cả categories (không trùng lặp)
     * Dùng để tạo TabLayout động
     * @return LiveData list categories
     */
    @Query("SELECT DISTINCT category FROM foods")
    LiveData<List<String>> getAllCategories();

    // ========== INSERT OPERATIONS ==========

    /**
     * Thêm nhiều món ăn cùng lúc (khi fetch từ API)
     * OnConflictStrategy.REPLACE: Nếu ID trùng thì update
     * @param foods Danh sách FoodEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoods(List<FoodEntity> foods);

    /**
     * Thêm 1 món ăn (cho Admin thêm món mới)
     * @param food FoodEntity cần thêm
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFood(FoodEntity food);

    // ========== DELETE OPERATIONS ==========

    /**
     * Xóa toàn bộ món ăn (khi refresh data từ API)
     */
    @Query("DELETE FROM foods")
    void deleteAllFoods();

    /**
     * Xóa 1 món ăn cụ thể (cho Admin)
     * @param food FoodEntity cần xóa
     */
    @Delete
    void deleteFood(FoodEntity food);

    // ========== SEARCH (OPTIONAL) ==========

    /**
     * Tìm kiếm món ăn theo tên (cho SearchView)
     * @param query Từ khóa tìm kiếm
     * @return LiveData list kết quả
     */
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%'")
    LiveData<List<FoodEntity>> searchFoods(String query);

    // ========== COUNT (OPTIONAL) ==========

    /**
     * Đếm tổng số món ăn
     * @return Số lượng món ăn
     */
    @Query("SELECT COUNT(*) FROM foods")
    int getFoodCount();
}