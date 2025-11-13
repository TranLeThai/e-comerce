// module_admin/adapter/AdminFoodAdapter.java
package com.example.e_comerce.module_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;

public class AdminFoodAdapter extends ListAdapter<FoodItem, AdminFoodAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public AdminFoodAdapter() {
        super(DIFF_CALLBACK);
    }

    public interface OnItemClickListener {
        void onEditClick(FoodItem food);
        void onDeleteClick(FoodItem food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem food = getItem(position);
        holder.bind(food);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.img_food);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvPrice = itemView.findViewById(R.id.tv_food_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(FoodItem food) {
            tvName.setText(food.getName());
            tvPrice.setText(String.format("%,.0f đ", food.getPrice()));
            imgFood.setImageResource(food.getImageResId());

            btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(food);
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(food);
            });
        }
    }

    private static final DiffUtil.ItemCallback<FoodItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}