package com.example.e_comerce.product;

public class MonAn {
    private String ten;
    private String gia;
    private int hinh;

    public MonAn(String ten, String gia, int hinh) {
        this.ten = ten;
        this.gia = gia;
        this.hinh = hinh;
    }

    public String getTen() { return ten; }
    public String getGia() { return gia; }
    public int getHinh() { return hinh; }
}
