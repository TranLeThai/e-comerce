package com.example.e_comerce.module_customer.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.prefs.PrefManager;
import com.example.e_comerce.module_customer.auth.LoginActivity;
import com.example.e_comerce.module_customer.history.OrderHistoryActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvEmail = findViewById(R.id.tvEmail);
        LinearLayout btnHistory = findViewById(R.id.btnHistory);
        LinearLayout btnLogout = findViewById(R.id.btnLogout);

        // 1. Hiển thị Email từ PrefManager (Nếu bạn đã lưu lúc Login)
        PrefManager prefManager = new PrefManager(this);
        // Nếu bạn chưa lưu email vào PrefManager thì tạm thời fix cứng hoặc lấy role
        // tvEmail.setText(prefManager.getEmail());
        tvEmail.setText("Xin chào, Khách hàng!");

        // 2. Chuyển sang màn hình Lịch sử
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // 3. Xử lý Đăng xuất
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn thoát?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    PrefManager prefManager = new PrefManager(this);
                    prefManager.logout();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}