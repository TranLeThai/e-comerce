// module_admin/activity/OrderFoodActivity.java
package com.example.e_comerce.module_admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.databinding.ActivityOrderFoodBinding;
import com.example.e_comerce.module_admin.adapter.OrderFoodAdapter;
import com.example.e_comerce.module_admin.viewmodel.AdminFoodViewModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderFoodActivity extends AppCompatActivity {

    private ActivityOrderFoodBinding binding;
    private AdminFoodViewModel viewModel;
    private OrderFoodAdapter adapter;                    // Dùng OrderFoodAdapter riêng
    private final List<FoodItem> selectedFoods = new ArrayList<>();
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        setupToolbar();
        setupRecyclerView();
        observeFoods();
        setupConfirmButton();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chọn món ăn");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new OrderFoodAdapter(selectedFoods);   // Truyền danh sách đã chọn vào
        binding.recyclerFoods.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFoods.setAdapter(adapter);

        adapter.setOnFoodSelectListener(food -> {
            if (selectedFoods.contains(food)) {
                selectedFoods.remove(food);
                totalPrice -= food.getPrice();
                Toast.makeText(this, "Đã bỏ chọn: " + food.getName(), Toast.LENGTH_SHORT).show();
            } else {
                selectedFoods.add(food);
                totalPrice += food.getPrice();
                Toast.makeText(this, "Đã chọn: " + food.getName(), Toast.LENGTH_SHORT).show();
            }
            updateTotalPrice();
            adapter.notifyDataSetChanged();   // Cập nhật dấu tick ngay lập tức
        });
    }

    private void observeFoods() {
        viewModel.getAllFoods().observe(this, foods -> {
            if (foods != null && !foods.isEmpty()) {
                adapter.submitList(foods);
            } else {
                Toast.makeText(this, "Không có món ăn nào", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        binding.tvTotalPrice.setText("Tổng tiền: " + formatter.format(totalPrice));
    }

    private void setupConfirmButton() {
        binding.btnConfirmOrder.setOnClickListener(v -> {
            if (selectedFoods.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất 1 món", Toast.LENGTH_SHORT).show();
                return;
            }

            String orderId = "ORD_" + System.currentTimeMillis();
            String customerName = "Khách lẻ";
            String status = "Chờ xác nhận";
            String date = "18/11/2025";

            Intent result = new Intent();
            result.putExtra("order_id", orderId);
            result.putExtra("customer_name", customerName);
            result.putExtra("total_price", totalPrice);
            result.putExtra("status", status);
            result.putExtra("date", date);
            setResult(RESULT_OK, result);

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}