package com.example.e_comerce.core.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {

    private static CartRepository instance;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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

    public LiveData<Integer> getCartItemCount() {
        return db.cartDao().getCartItemCount();
    }

    public LiveData<Double> getTotalPrice() {
        return db.cartDao().getTotalPrice();
    }

    public void addToCart(CartItem newItem) {
        executor.execute(() -> {
            CartItem existingItem = db.cartDao().getCartItemById(newItem.getFoodId());

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                db.cartDao().updateCartItem(existingItem);
            } else {
                db.cartDao().addToCart(newItem);
            }
        });
    }

    public void updateQuantity(String foodId, int newQuantity) {
        executor.execute(() -> {
            CartItem item = db.cartDao().getCartItemById(foodId);
            if (item != null) {
                if (newQuantity <= 0) {
                    db.cartDao().removeFromCart(item);
                } else {
                    item.setQuantity(newQuantity);
                    db.cartDao().updateCartItem(item);
                }
            }
        });
    }

    public void removeFromCart(CartItem item) {
        executor.execute(() -> db.cartDao().removeFromCart(item));
    }

    public void clearCart() {
        executor.execute(() -> db.cartDao().clearCart());
    }
}
