//module_customer/main/MainActivity.java
package com.example.e_comerce.module_customer.main;

import com.example.e_comerce.core.data.local.database.CartViewModelFactory;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.viewmodel.CartViewModel;
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
    private Button buttonHome, buttonCart, buttonFavorite, buttonProfile;
    private EditText edtSearch;
    private ListView listView;
    private List<FoodItem> dsMonAn;
    private FoodItemAdapter adapter;

    private void showThongBao() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_khuyenmai);
        dialog.setCancelable(false); // Không cho tắt khi nhấn ngoài

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_home);

        showThongBao();

        // Ánh xạ view
        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listMonAn);
        buttonHome = findViewById(R.id.btnHome);
        buttonCart = findViewById(R.id.btnCart);
        buttonFavorite = findViewById(R.id.btnFav);
        buttonProfile = findViewById(R.id.btnProfile);

        // Xử lý padding cho tai thỏ
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        CartViewModel viewModel = new ViewModelProvider(
                this,
                new CartViewModelFactory(getApplication())
        ).get(CartViewModel.class);


        // TẠO DỮ LIỆU MẪU – ĐÚNG 100% VỚI FoodItem
        dsMonAn = new ArrayList<>();
        dsMonAn.add(new FoodItem("1", "Pizza Hải Sản", 120000, R.drawable.pizza, "Pizza"));
        dsMonAn.add(new FoodItem("2", "Burger Bò Phô Mai", 85000, R.drawable.burger, "Burger"));
        dsMonAn.add(new FoodItem("3", "Cơm Gà Xối Mỡ", 70000, R.drawable.com_ga, "Cơm"));
        dsMonAn.add(new FoodItem("4", "Trà Sữa Trân Châu", 45000, R.drawable.tra_sua, "Đồ uống"));

        // GÁN ADAPTER – KHÔNG CẦN LISTENER
        adapter = new FoodItemAdapter(this, dsMonAn);
        listView.setAdapter(adapter);

        // CLICK ITEM → HIỂN THỊ TÊN
        listView.setOnItemClickListener((parent, view, position, id) -> {
            FoodItem mon = dsMonAn.get(position);

            CartItem item = new CartItem(
                    mon.getId(),
                    mon.getName(),
                    mon.getPrice(),
                    1,
                    String.valueOf(mon.getImageResId())
            );

            viewModel.addToCart(item);   // ⬅ THÊM VÀO CART

            Toast.makeText(this, "Đã thêm " + mon.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });


        // CLICK ẢNH NHÀ HÀNG
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