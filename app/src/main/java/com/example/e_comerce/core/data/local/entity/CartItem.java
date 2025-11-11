// core/data/local/entity/CartItem.java
package com.example.e_comerce.core.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartItem {
    @PrimaryKey
    @NonNull
    private String foodId;

    private String name;
    private double price;
    private int quantity;
    private String image;

    // Constructor
    public CartItem(String foodId, String name, double price, int quantity, String image) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // Getters and Setters
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}