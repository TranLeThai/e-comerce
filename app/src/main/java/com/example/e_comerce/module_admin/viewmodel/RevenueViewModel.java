package com.example.e_comerce.module_admin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;

public class RevenueViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public RevenueViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<Double> getTotalRevenue() {
        return db.orderDao().getTotalRevenue();
    }

    public LiveData<Integer> getOrderCount() {
        return db.orderDao().getCompletedOrderCount();
    }
}