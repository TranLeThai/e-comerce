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
    private int imageResId;
    private String category;


    public FoodEntity(String id, String name, double price, int imageResId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category != null ? category : "Other"; }
}
