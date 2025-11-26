package com.example.e_comerce.module_admin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import com.example.e_comerce.module_admin.adapter.AdminOrderAdapter;
import com.example.e_comerce.module_admin.viewmodel.AdminOrderViewModel;

import java.util.List;

public class ManageOrderFragment extends Fragment {

    private RecyclerView rvOrders;
    private TextView tvEmpty;
    private AutoCompleteTextView spinnerFilter;

    private AdminOrderAdapter adapter;
    private AdminOrderViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_order, container, false);

        // 1. Ánh xạ View
        rvOrders = view.findViewById(R.id.rvOrderList);
        tvEmpty = view.findViewById(R.id.tv_empty);
        spinnerFilter = view.findViewById(R.id.spinner_filter);

        // 2. Setup ViewModel
        viewModel = new ViewModelProvider(this).get(AdminOrderViewModel.class);

        // 3. Setup RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminOrderAdapter(order -> {
            // Sự kiện khi click vào đơn hàng
            showUpdateStatusDialog(order);
        });
        rvOrders.setAdapter(adapter);

        // 4. Setup bộ lọc (Spinner)
        setupFilterSpinner();

        // 5. Load dữ liệu mặc định (Tất cả)
        loadOrders("Tất cả");

        return view;
    }

    // --- HÀM CÀI ĐẶT SPINNER LỌC ---
    private void setupFilterSpinner() {
        // Danh sách trạng thái (Phải khớp với Database)
        String[] filterOptions = {"Tất cả", "Đang chờ", "Đang giao", "Hoàn thành", "Đã hủy"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                filterOptions
        );

        spinnerFilter.setAdapter(adapterSpinner);

        // Mặc định chọn "Tất cả" nhưng không kích hoạt lọc ngay lập tức để tránh load 2 lần
        spinnerFilter.setText(filterOptions[0], false);

        // Bắt sự kiện khi chọn item trong spinner
        spinnerFilter.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStatus = filterOptions[position];
            loadOrders(selectedStatus);
        });
    }

    // --- HÀM TẢI DỮ LIỆU ---
    private void loadOrders(String status) {
        // Xóa các observer cũ để tránh chồng chéo dữ liệu khi switch qua lại
        viewModel.getAllOrders().removeObservers(getViewLifecycleOwner());
        // Lưu ý: Với getOrdersByStatus trả về LiveData mới mỗi lần gọi,
        // nên việc removeObservers triệt để ở đây chỉ mang tính tương đối trong scope cơ bản này.

        if (status.equals("Tất cả")) {
            viewModel.getAllOrders().observe(getViewLifecycleOwner(), this::updateList);
        } else {
            // Lọc theo trạng thái cụ thể
            viewModel.getOrdersByStatus(status).observe(getViewLifecycleOwner(), this::updateList);
        }
    }

    // --- HÀM CẬP NHẬT GIAO DIỆN LIST ---
    private void updateList(List<OrderEntity> orders) {
        if (orders != null && !orders.isEmpty()) {
            rvOrders.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            adapter.setOrderList(orders);
        } else {
            rvOrders.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    // --- HIỆN DIALOG CẬP NHẬT TRẠNG THÁI ---
    private void showUpdateStatusDialog(OrderEntity order) {
        String[] statuses = {"Đang chờ", "Đang giao", "Hoàn thành", "Đã hủy"};

        // Tìm vị trí của trạng thái hiện tại để check sẵn vào dialog
        int checkedItem = -1;
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(order.status)) {
                checkedItem = i;
                break;
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Cập nhật trạng thái đơn #" + order.id)
                .setSingleChoiceItems(statuses, checkedItem, (dialog, which) -> {
                    String selectedStatus = statuses[which];

                    // Thực hiện update vào Database
                    updateOrderStatus(order.id, selectedStatus);

                    dialog.dismiss(); // Đóng dialog
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // --- GỌI DATABASE ĐỂ UPDATE ---
    private void updateOrderStatus(int orderId, String newStatus) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            db.orderDao().updateOrderStatus(orderId, newStatus);

            // Thông báo lên Main Thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Đã cập nhật: " + newStatus, Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}