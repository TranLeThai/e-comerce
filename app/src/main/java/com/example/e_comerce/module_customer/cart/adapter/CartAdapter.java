package com.example.e_comerce.module_customer.cart.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton; // Thêm ImageButton
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.entity.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnItemInteractionListener listener;

    public interface OnItemInteractionListener {
        void onRemoveClick(CartItem item);
        void onQuantityChange(CartItem item, int newQuantity);
    }

    public CartAdapter(List<CartItem> cartItems, OnItemInteractionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout: item_cart.xml (Sử dụng layout đã sửa)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Tính tổng giá
        double subTotal = item.getPrice() * item.getQuantity();

        holder.textName.setText(item.getName());
        holder.textPrice.setText(formatCurrency(subTotal));
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        // 1. Logic Hiển thị ảnh (QUAN TRỌNG)
        try {
            int resId = Integer.parseInt(item.getImage());
            holder.imgFood.setImageResource(resId);
        } catch (NumberFormatException e) {
            try {
                holder.imgFood.setImageURI(Uri.parse(item.getImage()));
            } catch (Exception ex) {
                holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // 2. Sự kiện Xóa món
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(item));

        // 3. Sự kiện Tăng/Giảm số lượng
        holder.btnPlus.setOnClickListener(v -> {
            listener.onQuantityChange(item, item.getQuantity() + 1);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                listener.onQuantityChange(item, item.getQuantity() - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        ImageButton btnRemove, btnPlus, btnMinus; // Đổi btnRemove thành ImageButton (nếu cần)
        TextView textName, textPrice, textQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View mới
            imgFood = itemView.findViewById(R.id.img_cart_food);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);

            // Bộ đếm số lượng (textQuantity là TextView ở giữa)
            textQuantity = itemView.findViewById(R.id.textQuantity);

            // Nút chức năng
            btnRemove = itemView.findViewById(R.id.btnRemove); // Giữ nguyên ID, đổi kiểu sang ImageButton/ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}