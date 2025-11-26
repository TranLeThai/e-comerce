package com.example.e_comerce.module_customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.viewmodel.AnalyticsViewModel; // Import ViewModel mới
import com.example.e_comerce.module_customer.menu.adapter.HomeFoodAdapter; // Tái sử dụng Adapter

// Lưu ý: Cần có HomeFoodAdapter.OnFoodClickListener để Adapter hoạt động
public class FavoriteFragment extends Fragment implements HomeFoodAdapter.OnFoodClickListener {

    private RecyclerView rvFavorites;
    private HomeFoodAdapter adapter;
    private AnalyticsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HomeFoodAdapter(this);
        rvFavorites.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);

        viewModel.getTopSellingFoodsLiveData().observe(getViewLifecycleOwner(), foodItems -> {
            adapter.setFoodList(foodItems);
        });

        return view;
    }

    @Override
    public void onAddToCartClick(FoodItem food) {

        Toast.makeText(getContext(), "Đã thêm " + food.getName() + " từ gợi ý", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(FoodItem food) {
        Toast.makeText(getContext(), "Xem chi tiết món " + food.getName(), Toast.LENGTH_SHORT).show();
    }
}