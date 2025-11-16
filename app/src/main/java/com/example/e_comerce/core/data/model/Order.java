// core/data/model/Order.java
package com.example.e_comerce.core.data.model;

public class Order {
    private String id;
    private String customerName;
    private double totalPrice;
    private String status; // "Chờ xác nhận", "Đang giao", "Hoàn thành", "Đã hủy"
    private String date;

    public Order(String id, String customerName, double totalPrice, String status, String date) {
        this.id = id;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.date = date;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getDate() { return date; }

    // Setters
    public void setStatus(String status) { this.status = status; }
}