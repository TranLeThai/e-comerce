// module_admin/viewmodel/AdminOrderViewModel.java
package com.example.e_comerce.module_admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_comerce.core.data.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminOrderViewModel extends ViewModel {

    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private List<Order> allOrders = new ArrayList<>(); // Danh sách gốc

    public AdminOrderViewModel() {
        loadAllOrders();
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void loadAllOrders() {
        isLoading.setValue(true);

        new android.os.Handler().postDelayed(() -> {
            allOrders = generateDummyOrders();
            orders.setValue(new ArrayList<>(allOrders)); // Clone để tránh reference
            isLoading.setValue(false);
        }, 500);
    }

    public void filterOrdersByStatus(String status) {
        if (allOrders.isEmpty()) {
            orders.setValue(new ArrayList<>());
            return;
        }

        List<Order> filtered;
        if (status.equals("Tất cả")) {
            filtered = new ArrayList<>(allOrders);
        } else {
            filtered = allOrders.stream()
                    .filter(order -> order.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        orders.setValue(filtered);
    }

    // SỬA: CẬP NHẬT CẢ allOrders VÀ orders
    public void updateOrderStatus(String orderId, String newStatus) {
        // Cập nhật trong allOrders (danh sách gốc)
        for (Order order : allOrders) {
            if (order.getId().equals(orderId)) {
                order.setStatus(newStatus);
                break;
            }
        }

        // Cập nhật trong orders (danh sách đang hiển thị)
        List<Order> current = orders.getValue();
        if (current != null) {
            for (Order order : current) {
                if (order.getId().equals(orderId)) {
                    order.setStatus(newStatus);
                    break;
                }
            }
            orders.setValue(new ArrayList<>(current)); // Trigger observer
        }
    }

    // CẢI TIẾN: Thêm nhiều trạng thái + dữ liệu thực tế hơn
    private List<Order> generateDummyOrders() {
        List<Order> dummy = new ArrayList<>();
        dummy.add(new Order("ORD001", "Nguyễn Văn A", 125000, "Chờ xác nhận", "17/11/2025"));
        dummy.add(new Order("ORD002", "Trần Thị B", 89000, "Đang giao", "17/11/2025"));
        dummy.add(new Order("ORD003", "Lê Văn C", 200000, "Hoàn thành", "16/11/2025"));
        dummy.add(new Order("ORD004", "Phạm Thị D", 150000, "Đã hủy", "16/11/2025"));
        dummy.add(new Order("ORD005", "Hoàng Văn E", 95000, "Chờ xác nhận", "15/11/2025"));
        dummy.add(new Order("ORD006", "Vũ Thị F", 180000, "Đang giao", "15/11/2025"));
        return dummy;
    }
}