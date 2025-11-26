package com.example.e_comerce.module_admin.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.module_admin.viewmodel.AdminFoodViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditFoodActivity extends AppCompatActivity {

    // Khai báo View theo ID mới
    private TextInputEditText etName, etPrice;
    private Spinner spinnerCategory;
    private ImageView imgFood;
    private Button btnSave;

    private AdminFoodViewModel viewModel;
    private String selectedImageStr = ""; // Lưu đường dẫn ảnh

    // Bộ chọn ảnh
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageStr = uri.toString();
                    imgFood.setImageURI(uri);
                    imgFood.setPadding(0,0,0,0); // Xóa padding để ảnh full đẹp hơn
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_food);

        // 1. Ánh xạ View (ID khớp với XML mới)
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgFood = findViewById(R.id.imgFood);
        btnSave = findViewById(R.id.btnSave);

        // Setup Toolbar (Nút back)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        // 2. Setup Spinner Danh mục
        setupCategorySpinner();

        // 3. Sự kiện chọn ảnh
        imgFood.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // 4. Sự kiện Lưu
        btnSave.setOnClickListener(v -> saveFood());
    }

    private void setupCategorySpinner() {
        // Danh sách danh mục mẫu
        String[] categories = {"Pizza", "Burger", "Chicken", "Noodles", "Drinks", "Snacks"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void saveFood() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        // Lấy giá trị từ Spinner
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu chưa chọn ảnh thì lấy ảnh mặc định
        if (selectedImageStr.isEmpty()) {
            selectedImageStr = String.valueOf(R.drawable.ic_menu_camera);
            // Hoặc R.drawable.ic_pizza tùy bạn
        }

        double price = Double.parseDouble(priceStr);

        // Tạo Entity và Lưu
        FoodEntity newFood = new FoodEntity(name, price, selectedImageStr, category);
        viewModel.insertFood(newFood);

        Toast.makeText(this, "Đã thêm món: " + name, Toast.LENGTH_SHORT).show();
        finish();
    }
}