package com.example.e_comerce.module_customer.main;

import com.example.e_comerce.module_customer.product.MonAn;
import com.example.e_comerce.module_customer.product.MonAnAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ListView listView;
    private List<MonAn> dsMonAn;
    private MonAnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ view
        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listMonAn);

        // Xử lý padding cho tai thỏ nếu cần
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Tạo danh sách món ăn
        dsMonAn = new ArrayList<>();
        dsMonAn.add(new MonAn("Pizza Hải Sản", "120.000đ", R.drawable.pizza));
        dsMonAn.add(new MonAn("Burger Bò Phô Mai", "85.000đ", R.drawable.burger));
        dsMonAn.add(new MonAn("Cơm Gà Xối Mỡ", "70.000đ", R.drawable.com_ga));
        dsMonAn.add(new MonAn("Trà Sữa Trân Châu", "45.000đ", R.drawable.tra_sua));

        // Gán adapter
        adapter = new MonAnAdapter(this, dsMonAn);
        listView.setAdapter(adapter);

        // Bắt sự kiện click item trong list
        listView.setOnItemClickListener((parent, view, position, id) -> {
            MonAn mon = dsMonAn.get(position);
            Toast.makeText(this, "Bạn chọn: " + mon.getName(), Toast.LENGTH_SHORT).show();
        });

        // Ví dụ xử lý click ảnh nhà hàng
        ImageView restaurantImage = findViewById(R.id.ivRestaurantImage);
        if (restaurantImage != null) {
            restaurantImage.setOnClickListener(v ->
                    Toast.makeText(this, "Clicked on Pizza Stories", Toast.LENGTH_SHORT).show()
            );
        }
    }

    // Hàm ví dụ xử lý click danh mục
    private void setupCategoryClicks() {
        // Thêm sau nếu có category
    }
}
