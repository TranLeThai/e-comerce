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
import androidx.recyclerview.widget.ItemTouchHelper; // Cần import này
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import com.example.e_comerce.module_customer.cart.CheckoutActivity;
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Sửa lại class để implement Listener mới từ CartAdapter
public class CartFragment extends Fragment implements CartAdapter.OnItemInteractionListener {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private ImageView btnDeleteAll;
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

        // 1. Setup RecyclerView
        adapter = new CartAdapter(new ArrayList<>(), this); // Dùng 'this' vì Fragment implement Listener
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 2. Kích hoạt Vuốt xóa (Swipe-to-Delete)
        setupItemTouchHelper();

        // 3. Quan sát dữ liệu
        db.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            this.currentCartList = cartItems;

            if (adapter != null) {
                adapter.setCartItems(cartItems);
            }

            updateTotalPrice(cartItems);

            // Cập nhật trạng thái nút (Giữ nguyên)
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
        btnDeleteAll.setOnClickListener(v -> showClearCartDialog());
    }


    @Override
    public void onRemoveClick(CartItem item) {
        // Hiện hộp thoại xác nhận
        showConfirmRemoveDialog(item);
    }

    @Override
    public void onQuantityChange(CartItem item, int newQuantity) {
        CartItem updatedItem = new CartItem(
                item.getFoodId(),
                item.getName(),
                item.getPrice(),
                newQuantity,
                item.getImage()
        );
        new Thread(() -> db.cartDao().updateCartItem(updatedItem)).start();
    }


    private void directRemoveItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
    }

    private void showConfirmRemoveDialog(CartItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa món ăn")
                .setMessage("Bạn muốn xóa '" + item.getName() + "' khỏi giỏ?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    directRemoveItem(item);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

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

        double totalMoney = 0;
        StringBuilder summaryBuilder = new StringBuilder();
        for (CartItem item : currentCartList) {
            totalMoney += item.getPrice() * item.getQuantity();
            summaryBuilder.append(item.getName()).append(" (x").append(item.getQuantity()).append("), ");
        }
        String summary = summaryBuilder.length() > 2 ? summaryBuilder.substring(0, summaryBuilder.length() - 2) : "";

        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.putExtra("TOTAL_PRICE", totalMoney);
        intent.putExtra("ITEMS_SUMMARY", summary);
        startActivity(intent);
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItem item = currentCartList.get(position);

                directRemoveItem(item);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}