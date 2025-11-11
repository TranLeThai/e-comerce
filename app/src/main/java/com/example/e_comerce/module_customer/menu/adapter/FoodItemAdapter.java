// module_customer/menu/adapter/FoodItemAdapter.java
package com.example.e_comerce.module_customer.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {

    public FoodItemAdapter(@NonNull Context context, @NonNull List<FoodItem> objects) {
        super(context, R.layout.item_food, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }

        FoodItem item = getItem(position);

        ImageView imgFood = convertView.findViewById(R.id.imgFood);
        TextView tvName = convertView.findViewById(R.id.tvFoodName);
        TextView tvPrice = convertView.findViewById(R.id.tvFoodPrice);

        if (item != null) {
            imgFood.setImageResource(item.getImageRes());
            tvName.setText(item.getName());

            // Định dạng tiền Việt Nam
            NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvPrice.setText(fmt.format(item.getPrice()));
        }

        return convertView;
    }
}