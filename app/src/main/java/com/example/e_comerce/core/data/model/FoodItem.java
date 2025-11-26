package com.example.e_comerce.core.data.model;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String id;
    private String name;
    private double price;
    private String image;
    private String category;

    public FoodItem(String id, String name, double price, String image) { // Sửa constructor
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    // Sửa Getter
    public String getImage() { return image; }

    public String getCategory() { return category; }
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}