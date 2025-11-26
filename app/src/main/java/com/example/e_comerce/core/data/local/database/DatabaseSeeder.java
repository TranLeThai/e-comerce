package com.example.e_comerce.core.data.local.database;

import android.content.Context;
import android.util.Log;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DatabaseSeeder {

    public static void seed(Context context, AppDatabase db) {
        new Thread(() -> {
            if (db.foodDao().getFoodCount() == 0) {
                try {
                    String jsonFileString = getJsonFromAssets(context, "food_data.json");
                    Gson gson = new Gson();
                    Type listUserType = new TypeToken<List<FoodEntity>>() { }.getType();
                    List<FoodEntity> foods = gson.fromJson(jsonFileString, listUserType);

                    if (foods != null) {
                        for (FoodEntity food : foods) {
                            String imgName = food.getImage();
                            int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());

                            if (resId != 0) {
                                food.setImage(String.valueOf(resId));
                            } else {
                                food.setImage(String.valueOf(R.drawable.ic_menu_camera));
                            }
                        }
                        db.foodDao().insertFoods(foods);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String getJsonFromAssets(Context context, String fileName) {
        // (Giữ nguyên hàm này như cũ)
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}