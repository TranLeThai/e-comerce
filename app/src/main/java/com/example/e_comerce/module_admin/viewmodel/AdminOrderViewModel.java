package com.example.e_comerce.module_admin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import java.util.List;

public class AdminOrderViewModel extends AndroidViewModel {

    private final LiveData<List<OrderEntity>> allOrders;

    private final AppDatabase db;

    public AdminOrderViewModel(@NonNull Application application) {
        super(application);

        db = AppDatabase.getInstance(application);

        allOrders = db.orderDao().getAllOrders();
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return allOrders;
    }

    public LiveData<List<OrderEntity>> getOrdersByStatus(String status) {
        return db.orderDao().getOrdersByStatus(status);
    }
}