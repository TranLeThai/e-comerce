package com.example.e_comerce.core.remote;

import com.example.e_comerce.core.data.model.FoodItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private static final String COLLECTION_FOODS = "foods";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface Callback {
        void onSuccess(List<FoodItem> foodList);
        void onFailure(String errorMessage);
    }

    // Lấy tất cả món ăn từ Firestore
    public void getAllFoods(Callback callback) {
        db.collection(COLLECTION_FOODS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FoodItem> list = new ArrayList<>();
                    for (var document : queryDocumentSnapshots) {
                        FoodItem food = document.toObject(FoodItem.class);
                        food.setId(document.getId()); // rất quan trọng để có ID
                        list.add(food);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Tìm theo category
    public void getFoodsByCategory(String category, Callback callback) {
        db.collection(COLLECTION_FOODS)
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FoodItem> list = new ArrayList<>();
                    for (var doc : queryDocumentSnapshots) {
                        FoodItem food = doc.toObject(FoodItem.class);
                        food.setId(doc.getId());
                        list.add(food);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}