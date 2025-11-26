package com.example.e_comerce.module_customer.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private CartAdapter adapter;
    private AppDatabase db;
    private List<CartItem> currentCartList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Khởi tạo Views
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        textTotalPrice = view.findViewById(R.id.textTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        // 2. Setup Database (Dùng requireContext)
        db = AppDatabase.getInstance(requireContext());

        // 3. Setup RecyclerView
        adapter = new CartAdapter(new ArrayList<>(), this::removeItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 4. Quan sát dữ liệu (Dùng getViewLifecycleOwner)
        db.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            this.currentCartList = cartItems;
            if (adapter != null) {
                // Tốt nhất là thêm hàm setData trong Adapter để tránh new lại
                adapter = new CartAdapter(cartItems, this::removeItem);
                recyclerView.setAdapter(adapter);
            }
            updateTotalPrice(cartItems);
        });

        // 5. Sự kiện Thanh Toán
        btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    private void removeItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
        Toast.makeText(getContext(), "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Logic tính toán chuỗi summary
        double totalMoney = 0;
        StringBuilder summaryBuilder = new StringBuilder();
        for (CartItem item : currentCartList) {
            totalMoney += item.getPrice() * item.getQuantity();
            summaryBuilder.append(item.getName()).append(" (x").append(item.getQuantity()).append("), ");
        }
        String summary = summaryBuilder.toString();
        if (summary.length() > 2) summary = summary.substring(0, summary.length() - 2);

        String dateNow = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        OrderEntity newOrder = new OrderEntity(dateNow, totalMoney, "Đang chờ", summary);

        new Thread(() -> {
            db.orderDao().insertOrder(newOrder);
            db.cartDao().clearCart();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    // Không cần finish() vì đây là Fragment, có thể chuyển về Home hoặc giữ nguyên
                });
            }
        }).start();
    }
}