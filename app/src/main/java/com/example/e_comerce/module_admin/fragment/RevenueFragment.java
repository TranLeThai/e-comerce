package com.example.e_comerce.module_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_comerce.R;
import com.example.e_comerce.module_admin.viewmodel.RevenueViewModel;

public class RevenueFragment extends Fragment {

    private TextView tvTotalRevenue, tvOrderCount;
    private RevenueViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revenue, container, false); // Nhớ đổi tên XML cho khớp

        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        tvOrderCount = view.findViewById(R.id.tvOrderCount);

        viewModel = new ViewModelProvider(this).get(RevenueViewModel.class);

        // 1. Quan sát Tổng tiền
        viewModel.getTotalRevenue().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                tvTotalRevenue.setText(String.format("%,.0f ₫", total));
            } else {
                tvTotalRevenue.setText("0 ₫");
            }
        });

        // 2. Quan sát Số lượng đơn
        viewModel.getOrderCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvOrderCount.setText(count + " đơn");
            } else {
                tvOrderCount.setText("0 đơn");
            }
        });

        return view;
    }
}