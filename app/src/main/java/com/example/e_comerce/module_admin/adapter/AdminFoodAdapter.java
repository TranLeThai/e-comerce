package com.example.e_comerce.module_admin.adapter;

import android.net.Uri;
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

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đảm bảo tên layout item_admin_food đúng với file xml của bạn
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
        TextView tvFoodName, tvCategory, tvPrice;
        ImageButton btnEdit, btnDelete; // Thêm nút Delete

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View (Đảm bảo ID khớp với file XML)
            imgFood = itemView.findViewById(R.id.img_food);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete); // Ánh xạ nút xóa

            // Sự kiện Edit
            if (btnEdit != null) {
                btnEdit.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null) {
                        listener.onEditClick(getItem(pos));
                    }
                });
            }

            // Sự kiện Delete
            if (btnDelete != null) {
                btnDelete.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null) {
                        listener.onDeleteClick(getItem(pos));
                    }
                });
            }
        }

        public void bind(FoodItem food) {
            tvFoodName.setText(food.getName());
            tvCategory.setText(food.getCategory());
            tvPrice.setText(formatCurrency(food.getPrice()));

            // --- SỬA LOGIC HIỂN THỊ ẢNH ---
            try {
                // 1. Thử ép kiểu sang int (Nếu là ảnh mẫu có sẵn R.drawable.xxx)
                int resId = Integer.parseInt(food.getImage());
                imgFood.setImageResource(resId);
            } catch (NumberFormatException e) {
                // 2. Nếu không phải số -> Là đường dẫn ảnh Admin chọn từ thư viện
                try {
                    imgFood.setImageURI(Uri.parse(food.getImage()));
                } catch (Exception ex) {
                    // Fallback nếu ảnh lỗi
                    imgFood.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private static final DiffUtil.ItemCallback<FoodItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            // Kiểm tra null cho ID (phòng trường hợp ID chưa có)
            if (oldItem.getId() == null || newItem.getId() == null) return false;
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodItem oldItem, @NonNull FoodItem newItem) {
            // Sử dụng Objects.equals để so sánh an toàn (tránh NullPointerException)
            // Thay vì old.getName().equals(new.getName())

            return java.util.Objects.equals(oldItem.getName(), newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    java.util.Objects.equals(oldItem.getImage(), newItem.getImage()) &&
                    java.util.Objects.equals(oldItem.getCategory(), newItem.getCategory());
        }
    };
}