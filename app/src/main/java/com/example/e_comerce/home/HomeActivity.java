package com.example.e_comerce.home; // <-- Thay đổi package name này cho đúng với project của bạn

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.R;

public class HomeActivity extends AppCompatActivity {

    // Khai báo các View mà chúng ta cần tương tác
    private EditText edtSearch;
    // Bạn có thể khai báo thêm các View khác ở đây nếu cần, ví dụ:
    // private LinearLayout categoryChicken, categoryBurger, ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đặt layout cho Activity này.
        // Giả sử tên file XML của bạn là "activity_home.xml"
        setContentView(R.layout.activity_home);

        // Ánh xạ các View từ file layout XML vào các biến Java
        edtSearch = findViewById(R.id.edtSearch);

        // (Optional) Xử lý padding cho màn hình tai thỏ, giọt nước...
        // Dựa trên ID "main" của ScrollView
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // === Thêm logic cho các View ở đây ===

        // Ví dụ: Thêm sự kiện click cho các danh mục (dù chúng không có ID)
        // Bạn có thể tìm chúng thông qua layout cha
        setupCategoryClicks();

        // Ví dụ: Xử lý khi người dùng nhấn vào nhà hàng "Pizza Stories"
        ImageView restaurantImage = findViewById(R.id.ivRestaurantImage); // <-- Bạn cần thêm ID này vào ImageView trong XML
        restaurantImage.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Clicked on Pizza Stories", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình chi tiết nhà hàng ở đây
        });
    }


    /**
     * Hàm ví dụ để thiết lập sự kiện click cho các danh mục.
     * Đây là một cách, bạn cũng có thể dùng RecyclerView để làm việc này hiệu quả hơn.
     */
    private void setupCategoryClicks() {
        // Bạn sẽ cần thêm ID cho các LinearLayout chứa danh mục trong file XML
        // Ví dụ: android:id="@+id/category_chicken"

        // LinearLayout categoryChicken = findViewById(R.id.category_chicken);
        // if (categoryChicken != null) {
        //     categoryChicken.setOnClickListener(v -> {
        //         Toast.makeText(HomeActivity.this, "Selected Chicken", Toast.LENGTH_SHORT).show();
        //     });
        // }
        // Tương tự cho Burger, Pizza, Noodles...
    }
}