package com.example.e_comerce.core.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.repository.FoodRepository;
import java.util.List;

public class CustomerFoodViewModel extends AndroidViewModel {
    private final FoodRepository repository;

    public CustomerFoodViewModel(@NonNull Application application) {
        super(application);
        repository = FoodRepository.getInstance(application);
    }

    // Lấy danh sách món ăn từ Database (Real-time)
    public LiveData<List<FoodEntity>> getAllFoods() {
        return repository.getLocalFoods();
    }
}