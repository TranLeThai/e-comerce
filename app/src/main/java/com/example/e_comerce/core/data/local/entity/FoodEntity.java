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
    private int imageResId;  // DÙNG int, KHÔNG PHẢI String
    private String category;

    // The constructor parameter 'imageResId' now matches the field name.
    public FoodEntity(String id, String name, double price, int imageResId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId; // Corrected the assignment here as well.
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category != null ? category : "Other"; }
}
