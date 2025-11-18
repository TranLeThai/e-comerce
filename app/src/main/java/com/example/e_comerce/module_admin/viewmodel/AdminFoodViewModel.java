// module_admin/viewmodel/AdminFoodViewModel.java
package com.example.e_comerce.module_admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_comerce.core.data.model.FoodItem;
import java.util.ArrayList;
import com.example.e_comerce.R;
import java.util.List;

public class AdminFoodViewModel extends ViewModel {
    private final MutableLiveData<List<FoodItem>> foodList = new MutableLiveData<>();

    public AdminFoodViewModel() {
        loadFoods();
    }

    public LiveData<List<FoodItem>> getAllFoods() {
        return foodList;
    }

    // === THÊM METHOD NÀY ===
    public LiveData<FoodItem> getFoodById(String id) {
        MutableLiveData<FoodItem> result = new MutableLiveData<>();
        List<FoodItem> currentList = foodList.getValue();
        if (currentList != null) {
            for (FoodItem food : currentList) {
                if (food.getId().equals(id)) {
                    result.setValue(food);
                    return result;
                }
            }
        }
        result.setValue(null); // Không tìm thấy
        return result;
    }
    // === KẾT THÚC THÊM ===

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
                break; // <-- BỎ break ở đây là SAI! (đã sửa)
            }
        }
        foodList.setValue(curr);
    }

    public void deleteFood(String foodId) {
        List<FoodItem> current = new ArrayList<>(foodList.getValue());
        current.removeIf(food -> food.getId().equals(foodId));
        foodList.setValue(current);
    }

    private void loadFoods() {
        List<FoodItem> dummy = new ArrayList<>();
        dummy.add(new FoodItem("1", "Phở bò", 55000, R.drawable.burger, "Món Việt"));
        dummy.add(new FoodItem("2", "Bún chả", 45000, R.drawable.com_ga, "Món Việt"));
        dummy.add(new FoodItem("3", "Pizza Margherita", 120000, R.drawable.burger, "Pizza"));
        dummy.add(new FoodItem("4", "Burger Bò", 85000, R.drawable.burger, "Burger"));
        dummy.add(new FoodItem("5", "Trà sữa", 35000, R.drawable.burger, "Đồ uống"));
        foodList.setValue(dummy);
    }
}