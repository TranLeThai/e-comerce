// core/data/mapper/FoodMapper.java
package com.example.e_comerce.core.data.mapper;

import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.model.FoodItem;
import java.util.ArrayList;
import java.util.List;

public class FoodMapper {

    public static FoodEntity toEntity(FoodItem model) {
        return new FoodEntity(
                model.getId(),
                model.getName(),
                model.getPrice(),
                model.getImageRes(),  // SỬA: getImageRes()
                model.getCategory()
        );
    }

    public static List<FoodEntity> toEntityList(List<FoodItem> models) {
        List<FoodEntity> list = new ArrayList<>();
        for (FoodItem m : models) list.add(toEntity(m));
        return list;
    }

    public static FoodItem toModel(FoodEntity entity) {
        return new FoodItem(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageRes(),  // SỬA: getImageRes()
                entity.getCategory()
        );
    }

    public static List<FoodItem> toModelList(List<FoodEntity> entities) {
        List<FoodItem> list = new ArrayList<>();
        for (FoodEntity e : entities) list.add(toModel(e));
        return list;
    }
}