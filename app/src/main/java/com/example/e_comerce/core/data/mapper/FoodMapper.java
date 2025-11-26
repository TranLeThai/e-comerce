package com.example.e_comerce.core.data.mapper;

import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodMapper {


    public static FoodEntity toEntity(FoodItem model) {
        if (model == null) return null;
        return new FoodEntity(
                model.getId(),
                model.getName(),
                model.getPrice(),
                model.getImageResId(),
                model.getCategory()
        );
    }

    public static List<FoodEntity> toEntityList(List<FoodItem> models) {
        List<FoodEntity> list = new ArrayList<>();
        if (models != null) {
            for (FoodItem item : models) {
                list.add(toEntity(item));
            }
        }
        return list;
    }

    public static FoodItem toModel(FoodEntity entity) {
        if (entity == null) return null;
        return new FoodItem(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageResId(),
                entity.getCategory()
        );
    }
}