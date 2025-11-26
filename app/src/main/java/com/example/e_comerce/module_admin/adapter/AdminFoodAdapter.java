package com.example.e_comerce.module_admin.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;
import java.text.NumberFormat;
import java.util.Locale;

public class AdminFoodAdapter extends ListAdapter<FoodItem, AdminFoodAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(FoodItem food);
        void onDeleteClick(FoodItem food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminFoodAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem food = getItem(position);
        holder.bind(food);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvPrice, tvCategory;
        ImageButton btnOptions; // Nút 3 chấm

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.img_food);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCategory = itemView.findViewById(R.id.tv_category);
            btnOptions = itemView.findViewById(R.id.btn_edit);

            btnOptions.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    showPopupMenu(v, getItem(pos));
                }
            });
        }

        private void showPopupMenu(View view, FoodItem food) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            // Thêm 2 menu item bằng code (hoặc dùng menu xml cũng được)
            popup.getMenu().add(0, 1, 0, "Sửa món ăn");
            popup.getMenu().add(0, 2, 1, "Xóa món ăn");

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == 1) {
                        listener.onEditClick(food);
                        return true;
                    } else if (item.getItemId() == 2) {
                        listener.onDeleteClick(food);
                        return true;
                    }
                    return false;
                }
            });
            popup.show();
        }

        public void bind(FoodItem food) {
            tvFoodName.setText(food.getName());
            tvPrice.setText(formatCurrency(food.getPrice()));
            tvCategory.setText(food.getCategory());

            try {
                int resId = Integer.parseInt(food.getImage());
                imgFood.setImageResource(resId);
            } catch (NumberFormatException e) {
                try {
                    imgFood.setImageURI(Uri.parse(food.getImage()));
                } catch (Exception ex) {
                    imgFood.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }

    private static final DiffUtil.ItemCallback<FoodItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            if (oldItem.getId() == null || newItem.getId() == null) return false;
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            return java.util.Objects.equals(oldItem.getName(), newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    java.util.Objects.equals(oldItem.getImage(), newItem.getImage());
        }
    };
}