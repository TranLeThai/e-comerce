// core/data/repository/CartRepository.java (IMPROVED VERSION)
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

    // === GET ALL CART ITEMS ===
    public LiveData<List<CartItem>> getCartItems() {
        return db.cartDao().getAllCartItems();
    }

    // === GET CART COUNT ===
    public LiveData<Integer> getCartItemCount() {
        return db.cartDao().getCartItemCount();
    }

    // === GET TOTAL PRICE ===
    public LiveData<Double> getTotalPrice() {
        return db.cartDao().getTotalPrice();
    }

    // === ADD TO CART (với logic tăng quantity nếu đã tồn tại) ===
    public void addToCart(CartItem newItem) {
        new Thread(() -> {
            CartItem existingItem = db.cartDao().getCartItemById(newItem.getFoodId());

            if (existingItem != null) {
                // Item đã có trong cart -> tăng quantity
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                db.cartDao().updateCartItem(existingItem);
            } else {
                // Item chưa có -> thêm mới
                db.cartDao().addToCart(newItem);
            }
        }).start();
    }

    // === UPDATE QUANTITY ===
    public void updateQuantity(String foodId, int newQuantity) {
        new Thread(() -> {
            CartItem item = db.cartDao().getCartItemById(foodId);
            if (item != null) {
                if (newQuantity <= 0) {
                    db.cartDao().removeFromCart(item);
                } else {
                    item.setQuantity(newQuantity);
                    db.cartDao().updateCartItem(item);
                }
            }
        }).start();
    }

    // === REMOVE FROM CART ===
    public void removeFromCart(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
    }

    // === CLEAR CART ===
    public void clearCart() {
        new Thread(() -> db.cartDao().clearCart()).start();
    }
}