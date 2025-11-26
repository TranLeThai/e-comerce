package com.example.e_comerce.module_customer.history;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.module_customer.menu.adapter.CustomerOrderAdapter;
import com.example.e_comerce.module_admin.viewmodel.AdminOrderViewModel;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        CustomerOrderAdapter adapter = new CustomerOrderAdapter();
        rvHistory.setAdapter(adapter);

        // Lấy dữ liệu
        AdminOrderViewModel viewModel = new ViewModelProvider(this).get(AdminOrderViewModel.class);

        viewModel.getAllOrders().observe(this, orders -> {
            adapter.setOrderList(orders);
        });
    }
}