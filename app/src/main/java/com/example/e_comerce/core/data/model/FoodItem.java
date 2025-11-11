// core/data/model/FoodItem.java
package com.example.e_comerce.core.data.model;

public class FoodItem {
    private String id;
    private String name;
    private double price;
    private int imageRes;
    private String category;

    public FoodItem(String id, String name, double price, int imageRes, String category) {
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