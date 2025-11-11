// module_admin/fragment/ManageFoodFragment.java
package com.example.fooddelivery.module_admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fooddelivery.core.data.model.FoodItem;
import com.example.fooddelivery.databinding.FragmentManageFoodBinding;
import com.example.fooddelivery.module_admin.activity.AddEditFoodActivity;
import com.example.fooddelivery.module_admin.adapter.AdminFoodAdapter;
import com.example.fooddelivery.module_admin.viewmodel.AdminFoodViewModel;

public class ManageFoodFragment extends Fragment {

    private FragmentManageFoodBinding binding;
    private AdminFoodAdapter adapter;
    private AdminFoodViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageFoodBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        setupRecyclerView();
        observeData();
        setupFab();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new AdminFoodAdapter();
        binding.recyclerFood.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFood.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdminFoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(FoodItem food) {
                Intent intent = new Intent(requireContext(), AddEditFoodActivity.class);
                intent.putExtra("food_id", food.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(FoodItem food) {
                viewModel.deleteFood(food.getId());
            }
        });
    }

    private void observeData() {
        viewModel.getAllFoods().observe(getViewLifecycleOwner(), foods -> {
            adapter.submitList(foods);
            binding.emptyView.setVisibility(foods.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddEditFoodActivity.class));
        });
    }
}