package com.example.e_comerce.core.data.mapper;

import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;
import java.util.ArrayList;
import java.util.List;

public class FoodMapper {

    // Chuyển từ Database (Entity) -> App (Model)
    public static FoodItem toModel(FoodEntity entity) {
        if (entity == null) return null;
        return new FoodItem(
                String.valueOf(entity.getId()),
                entity.getName(),
                entity.getPrice(),
                entity.getImage() // Bây giờ entity.getImage() đã tồn tại
        );
    }

    // Chuyển từ App (Model) -> Database (Entity)
    public static FoodEntity toEntity(FoodItem model) {
        if (model == null) return null;
        return new FoodEntity(
                model.getName(),
                model.getPrice(),
                model.getImage(), // Lưu ý: Model dùng String, Entity cũng dùng String
                model.getCategory()
        );
    }

    // --- HÀM BẠN ĐANG THIẾU ĐỂ SỬA LỖI HÌNH 1 ---
    public static List<FoodEntity> toEntityList(List<FoodItem> models) {
        List<FoodEntity> entities = new ArrayList<>();
        if (models != null) {
            for (FoodItem item : models) {
                entities.add(toEntity(item));
            }
        }
        return entities;
    }
}