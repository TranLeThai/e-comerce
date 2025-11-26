package com.example.e_comerce.module_customer.menu.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFoodAdapter extends RecyclerView.Adapter<HomeFoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;
    private OnFoodClickListener listener;

    public interface OnFoodClickListener {
        void onAddToCartClick(FoodItem food);
        void onItemClick(FoodItem food);
    }

    public HomeFoodAdapter(OnFoodClickListener listener) {
        this.foodList = new ArrayList<>();
        this.listener = listener;
    }

    public void setFoodList(List<FoodItem> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        holder.tvName.setText(food.getName());
        holder.tvPrice.setText(String.format("%,.0f đ", food.getPrice()));

        try {
            int resId = Integer.parseInt(food.getImage());
            holder.imgFood.setImageResource(resId);
        } catch (NumberFormatException e) {
            try {
                holder.imgFood.setImageURI(Uri.parse(food.getImage()));
            } catch (Exception ex) {
                holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        holder.btnAddCart.setOnClickListener(v -> listener.onAddToCartClick(food));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(food));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgFood, btnAddCart;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View (Đảm bảo ID khớp với file XML item_food_home.xml)
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
        }
    }
}