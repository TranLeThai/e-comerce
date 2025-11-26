package com.example.e_comerce.module_customer.cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.OrderEntity; // Đừng quên import cái này
import com.example.e_comerce.module_customer.cart.adapter.CartAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private CartAdapter adapter;
    private AppDatabase db;
    private List<CartItem> currentCartList; // Biến lưu tạm danh sách để dùng khi thanh toán

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // 1. Khởi tạo Views
        recyclerView = findViewById(R.id.recyclerViewCart);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        // 2. Setup Database
        db = AppDatabase.getInstance(this);

        // 3. Setup RecyclerView
        // Lưu ý: Khởi tạo Adapter với list rỗng trước để tránh lỗi null
        adapter = new CartAdapter(new ArrayList<>(), this::removeItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 4. Lắng nghe dữ liệu từ Database (Observer)
        db.cartDao().getAllCartItems().observe(this, cartItems -> {
            // Lưu danh sách mới nhất vào biến tạm
            this.currentCartList = cartItems;

            // Cập nhật dữ liệu cho Adapter
            // Nếu bạn chưa viết hàm setData trong Adapter, hãy xem ghi chú bên dưới code này (*)
            if (adapter != null) {
                // Cách 1: Nếu Adapter có hàm setData (Khuyên dùng)
                // adapter.setData(cartItems);

                // Cách 2: Tạo mới Adapter (Cách an toàn nếu bạn chưa sửa Adapter)
                adapter = new CartAdapter(cartItems, this::removeItem);
                recyclerView.setAdapter(adapter);
            }

            // Tính lại tổng tiền
            updateTotalPrice(cartItems);
        });

        // 5. Sự kiện nút Thanh Toán
        btnCheckout.setOnClickListener(v -> {
            if (currentCartList == null || currentCartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Gọi hàm xử lý thanh toán
            handleCheckout();
        });
    }

    // --- HÀM XỬ LÝ XÓA MÓN ---
    private void removeItem(CartItem item) {
        new Thread(() -> db.cartDao().removeFromCart(item)).start();
        Toast.makeText(this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    // --- HÀM TÍNH TỔNG TIỀN ---
    private void updateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        textTotalPrice.setText(String.format("%,.0f ₫", total));
    }

    // --- HÀM XỬ LÝ THANH TOÁN (LƯU ĐƠN HÀNG) ---
    private void handleCheckout() {
        // 1. Chuẩn bị dữ liệu
        double totalMoney = 0;
        StringBuilder summaryBuilder = new StringBuilder();

        for (CartItem item : currentCartList) {
            totalMoney += item.getPrice() * item.getQuantity();
            // Tạo chuỗi tóm tắt: "Pizza (x2), Burger (x1), "
            summaryBuilder.append(item.getName())
                    .append(" (x").append(item.getQuantity()).append("), ");
        }

        String summary = summaryBuilder.toString();
        // Xóa dấu phẩy thừa ở cuối nếu có
        if (summary.length() > 2) {
            summary = summary.substring(0, summary.length() - 2);
        }

        // Lấy ngày giờ hiện tại
        String dateNow = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        // 2. Tạo đối tượng OrderEntity
        OrderEntity newOrder = new OrderEntity(
                dateNow,
                totalMoney,
                "Đang chờ", // Trạng thái mặc định
                summary
        );

        // 3. Thực hiện lưu vào DB (Ở luồng phụ - Background Thread)
        new Thread(() -> {
            // A. Lưu đơn hàng vào bảng orders
            db.orderDao().insertOrder(newOrder);

            // B. Xóa sạch giỏ hàng trong bảng cart
            db.cartDao().clearCart();

            // C. Cập nhật giao diện (Về luồng chính - Main Thread)
            runOnUiThread(() -> {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                finish(); // Đóng màn hình Cart quay về Home
            });
        }).start();
    }
}