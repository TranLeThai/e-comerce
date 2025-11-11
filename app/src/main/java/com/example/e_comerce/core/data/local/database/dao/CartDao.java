// core/data/local/database/dao/CartDao.java
package com.example.e_comerce.core.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.e_comerce.core.data.local.entity.CartItem;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToCart(CartItem item);

    @Query("SELECT * FROM cart")
    LiveData<List<CartItem>> getAllCartItems();

    @Delete
    void removeFromCart(CartItem item);

    @Query("DELETE FROM cart")
    void clearCart();
}