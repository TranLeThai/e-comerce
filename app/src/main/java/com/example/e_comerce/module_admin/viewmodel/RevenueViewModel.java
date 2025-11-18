// module_admin/viewmodel/RevenueViewModel.java
package com.example.e_comerce.module_admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class RevenueViewModel extends ViewModel {

    // Các LiveData để fragment observe
    private final MutableLiveData<Double> totalRevenue = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalOrders = new MutableLiveData<>();
    private final MutableLiveData<Integer> completedOrders = new MutableLiveData<>();
    private final MutableLiveData<Integer> cancelledOrders = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Double>> categoryRevenue = new MutableLiveData<>();

    // Constructor (có thể để trống hoặc khởi tạo dữ liệu mẫu)
    public RevenueViewModel() {
        // Khi mới khởi tạo, bạn có thể load dữ liệu mặc định (ví dụ: "Hôm nay")
        loadRevenueByPeriod("Hôm nay");
    }

    // ============================== GETTER ==============================
    public LiveData<Double> getTotalRevenue() {
        return totalRevenue;
    }

    public LiveData<Integer> getTotalOrders() {
        return totalOrders;
    }

    public LiveData<Integer> getCompletedOrders() {
        return completedOrders;
    }

    public LiveData<Integer> getCancelledOrders() {
        return cancelledOrders;
    }

    public LiveData<Map<String, Double>> getCategoryRevenue() {
        return categoryRevenue;
    }

    // ============================== LOAD DATA ==============================
    public void loadRevenueByPeriod(String period) {
        // Ở đây bạn sẽ gọi Repository / API thực tế
        // Hiện tại mình để dữ liệu mẫu để chart và các TextView hiển thị được luôn

        switch (period) {
            case "Hôm nay":
                totalRevenue.setValue(158_900_000.0);
                totalOrders.setValue(87);
                completedOrders.setValue(72);
                cancelledOrders.setValue(15);
                setSampleCategoryRevenue(new double[]{45_000_000, 38_500_000, 32_400_000, 25_000_000, 18_000_000},
                        new String[]{"Điện thoại", "Laptop", "Tai nghe", "Đồng hồ", "Phụ kiện"});
                break;

            case "Tuần này":
                totalRevenue.setValue(1_245_600_000.0);
                totalOrders.setValue(623);
                completedOrders.setValue(589);
                cancelledOrders.setValue(34);
                setSampleCategoryRevenue(new double[]{380_000_000, 320_500_000, 245_100_000, 180_000_000, 120_000_000},
                        new String[]{"Điện thoại", "Laptop", "Tai nghe", "Đồng hồ", "Phụ kiện"});
                break;

            case "Tháng này":
                totalRevenue.setValue(5_890_400_000.0);
                totalOrders.setValue(2847);
                completedOrders.setValue(2701);
                cancelledOrders.setValue(146);
                setSampleCategoryRevenue(new double[]{1_850_000_000, 1_620_000_000, 980_000_000, 780_000_000, 660_400_000},
                        new String[]{"Điện thoại", "Laptop", "Tai nghe", "Đồng hồ", "Phụ kiện"});
                break;

            case "Năm nay":
                totalRevenue.setValue(68_450_000_000.0);
                totalOrders.setValue(31245);
                completedOrders.setValue(29987);
                cancelledOrders.setValue(1258);
                setSampleCategoryRevenue(new double[]{22_500_000_000.0, 18_400_000_000.0, 12_300_000_000.0, 9_250_000_000.0, 6_000_000_000.0},
                        new String[]{"Điện thoại", "Laptop", "Tai nghe", "Đồng hồ", "Phụ kiện"});
                break;

            default:
                // Giá trị mặc định
                totalRevenue.setValue(0.0);
                totalOrders.setValue(0);
                completedOrders.setValue(0);
                cancelledOrders.setValue(0);
                categoryRevenue.setValue(new HashMap<>());
                break;
        }
    }

    // Helper để tạo dữ liệu mẫu cho biểu đồ tròn
    private void setSampleCategoryRevenue(double[] values, String[] labels) {
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < labels.length; i++) {
            map.put(labels[i], values[i]);
        }
        categoryRevenue.setValue(map);
    }
}