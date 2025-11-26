package com.example.e_comerce.core.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String orderDate;
    public double totalAmount;
    public String status;
    public String itemsSummary;

    // --- CÁC CỘT MỚI THÊM ---
    public String paymentMethod;
    public String note;
    public double discount;
    public String deliveryMethod;

    public OrderEntity() { }

    // Cập nhật Constructor
    public OrderEntity(String orderDate, double totalAmount, String status, String itemsSummary,
                       String paymentMethod, String note, double discount, String deliveryMethod) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.itemsSummary = itemsSummary;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.discount = discount;
        this.deliveryMethod = deliveryMethod;
    }
}