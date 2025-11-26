package com.example.e_comerce.module_admin.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderFoodAdapter extends ListAdapter<FoodItem, OrderFoodAdapter.VH> {

    private final List<FoodItem> selectedFoods;
    private OnFoodSelectListener listener;

    public OrderFoodAdapter(List<FoodItem> selectedFoods) {
        super(DIFF_CALLBACK);
        this.selectedFoods = selectedFoods;
    }

    public void setOnFoodSelectListener(OnFoodSelectListener l) {
        this.listener = l;
    }

    public interface OnFoodSelectListener {
        void onFoodSelected(FoodItem food);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_food, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position));
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView imgFood, imgSelected;
        TextView tvName, tvPrice;

        VH(View v) {
            super(v);
            // Đảm bảo các ID này trùng khớp với file XML item_order_food.xml
            imgFood = v.findViewById(R.id.img_food);
            imgSelected = v.findViewById(R.id.img_selected);
            tvName = v.findViewById(R.id.tv_food_name);
            tvPrice = v.findViewById(R.id.tv_price);

            v.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFoodSelected(getItem(pos));
                }
            });
        }

        void bind(FoodItem food) {
            tvName.setText(food.getName());
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

            // Kiểm tra xem món này có đang được chọn không
            // Lưu ý: FoodItem cần override equals() hoặc so sánh ID thủ công để contains hoạt động đúng
            boolean isSelected = false;
            for (FoodItem item : selectedFoods) {
                if (item.getId().equals(food.getId())) {
                    isSelected = true;
                    break;
                }
            }
            imgSelected.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        }
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }

    private static final DiffUtil.ItemCallback<FoodItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodItem o, @NonNull FoodItem n) {
            return o.getId().equals(n.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodItem o, @NonNull FoodItem n) {
            // So sánh nội dung để biết có cần vẽ lại không
            return o.getName().equals(n.getName()) &&
                    o.getPrice() == n.getPrice() &&
                    o.getImage().equals(n.getImage());
        }
    };
}