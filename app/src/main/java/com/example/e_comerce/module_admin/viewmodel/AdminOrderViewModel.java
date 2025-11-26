package com.example.e_comerce.module_admin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import java.util.List;

public class AdminOrderViewModel extends AndroidViewModel {

    // Biến lưu danh sách tất cả (được load sẵn)
    private final LiveData<List<OrderEntity>> allOrders;

    // Biến database dùng để gọi các query khác
    private final AppDatabase db;

    public AdminOrderViewModel(@NonNull Application application) {
        super(application);

        // --- SỬA LỖI Ở ĐÂY ---
        // Không khai báo "AppDatabase db =" nữa, mà gán trực tiếp
        db = AppDatabase.getInstance(application);

        // Load danh sách mặc định
        allOrders = db.orderDao().getAllOrders();
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return allOrders;
    }

    // Hàm này sẽ được gọi khi chọn Spinner
    public LiveData<List<OrderEntity>> getOrdersByStatus(String status) {
        // Bây giờ biến db đã có giá trị, không bị Null nữa
        return db.orderDao().getOrdersByStatus(status);
    }
}