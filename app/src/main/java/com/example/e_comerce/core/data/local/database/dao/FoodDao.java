// core/data/local/database/dao/FoodDao.java
package com.example.e_comerce.core.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM foods")
    LiveData<List<FoodEntity>> getAllFoods();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FoodEntity> foods);

    @Query("DELETE FROM foods")
    void deleteAll();
}