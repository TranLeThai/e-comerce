// core/data/local/entity/FoodEntity.java
package com.example.e_comerce.core.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class FoodEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private double price;
    private int imageRes;  // DÙNG int, KHÔNG PHẢI String
    private String category;

    public FoodEntity(String id, String name, double price, int imageRes, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public String getCategory() { return category; }
}