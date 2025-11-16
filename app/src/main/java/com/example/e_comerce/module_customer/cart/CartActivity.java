// module_customer/cart/CartActivity.java (SIMPLE VERSION)
package com.example.e_comerce.module_customer.cart;

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
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private CartAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Khởi tạo views
        recyclerView = findViewById(R.id.recyclerViewCart);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Setup database
        db = AppDatabase.getInstance(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe cart data
        db.cartDao().getAllCartItems().observe(this, cartItems -> {
            // ✅ SỬA: Tạo lại adapter mỗi lần data thay đổi
            adapter = new CartAdapter(cartItems, this::removeItem);
            recyclerView.setAdapter(adapter);

            // Update tổng tiền
            updateTotalPrice(cartItems);
        });

        // Nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thanh toán - Đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void removeItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
        Toast.makeText(this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        textTotalPrice.setText(String.format("%,.0f ₫", total));
    }
}