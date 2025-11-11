// module_admin/viewmodel/AdminFoodViewModel.java
package com.example.e_comerce.module_admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_comerce.core.data.model.FoodItem;
import java.util.ArrayList;
import java.util.List;

public class AdminFoodViewModel extends ViewModel {

    private final MutableLiveData<List<FoodItem>> foodList = new MutableLiveData<>();

    public AdminFoodViewModel() {
        loadFoods();
    }

    public LiveData<List<FoodItem>> getAllFoods() {
        return foodList;
    }

    public void addFood(FoodItem food) {
        List<FoodItem> curr = new ArrayList<>(foodList.getValue());
        curr.add(food);
        foodList.setValue(curr);
    }

    public void updateFood(FoodItem updatedFood) {
        List<FoodItem> curr = new ArrayList<>(foodList.getValue());
        for (int i = 0; i < curr.size(); i++) {
            if (curr.get(i).getId().equals(updatedFood.getId())) {
                curr.set(i, updatedFood);
            }
            break;
        }
        foodList.setValue(curr);
    }

    public void deleteFood(String foodId) {
        List<FoodItem> current = new ArrayList<>(foodList.getValue());
        current.removeIf(food -> food.getId().equals(foodId));
        foodList.setValue(current);
    }

    private void loadFoods() {
        // TODO: Gọi API hoặc Room
        List<FoodItem> dummy = new ArrayList<>();
        dummy.add(new FoodItem("1", "Phở bò", 55000, "https://example.com/pho.jpg"));
        dummy.add(new FoodItem("2", "Bún chả", 45000, "https://example.com/bun.jpg"));
        foodList.setValue(dummy);
    }
}