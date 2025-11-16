// module_admin/adapter/AdminOrderAdapter.java
package com.example.e_comerce.module_admin.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.Order;
import java.text.NumberFormat;
import java.util.Locale;

public class AdminOrderAdapter extends ListAdapter<Order, AdminOrderAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdminOrderAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = getItem(position);
        holder.bind(order);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvTotalPrice, tvStatus, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Order order) {
            tvOrderId.setText("#" + order.getId());
            tvCustomerName.setText(order.getCustomerName());
            tvTotalPrice.setText(formatCurrency(order.getTotalPrice()));
            tvStatus.setText(order.getStatus());
            tvDate.setText(order.getDate());

            // Set status color
            switch (order.getStatus()) {
                case "Chờ xác nhận":
                    tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
                    break;
                case "Đang giao":
                    tvStatus.setTextColor(Color.parseColor("#2196F3")); // Blue
                    break;
                case "Hoàn thành":
                    tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                    break;
                case "Đã hủy":
                    tvStatus.setTextColor(Color.parseColor("#F44336")); // Red
                    break;
            }
        }

        private String formatCurrency(double amount) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return formatter.format(amount);
        }
    }

    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getStatus().equals(newItem.getStatus()) &&
                    oldItem.getTotalPrice() == newItem.getTotalPrice();
        }
    };
}