// module_customer/cart/CartActivity.java
package com.example.e_comerce.module_customer.cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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

        initViews();
        setupDatabase();
        setupRecyclerView();
        observeCart();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewCart);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupDatabase() {
        db = AppDatabase.getInstance(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeCart() {
        LiveData<List<CartItem>> liveData = db.cartDao().getAllCartItems();
        liveData.observe(this, cartItems -> {
            if (adapter == null) {
                adapter = new CartAdapter(cartItems, this::removeItem);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            updateTotalPrice(cartItems);
        });
    }

    private void removeItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        textTotalPrice.setText(String.format("%,.0f ₫", total));
    }
}