// FoodItem.java
package com.example.e_comerce.core.data.model;

import java.util.Objects;

public class FoodItem {
    private String id;
    private String name;
    private double price;
    private int imageResId;

    public FoodItem(String id, String name, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }

    // --- Setters (nếu cần update) ---
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

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