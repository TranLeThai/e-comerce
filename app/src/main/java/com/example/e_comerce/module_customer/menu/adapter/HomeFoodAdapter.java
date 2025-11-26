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

    // Interface để MainActivity lắng nghe sự kiện
    public interface OnFoodClickListener {
        void onAddToCartClick(FoodItem food); // Bấm nút cộng
        void onItemClick(FoodItem food);      // Bấm vào xem chi tiết
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
        // Lưu ý: Đảm bảo tên file layout xml của bạn đúng là 'item_food_home' hay 'item_food'
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        holder.tvName.setText(food.getName());
        holder.tvPrice.setText(String.format("%,.0f đ", food.getPrice()));

        // --- CẬP NHẬT: LOGIC HIỂN THỊ ẢNH (QUAN TRỌNG) ---
        // Vì 'food.getImage()' trả về String, nên ta cần kiểm tra xem nó là ID số hay đường dẫn URI
        try {
            // 1. Thử ép kiểu sang số nguyên (Giả sử là R.drawable.ic_pizza = 213123...)
            int resId = Integer.parseInt(food.getImage());
            holder.imgFood.setImageResource(resId);
        } catch (NumberFormatException e) {
            // 2. Nếu lỗi (không phải số), nghĩa là đường dẫn file ảnh (content://...)
            // Lúc này dùng setImageURI để hiển thị ảnh Admin chọn
            try {
                holder.imgFood.setImageURI(Uri.parse(food.getImage()));
            } catch (Exception ex) {
                // Nếu ảnh lỗi link, hiển thị ảnh mặc định
                holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // Sự kiện click
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