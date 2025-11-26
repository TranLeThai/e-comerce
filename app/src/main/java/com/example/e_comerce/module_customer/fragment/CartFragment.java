package com.example.e_comerce.module_customer.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.e_comerce.module_customer.cart.CheckoutActivity;
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private ImageView btnDeleteAll; // Nút xóa hết
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

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        textTotalPrice = view.findViewById(R.id.textTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnDeleteAll = view.findViewById(R.id.btnDeleteAll);

        db = AppDatabase.getInstance(requireContext());

        adapter = new CartAdapter(new ArrayList<>(), this::confirmRemoveItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            this.currentCartList = cartItems;
            adapter = new CartAdapter(cartItems, this::confirmRemoveItem);
            recyclerView.setAdapter(adapter);

            updateTotalPrice(cartItems);

            if (cartItems == null || cartItems.isEmpty()) {
                btnDeleteAll.setVisibility(View.GONE);
                btnCheckout.setEnabled(false);
                btnCheckout.setAlpha(0.5f);
            } else {
                btnDeleteAll.setVisibility(View.VISIBLE);
                btnCheckout.setEnabled(true);
                btnCheckout.setAlpha(1.0f);
            }
        });

        btnCheckout.setOnClickListener(v -> handleCheckout());

        // Sự kiện Xóa Hết
        btnDeleteAll.setOnClickListener(v -> showClearCartDialog());
    }

    // Hộp thoại xác nhận xóa 1 món
    private void confirmRemoveItem(CartItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa món ăn")
                .setMessage("Bạn muốn xóa '" + item.getName() + "' khỏi giỏ?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new Thread(() -> db.cartDao().removeFromCart(item)).start();
                    Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Hộp thoại xác nhận xóa hết giỏ
    private void showClearCartDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa giỏ hàng")
                .setMessage("Bạn có chắc muốn xóa tất cả món ăn?")
                .setPositiveButton("Xóa hết", (dialog, which) -> {
                    new Thread(() -> db.cartDao().clearCart()).start();
                    Toast.makeText(getContext(), "Giỏ hàng đã được làm trống", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        String formattedTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
        textTotalPrice.setText(formattedTotal);
    }

    private void handleCheckout() {
        if (currentCartList == null || currentCartList.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tính tổng tiền
        double totalMoney = 0;
        StringBuilder summaryBuilder = new StringBuilder();
        for (CartItem item : currentCartList) {
            totalMoney += item.getPrice() * item.getQuantity();
            summaryBuilder.append(item.getName()).append(" (x").append(item.getQuantity()).append("), ");
        }
        String summary = summaryBuilder.length() > 2 ? summaryBuilder.substring(0, summaryBuilder.length() - 2) : "";

        // 2. Chuyển sang màn hình CheckoutActivity
        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.putExtra("TOTAL_PRICE", totalMoney);
        intent.putExtra("ITEMS_SUMMARY", summary);
        startActivity(intent);
    }
}