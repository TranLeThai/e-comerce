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

    public OrderEntity() { }

    public OrderEntity(String orderDate, double totalAmount, String status, String itemsSummary) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.itemsSummary = itemsSummary;
    }
}