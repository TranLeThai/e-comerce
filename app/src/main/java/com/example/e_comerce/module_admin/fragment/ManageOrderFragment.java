// module_admin/fragment/ManageOrderFragment.java (FULL IMPLEMENTATION)
package com.example.e_comerce.module_admin.fragment;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.e_comerce.databinding.FragmentManageOrderBinding;
import com.example.e_comerce.module_admin.activity.OrderDetailActivity;
import com.example.e_comerce.module_admin.adapter.AdminOrderAdapter;
import com.example.e_comerce.module_admin.viewmodel.AdminOrderViewModel;

public class ManageOrderFragment extends Fragment {

    private FragmentManageOrderBinding binding;
    private AdminOrderAdapter adapter;
    private AdminOrderViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManageOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AdminOrderViewModel.class);

        setupFilterSpinner();
        setupRecyclerView();
        observeOrders();
    }

    private void setupFilterSpinner() {
        String[] statuses = {"Tất cả", "Chờ xác nhận", "Đang giao", "Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statuses
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilter.setAdapter(adapter);

        binding.spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = statuses[position];
                if (status.equals("Tất cả")) {
                    viewModel.loadAllOrders();
                } else {
                    viewModel.filterOrdersByStatus(status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new AdminOrderAdapter();
        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerOrders.setAdapter(adapter);

        adapter.setOnItemClickListener(order -> {
            Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
            intent.putExtra("order_id", order.getId());
            startActivity(intent);
        });
    }

    private void observeOrders() {
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            adapter.submitList(orders);

            // Show/hide empty view
            if (orders.isEmpty()) {
                binding.recyclerOrders.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerOrders.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}