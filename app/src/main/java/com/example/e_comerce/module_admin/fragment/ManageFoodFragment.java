// module_admin/fragment/ManageFoodFragment.java
package com.example.e_comerce.module_admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.databinding.FragmentManageFoodBinding;
import com.example.e_comerce.module_admin.activity.AddEditFoodActivity;
import com.example.e_comerce.module_admin.adapter.AdminFoodAdapter;
import com.example.e_comerce.module_admin.viewmodel.AdminFoodViewModel;

public class ManageFoodFragment extends Fragment {

    private FragmentManageFoodBinding binding;
    private AdminFoodAdapter adapter;
    private AdminFoodViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageFoodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel (AndroidViewModel tự động inject Application Context)
        viewModel = new ViewModelProvider(this).get(AdminFoodViewModel.class);

        setupRecyclerView();
        observeData();
        setupFab();
    }

    private void setupRecyclerView() {
        adapter = new AdminFoodAdapter();
        binding.recyclerFood.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFood.setAdapter(adapter);

        // Xử lý sự kiện click từ Adapter
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
        // Lắng nghe dữ liệu từ Database
        viewModel.getAllFoods().observe(getViewLifecycleOwner(), foods -> {
            adapter.submitList(foods);
            // Hiện thông báo trống nếu list rỗng
            if (binding.emptyView != null) {
                binding.emptyView.setVisibility(foods.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddEditFoodActivity.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}