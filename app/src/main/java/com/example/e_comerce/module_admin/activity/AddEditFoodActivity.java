package com.example.e_comerce.module_admin.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.databinding.ActivityAddEditFoodBinding;
import com.example.e_comerce.module_admin.viewmodel.AdminFoodViewModel;

import java.util.UUID;

public class AddEditFoodActivity extends AppCompatActivity {

    private ActivityAddEditFoodBinding binding;
    private AdminFoodViewModel viewModel;
    private boolean isEditMode = false;
    private String foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        // Kiểm tra xem đang Thêm mới hay Sửa
        foodId = getIntent().getStringExtra("food_id");
        isEditMode = (foodId != null);

        setupToolbar();
        setupCategorySpinner();

        // Load dữ liệu cũ nếu là chế độ Sửa
        if (isEditMode) {
            loadFoodData();
        }

        binding.btnSave.setOnClickListener(v -> saveFood());
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Chỉnh sửa món ăn" : "Thêm món mới");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupCategorySpinner() {
        String[] categories = {"Pizza", "Burger", "Món Việt", "Dessert", "Đồ uống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void loadFoodData() {
        // Quan sát dữ liệu từ Database
        viewModel.getFoodById(foodId).observe(this, food -> {
            if (food != null) {
                binding.etName.setText(food.getName());
                binding.etPrice.setText(String.valueOf(food.getPrice()));

                // Set ảnh (Tạm thời dùng ảnh resource)
                binding.imgFood.setImageResource(food.getImageResId());

                // Set danh mục cho Spinner
                String category = food.getCategory();
                ArrayAdapter adapter = (ArrayAdapter) binding.spinnerCategory.getAdapter();
                if (adapter != null) {
                    int position = adapter.getPosition(category);
                    if (position >= 0) {
                        binding.spinnerCategory.setSelection(position);
                    }
                }
            }
        });
    }

    private void saveFood() {
        String name = binding.etName.getText().toString().trim();
        String priceStr = binding.etPrice.getText().toString().trim();
        String category = "";
        if (binding.spinnerCategory.getSelectedItem() != null) {
            category = binding.spinnerCategory.getSelectedItem().toString();
        }

        if (name.isEmpty()) {
            binding.etName.setError("Vui lòng nhập tên món");
            return;
        }
        if (priceStr.isEmpty()) {
            binding.etPrice.setError("Vui lòng nhập giá");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            binding.etPrice.setError("Giá không hợp lệ");
            return;
        }

        // Ảnh mặc định (vì chưa có chức năng upload ảnh thật)
        int defaultImage = R.drawable.pizza;

        if (isEditMode) {
            // Cập nhật món cũ (giữ nguyên ID cũ)
            FoodItem updatedFood = new FoodItem(foodId, name, price, defaultImage, category);
            viewModel.updateFood(updatedFood);
            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
        } else {
            // Tạo món mới (Sinh ID ngẫu nhiên bằng UUID)
            String newId = UUID.randomUUID().toString();
            FoodItem newFood = new FoodItem(newId, name, price, defaultImage, category);
            viewModel.addFood(newFood);
            Toast.makeText(this, "Đã thêm mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}