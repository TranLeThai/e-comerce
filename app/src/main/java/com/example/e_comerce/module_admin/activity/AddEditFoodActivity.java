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

    private TextInputEditText etName, etPrice;
    private Spinner spinnerCategory;
    private ImageView imgFood;
    private Button btnSave;

    private AdminFoodViewModel viewModel;
    private String selectedImageStr = "";

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageStr = uri.toString();
                    imgFood.setImageURI(uri);
                    imgFood.setPadding(0,0,0,0);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_food);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgFood = findViewById(R.id.imgFood);
        btnSave = findViewById(R.id.btnSave);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        setupCategorySpinner();

        imgFood.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> saveFood());
    }

    private void setupCategorySpinner() {
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

        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageStr.isEmpty()) {
            selectedImageStr = String.valueOf(R.drawable.ic_menu_camera);
        }

        double price = Double.parseDouble(priceStr);

        FoodEntity newFood = new FoodEntity(name, price, selectedImageStr, category);
        viewModel.insertFood(newFood);

        Toast.makeText(this, "Đã thêm món: " + name, Toast.LENGTH_SHORT).show();
        finish();
    }
}