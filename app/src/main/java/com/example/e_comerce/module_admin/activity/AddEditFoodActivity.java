// module_admin/activity/AddEditFoodActivity.java
package com.example.e_comerce.module_admin.activity;


import com.example.e_comerce.core.data.model.FoodItem;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_comerce.R;
import com.example.e_comerce.databinding.ActivityAddEditFoodBinding;
import com.example.e_comerce.module_admin.viewmodel.AdminFoodViewModel;

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

        // Check if editing existing food
        foodId = getIntent().getStringExtra("food_id");
        isEditMode = (foodId != null);

        setupToolbar();
        setupCategorySpinner();
        setupImagePicker();

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

    private void setupImagePicker() {
        // TODO: Implement image picker (Camera/Gallery)
        binding.imgFood.setOnClickListener(v -> {
            Toast.makeText(this, "Image picker - Coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadFoodData() {
        // TODO: Load food from ViewModel by ID
        viewModel.getFoodById(foodId).observe(this, food -> {
            if (food != null) {
                binding.etName.setText(food.getName());
                binding.etPrice.setText(String.valueOf(food.getPrice()));
                binding.imgFood.setImageResource(food.getImageResId());

                // Set category spinner
                String category = food.getCategory();
                ArrayAdapter adapter = (ArrayAdapter) binding.spinnerCategory.getAdapter();
                int position = adapter.getPosition(category);
                binding.spinnerCategory.setSelection(position);
            }
        });
    }

    private void saveFood() {
        // Validate input
        String name = binding.etName.getText().toString().trim();
        String priceStr = binding.etPrice.getText().toString().trim();
        String category = binding.spinnerCategory.getSelectedItem().toString();

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
            if (price <= 0) {
                binding.etPrice.setError("Giá phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException e) {
            binding.etPrice.setError("Giá không hợp lệ");
            return;
        }

        // Create or update food
        if (isEditMode) {
            FoodItem updatedFood = new FoodItem(
                    foodId, name, price, R.drawable.burger, category
            );
            viewModel.updateFood(updatedFood);
            Toast.makeText(this, "Đã cập nhật món ăn", Toast.LENGTH_SHORT).show();
        } else {
            String newId = "food_" + System.currentTimeMillis();
            FoodItem newFood = new FoodItem(
                    newId, name, price, R.drawable.burger, category
            );
            viewModel.addFood(newFood);
            Toast.makeText(this, "Đã thêm món mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}