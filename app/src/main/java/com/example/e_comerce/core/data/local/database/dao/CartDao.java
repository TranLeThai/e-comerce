package com.example.e_comerce.core.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.e_comerce.core.data.local.entity.CartItem;
import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("SELECT * FROM cart WHERE foodId = :foodId LIMIT 1")
    CartItem getCartItemById(String foodId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToCart(CartItem item);

    @Update
    void updateCartItem(CartItem item);

    @Delete
    void removeFromCart(CartItem item);

    @Query("DELETE FROM cart")
    void clearCart();

    @Query("SELECT COUNT(*) FROM cart")
    LiveData<Integer> getCartItemCount();

    @Query("SELECT SUM(price * quantity) FROM cart")
    LiveData<Double> getTotalPrice();
}