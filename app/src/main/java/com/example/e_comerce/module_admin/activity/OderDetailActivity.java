// module_admin/activity/OrderDetailActivity.java (ĐÃ SỬA TÊN!)
package com.example.e_comerce.module_admin.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.databinding.ActivityOrderDetailBinding;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId = getIntent().getStringExtra("order_id");
        if (orderId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        loadOrderDetails();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết đơn hàng #" + orderId);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadOrderDetails() {
        // TODO: Load order details from database/API
        // binding.tvOrderId.setText(orderId);
        // binding.tvCustomerName.setText(...);
        // binding.tvTotalPrice.setText(...);
        // binding.recyclerOrderItems.setAdapter(...);
    }

    private void setupButtons() {
        binding.btnConfirm.setOnClickListener(v -> {
            // TODO: Update order status to "Confirmed"
            Toast.makeText(this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.btnCancel.setOnClickListener(v -> {
            // TODO: Show confirmation dialog
            // TODO: Update order status to "Cancelled"
            Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}