// core/data/repository/CartRepository.java
package com.example.e_comerce.core.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;
import java.util.List;

public class CartRepository {
    private static CartRepository instance;
    private final AppDatabase db;

    private CartRepository(Context context) {
        db = AppDatabase.getInstance(context.getApplicationContext());
    }

    public static synchronized CartRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CartRepository(context);
        }
        return instance;
    }

    public LiveData<List<CartItem>> getCartItems() {
        return db.cartDao().getAllCartItems();
    }

    public void addToCart(CartItem item) {
        new Thread(() -> db.cartDao().addToCart(item)).start();
    }

    public void removeFromCart(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
    }

    public void clearCart() {
        new Thread(() -> db.cartDao().clearCart()).start();
    }
}