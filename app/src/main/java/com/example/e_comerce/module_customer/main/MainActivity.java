package com.example.e_comerce.module_customer.main;

import com.example.e_comerce.core.data.local.database.CartViewModelFactory;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.FoodEntity; // Import mới
import com.example.e_comerce.core.data.mapper.FoodMapper; // Import mới
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.viewmodel.CartViewModel;
import com.example.e_comerce.core.viewmodel.CustomerFoodViewModel; // Import mới
import com.example.e_comerce.module_customer.cart.CartActivity;
import com.example.e_comerce.module_customer.menu.adapter.FoodItemAdapter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // ... Khai báo biến giữ nguyên
    private Button buttonHome, buttonCart, buttonFavorite, buttonProfile;
    private EditText edtSearch;
    private ListView listView;
    private List<FoodItem> dsMonAn;
    private FoodItemAdapter adapter;

    // Thêm ViewModel lấy danh sách món ăn
    private CustomerFoodViewModel foodViewModel;

    private void showThongBao() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_khuyenmai);
        dialog.setCancelable(false);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_home);

        showThongBao();

        // Ánh xạ view (Giữ nguyên)
        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listMonAn);
        buttonHome = findViewById(R.id.btnHome);
        buttonCart = findViewById(R.id.btnCart);
        buttonFavorite = findViewById(R.id.btnFav);
        buttonProfile = findViewById(R.id.btnProfile);

        // Xử lý padding (Giữ nguyên)
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // 1. ViewModel Giỏ hàng (Giữ nguyên)
        CartViewModel cartViewModel = new ViewModelProvider(
                this,
                new CartViewModelFactory(getApplication())
        ).get(CartViewModel.class);

        // 2. ViewModel Món ăn (THÊM MỚI)
        foodViewModel = new ViewModelProvider(this).get(CustomerFoodViewModel.class);

        // 3. Khởi tạo Adapter với danh sách rỗng trước
        dsMonAn = new ArrayList<>();
        adapter = new FoodItemAdapter(this, dsMonAn);
        listView.setAdapter(adapter);

        // 4. QUAN SÁT DỮ LIỆU TỪ DATABASE (THAY CHO DỮ LIỆU GIẢ)
        foodViewModel.getAllFoods().observe(this, foodEntities -> {
            if (foodEntities != null) {
                dsMonAn.clear(); // Xóa dữ liệu cũ

                // Chuyển đổi từ Entity (Database) sang Model (Hiển thị)
                for (FoodEntity entity : foodEntities) {
                    // Dùng Mapper cho gọn (nếu bạn đã có Mapper.toModel)
                    // Hoặc viết tay như sau:
                    dsMonAn.add(FoodMapper.toModel(entity));
                }

                // Cập nhật giao diện
                adapter.notifyDataSetChanged();
            }
        });

        // 5. Xử lý Click Item (Giữ nguyên logic)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            FoodItem mon = dsMonAn.get(position);

            CartItem item = new CartItem(
                    mon.getId(),
                    mon.getName(),
                    mon.getPrice(),
                    1,
                    String.valueOf(mon.getImageResId())
            );

            cartViewModel.addToCart(item);
            Toast.makeText(this, "Đã thêm " + mon.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // ... Các sự kiện Click khác giữ nguyên
        ImageView restaurantImage = findViewById(R.id.ivRestaurantImage);
        if (restaurantImage != null) {
            restaurantImage.setOnClickListener(v ->
                    Toast.makeText(this, "Clicked on Pizza Stories", Toast.LENGTH_SHORT).show()
            );
        }

        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }
}