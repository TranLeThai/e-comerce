package com.example.e_comerce.module_customer.menu.adapter;

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

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.OrderViewHolder> {

    private List<OrderEntity> orderList;

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

        holder.tvOrderId.setText("Đơn #" + order.id);
        holder.tvOrderDate.setText(order.orderDate);
        holder.tvOrderSummary.setText(order.itemsSummary);
        holder.tvOrderTotal.setText(String.format("%,.0f ₫", order.totalAmount));

        holder.tvStatus.setText(order.status);
        if (order.status.equals("Đang chờ")) {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Cam
        } else if (order.status.equals("Đang giao")) {
            holder.tvStatus.setTextColor(Color.BLUE);
        } else if (order.status.equals("Hoàn thành")) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh
        } else {
            holder.tvStatus.setTextColor(Color.BLACK);
        }

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