// module_admin/fragment/RevenueFragment.java
package com.example.e_comerce.module_admin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_comerce.databinding.FragmentRevenueBinding;
import com.example.e_comerce.module_admin.viewmodel.RevenueViewModel;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RevenueFragment extends Fragment {

    private FragmentRevenueBinding binding;
    private RevenueViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRevenueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RevenueViewModel.class);

        setupPeriodSpinner();
        observeData();
    }

    private void setupPeriodSpinner() {
        String[] periods = {"Hôm nay", "Tuần này", "Tháng này", "Năm nay"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                periods
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPeriod.setAdapter(adapter);

        binding.spinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.loadRevenueByPeriod(periods[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void observeData() {
        // Observe total revenue
        viewModel.getTotalRevenue().observe(getViewLifecycleOwner(), revenue -> {
            binding.tvTotalRevenue.setText(formatCurrency(revenue));
        });

        // Observe total orders
        viewModel.getTotalOrders().observe(getViewLifecycleOwner(), count -> {
            binding.tvTotalOrders.setText(String.valueOf(count));
        });

        // Observe completed orders
        viewModel.getCompletedOrders().observe(getViewLifecycleOwner(), count -> {
            binding.tvCompletedOrders.setText(String.valueOf(count));
        });

        // Observe cancelled orders
        viewModel.getCancelledOrders().observe(getViewLifecycleOwner(), count -> {
            binding.tvCancelledOrders.setText(String.valueOf(count));
        });

        // Observe category revenue (for chart)
        viewModel.getCategoryRevenue().observe(getViewLifecycleOwner(), categoryMap -> {
            setupPieChart(categoryMap);
        });
    }

    private void setupPieChart(java.util.Map<String, Double> categoryRevenue) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (java.util.Map.Entry<String, Double> entry : categoryRevenue.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Doanh thu theo danh mục");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setEntryLabelTextSize(12f);
        binding.pieChart.invalidate();
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}