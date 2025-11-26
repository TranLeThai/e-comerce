// core/data/local/database/AppDatabase.java
package com.example.e_comerce.core.data.local.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.e_comerce.core.data.local.database.dao.CartDao;
import com.example.e_comerce.core.data.local.database.dao.FoodDao;
import com.example.e_comerce.core.data.local.database.dao.OrderDao;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.local.entity.OrderEntity; // 1. Nhớ Import dòng này

@Database(entities = {CartItem.class, FoodEntity.class, OrderEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CartDao cartDao();
    public abstract FoodDao foodDao();
    public abstract OrderDao orderDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "ecommerce_db"
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}