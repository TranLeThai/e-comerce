package com.example.e_comerce.core.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import java.util.List;

@Dao
public interface OrderDao {
    // Thêm đơn hàng mới
    @Insert
    void insertOrder(OrderEntity order);

    // Lấy tất cả đơn hàng (Sắp xếp mới nhất lên đầu) - Dùng cho Admin sau này
    @Query("SELECT * FROM orders ORDER BY id DESC")
    LiveData<List<OrderEntity>> getAllOrders();

    @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
    void updateOrderStatus(int orderId, String newStatus);

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY id DESC")
    LiveData<List<OrderEntity>> getOrdersByStatus(String status);

    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = 'Hoàn thành'")
    LiveData<Double> getTotalRevenue();

    @Query("SELECT COUNT(*) FROM orders WHERE status = 'Hoàn thành'")
    LiveData<Integer> getCompletedOrderCount();

}