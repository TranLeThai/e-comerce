// core/data/model/FoodItem.java
package com.example.e_comerce.core.data.model;

import java.util.Objects;

public class FoodItem {
    private String id;
    private String name;
    private double price;
    private int imageResId;
    private String category; // MỚI

    public FoodItem(String id, String name, double price, int imageResId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
    }

    // GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; } // MỚI

    // SETTERS (nếu cần)
    public void setCategory(String category) { this.category = category; }

    // === OVERRIDE equals() ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Double.compare(foodItem.price, price) == 0 &&
                imageResId == foodItem.imageResId &&
                Objects.equals(id, foodItem.id) &&
                Objects.equals(name, foodItem.name);
    }

    // === OVERRIDE hashCode() ===
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageResId);
    }
}