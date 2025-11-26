package com.example.e_comerce.core.data.mapper;

import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodMapper {

    public static FoodItem toModel(FoodEntity entity) {
        if (entity == null) return null;

        return new FoodItem(
                String.valueOf(entity.getId()),
                entity.getName(),
                entity.getPrice(),
                entity.getImage(),
                entity.getCategory()
        );
    }

    public static FoodEntity toEntity(FoodItem model) {
        if (model == null) return null;
        return new FoodEntity(
                model.getName(),
                model.getPrice(),
                model.getImage(),
                model.getCategory()
        );
    }
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