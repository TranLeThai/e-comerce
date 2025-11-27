package com.example.e_comerce.module_customer.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

// Bổ sung import cho interface mới
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter.OnItemInteractionListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private CartAdapter adapter;
    private AppDatabase db;
    private List<CartItem> currentCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 1. Khởi tạo Views
        recyclerView = findViewById(R.id.recyclerViewCart);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        // 2. Setup Database
        db = AppDatabase.getInstance(this);

        // 3. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(new ArrayList<>(), createInteractionListener());
        recyclerView.setAdapter(adapter);

        // 4. Quan sát dữ liệu
        db.cartDao().getAllCartItems().observe(this, cartItems -> {
            this.currentCartList = cartItems;

            if (adapter != null) {
                // Dùng listener đã tạo để không báo lỗi
                adapter = new CartAdapter(cartItems, createInteractionListener());
                recyclerView.setAdapter(adapter);
            }

            updateTotalPrice(cartItems);
        });

        // 5. Sự kiện nút Thanh Toán
        btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    private OnItemInteractionListener createInteractionListener() {
        return new OnItemInteractionListener() {
            @Override
            public void onRemoveClick(CartItem item) {
                removeItem(item);
            }

            @Override
            public void onQuantityChange(CartItem item, int newQuantity) {
                onQuantityChange(item, newQuantity);
            }
        };
    }

    private void removeItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
        Toast.makeText(this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void onQuantityChange(CartItem item, int newQuantity) {
        CartItem updatedItem = new CartItem(
                item.getFoodId(), item.getName(), item.getPrice(), newQuantity, item.getImage()
        );
        new Thread(() -> db.cartDao().updateCartItem(updatedItem)).start();
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        textTotalPrice.setText(String.format("%,.0f ₫", total));
    }

    private void handleCheckout() {
        if (currentCartList == null || currentCartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalMoney = 0;
        StringBuilder summaryBuilder = new StringBuilder();

        for (CartItem item : currentCartList) {
            totalMoney += item.getPrice() * item.getQuantity();
            summaryBuilder.append(item.getName())
                    .append(" (x").append(item.getQuantity()).append("), ");
        }

        String summary = "";
        if (summaryBuilder.length() > 2) {
            summary = summaryBuilder.substring(0, summaryBuilder.length() - 2);
        }

        // Chuyển sang màn hình Checkout
        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        intent.putExtra("TOTAL_PRICE", totalMoney);
        intent.putExtra("ITEMS_SUMMARY", summary);
        startActivity(intent);
    }
}