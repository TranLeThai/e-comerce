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
    private List<Order> allOrders = new ArrayList<>();

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

        // TODO: Replace with actual repository call
        // For now, using dummy data
        new android.os.Handler().postDelayed(() -> {
            allOrders = generateDummyOrders();
            orders.setValue(allOrders);
            isLoading.setValue(false);
        }, 500);
    }

    public void filterOrdersByStatus(String status) {
        if (allOrders.isEmpty()) return;

        List<Order> filtered;
        if (status.equals("Tất cả")) {
            filtered = allOrders;
        } else {
            filtered = allOrders.stream()
                    .filter(order -> order.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        orders.setValue(filtered);
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        // TODO: Update in repository
        List<Order> current = orders.getValue();
        if (current != null) {
            for (Order order : current) {
                if (order.getId().equals(orderId)) {
                    order.setStatus(newStatus);
                    break;
                }
            }
            orders.setValue(current);
        }
    }

    private List<Order> generateDummyOrders() {
        List<Order> dummy = new ArrayList<>();
        dummy.add(new Order("ORD001", "Nguyễn Văn A", 125000, "Chờ xác nhận", "15/11/2024"));
        dummy.add(new Order("ORD002", "Trần Thị B", 89000, "Đang giao", "15/11/2024"));
        dummy.add(new Order("ORD003", "Lê Văn C", 200000, "Hoàn thành", "14/11/2024"));
        return dummy;
    }
}