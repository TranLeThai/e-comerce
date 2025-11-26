package com.example.e_comerce.core.data.model;

import com.example.e_comerce.core.data.local.entity.FoodEntity;

public class FoodItem {
    private String id;
    private String name;
    private double price;
    private int imageResId;
    private String category;

    public FoodItem(String id, String name, double price, int imageResId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
    }

    // --- SỬA ĐÚNG: Trả về String ---
    public String getId() { return id; }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    // (Giữ nguyên equals và hashCode...)
}