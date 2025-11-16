// module_customer/cart/adapter/CartAdapter.java

package com.example.e_comerce.module_customer.cart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.entity.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRemoveClick(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItems, OnItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.textName.setText(item.getName());
        holder.textPrice.setText(String.format("%,.0f ₫", item.getPrice()));
        holder.textQuantity.setText("Số lượng: " + item.getQuantity());
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPrice, textQuantity;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}