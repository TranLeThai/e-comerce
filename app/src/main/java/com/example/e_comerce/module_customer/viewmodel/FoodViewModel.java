// module_customer/viewmodel/FoodViewModel.java
package com.example.e_comerce.module_customer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.data.repository.FoodRepository;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private final FoodRepository repository;
    private final LiveData<List<FoodEntity>> allFoods;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = FoodRepository.getInstance(application);
        allFoods = repository.getLocalFoods();
    }

    public LiveData<List<FoodEntity>> getAllFoods() {
        return allFoods;
    }

    public LiveData<List<FoodEntity>> getFoodsByCategory(String category) {
        return repository.getFoodsByCategory(category);
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void refreshFoods() {
        isLoading.setValue(true);
        repository.fetchAndCacheFoods().observeForever(success -> {
            isLoading.setValue(false);
        });
    }

    public void saveSampleData(List<FoodItem> foods) {
        new Thread(() -> {
            List<FoodEntity> entities = FoodMapper.toEntityList(foods);
            for (FoodEntity entity : entities) {
                repository.insertFood(entity);
            }
        }).start();
    }

    public LiveData<List<FoodItem>> searchFoodsFromApi(String query) {
        return repository.searchFoods(query);
    }
}