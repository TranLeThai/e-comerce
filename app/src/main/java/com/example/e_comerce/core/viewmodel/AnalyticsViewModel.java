package com.example.e_comerce.core.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.example.e_comerce.core.data.local.database.AppDatabase;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper;
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyticsViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final LiveData<List<FoodEntity>> allFoodEntities;
    private final LiveData<List<OrderEntity>> completedOrders;
    private final MediatorLiveData<List<FoodItem>> topSellingFoods = new MediatorLiveData<>();
    static class FoodSaleResult {
        public FoodItem food;
        public int totalSold;

        public FoodSaleResult(FoodItem food, int totalSold) {
            this.food = food;
            this.totalSold = totalSold;
        }
    }

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);

        allFoodEntities = db.foodDao().getAllFoods();
        completedOrders = db.orderDao().getCompletedOrdersForAnalysis();

        topSellingFoods.addSource(allFoodEntities, foods -> updateTopSellers());
        topSellingFoods.addSource(completedOrders, orders -> updateTopSellers());
    }

    private void updateTopSellers() {
        List<FoodEntity> fullFoodList = allFoodEntities.getValue();
        List<OrderEntity> orders = completedOrders.getValue();

        if (fullFoodList == null || orders == null || orders.isEmpty()) {
            topSellingFoods.postValue(new ArrayList<>());
            return;
        }

        executor.execute(() -> {
            Map<String, Integer> itemQuantityMap = analyzeOrderSummaries(orders);

            List<FoodSaleResult> results = new ArrayList<>();
            for (FoodEntity entity : fullFoodList) {
                String dbFoodName = entity.getName();
                int totalSold = calculateTotalSold(dbFoodName, itemQuantityMap);

                if (totalSold > 0) {
                    FoodItem model = FoodMapper.toModel(entity);
                    results.add(new FoodSaleResult(model, totalSold));
                }
            }

            Collections.sort(results, (a, b) -> Integer.compare(b.totalSold, a.totalSold));

            List<FoodItem> finalRankedList = new ArrayList<>();
            int limit = Math.min(results.size(), 5);
            for(int i = 0; i < limit; i++) {
                finalRankedList.add(results.get(i).food);
            }

            topSellingFoods.postValue(finalRankedList);
        });
    }

    private Map<String, Integer> analyzeOrderSummaries(List<OrderEntity> orders) {
        Map<String, Integer> itemQuantityMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(.+?)\\(x(\\d+)\\)");

        for (OrderEntity order : orders) {
            if (order.itemsSummary == null) continue;

            String[] items = order.itemsSummary.split(", ");

            for (String itemEntry : items) {
                Matcher matcher = pattern.matcher(itemEntry);
                if (matcher.find()) {
                    String name = matcher.group(1).trim(); // Tên món (kèm size)

                    try {
                        int quantity = Integer.parseInt(matcher.group(2).trim()); // Số lượng

                        itemQuantityMap.put(name, itemQuantityMap.getOrDefault(name, 0) + quantity);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return itemQuantityMap;
    }

    private int calculateTotalSold(String dbFoodName, Map<String, Integer> itemQuantityMap) {
        int totalSold = 0;
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            if (entry.getKey().contains(dbFoodName)) {
                totalSold += entry.getValue();
            }
        }
        return totalSold;
    }

    public LiveData<List<FoodItem>> getTopSellingFoodsLiveData() {
        return topSellingFoods;
    }
}