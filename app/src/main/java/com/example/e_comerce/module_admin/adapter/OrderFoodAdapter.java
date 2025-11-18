package com.example.e_comerce.module_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_food, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(getItem(position));
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView imgFood, imgSelected;
        TextView tvName, tvPrice;

        VH(View v) {
            super(v);
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
            imgFood.setImageResource(food.getImageResId());
            imgSelected.setVisibility(selectedFoods.contains(food) ? View.VISIBLE : View.GONE);
        }
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }

    private static final DiffUtil.ItemCallback<FoodItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodItem>() {
        @Override public boolean areItemsTheSame(FoodItem o, FoodItem n) { return o.getId().equals(n.getId()); }
        @Override public boolean areContentsTheSame(FoodItem o, FoodItem n) { return o.equals(n); }
    };
}