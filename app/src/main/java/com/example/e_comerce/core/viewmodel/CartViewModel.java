package com.example.e_comerce.core.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.repository.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository repository;
    private final LiveData<List<CartItem>> cartItems;
    private final LiveData<Integer> cartItemCount;
    private final LiveData<Double> totalPrice;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = CartRepository.getInstance(application);
        cartItems = repository.getCartItems();
        cartItemCount = repository.getCartItemCount();
        totalPrice = repository.getTotalPrice();
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }

    public LiveData<Double> getTotalPrice() {
        return totalPrice;
    }

    // === ADD / UPDATE / REMOVE ===
    public void addToCart(CartItem item) {
        repository.addToCart(item);
    }

    public void updateQuantity(String foodId, int quantity) {
        repository.updateQuantity(foodId, quantity);
    }

    public void removeFromCart(CartItem item) {
        repository.removeFromCart(item);
    }

    public void clearCart() {
        repository.clearCart();
    }
}
