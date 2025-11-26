package com.example.e_comerce.core.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class FoodEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double price;
    private String image;
    private String category;

    public FoodEntity() { }

    // Constructor đầy đủ
    public FoodEntity(String name, double price, String image, String category) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Hàm này sẽ sửa lỗi đỏ "Cannot resolve method getImage"
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}