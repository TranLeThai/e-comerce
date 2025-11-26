package com.example.e_comerce.module_admin.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.entity.OrderEntity;
import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(OrderEntity order);
    }
    private List<OrderEntity> orderList;
    private final OnOrderClickListener listener; // Biến listener

    public AdminOrderAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void setOrderList(List<OrderEntity> list) {
        this.orderList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity order = orderList.get(position);

        holder.tvOrderId.setText("Đơn hàng #" + order.id);
        holder.tvOrderDate.setText(order.orderDate);
        holder.tvOrderSummary.setText(order.itemsSummary);
        holder.tvOrderTotal.setText(String.format("%,.0f ₫", order.totalAmount));

        // Xử lý màu sắc trạng thái
        holder.tvStatus.setText(order.status);
        if (order.status.equals("Đang chờ")) {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Màu cam
        } else if (order.status.equals("Hoàn thành")) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh
        } else {
            holder.tvStatus.setTextColor(Color.BLACK);
        }
        holder.itemView.setOnClickListener(v -> {
            listener.onOrderClick(order);
        });
    }

    @Override
    public int getItemCount() {
        return (orderList != null) ? orderList.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderSummary, tvOrderTotal, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderSummary = itemView.findViewById(R.id.tvOrderSummary);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}