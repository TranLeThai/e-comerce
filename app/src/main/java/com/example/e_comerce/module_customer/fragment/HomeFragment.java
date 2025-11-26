package com.example.e_comerce.module_customer.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.CartViewModelFactory;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.local.entity.FoodEntity;
import com.example.e_comerce.core.data.mapper.FoodMapper;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.viewmodel.CartViewModel;
import com.example.e_comerce.core.viewmodel.CustomerFoodViewModel;
import com.example.e_comerce.module_customer.FoodDetailFragment;
import com.example.e_comerce.module_customer.menu.adapter.HomeFoodAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private EditText edtSearch;
    private RecyclerView rvFoodList;
    private HomeFoodAdapter foodAdapter;

    private CustomerFoodViewModel foodViewModel;
    private CartViewModel cartViewModel;

    // Danh sách cho tìm kiếm
    private List<FoodItem> originalList = new ArrayList<>();

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ View
        edtSearch = view.findViewById(R.id.edtSearch);
        rvFoodList = view.findViewById(R.id.rvFoodList);
        ImageView restaurantImage = view.findViewById(R.id.ivRestaurantImage);

        // 2. Setup ViewModel
        cartViewModel = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(requireActivity().getApplication())).get(CartViewModel.class);

        foodViewModel = new ViewModelProvider(this).get(CustomerFoodViewModel.class);

        // 3. Setup RecyclerView
        rvFoodList.setLayoutManager(new LinearLayoutManager(getContext()));

        foodAdapter = new HomeFoodAdapter(new HomeFoodAdapter.OnFoodClickListener() {
            @Override
            public void onAddToCartClick(FoodItem food) {
                // Tạo CartItem từ FoodItem
                CartItem item = new CartItem(
                        food.getId(),
                        food.getName(),
                        food.getPrice(),
                        1,
                        food.getImage() // Dùng chuỗi ảnh (URI hoặc ID)
                );
                cartViewModel.addToCart(item);
                Toast.makeText(getContext(), "Đã thêm " + food.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(FoodItem food) {
                // Mở màn hình chi tiết
                FoodDetailFragment fragment = FoodDetailFragment.newInstance(food);

                getParentFragmentManager().beginTransaction()
                        // ⚠️ SỬA QUAN TRỌNG: ID này phải khớp với XML của MainActivity
                        .add(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        rvFoodList.setAdapter(foodAdapter);

        // 4. Quan sát dữ liệu
        foodViewModel.getAllFoods().observe(getViewLifecycleOwner(), foodEntities -> {
            if (foodEntities != null) {
                originalList.clear();
                for (FoodEntity entity : foodEntities) {
                    originalList.add(FoodMapper.toModel(entity));
                }
                // Ban đầu hiển thị tất cả
                foodAdapter.setFoodList(new ArrayList<>(originalList));
            }
        });

        // 5. Cài đặt Tìm kiếm
        setupSearchBar();

        // 6. Sự kiện Click ảnh Banner
        if (restaurantImage != null) {
            restaurantImage.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Khuyến mãi Pizza mua 1 tặng 1!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void setupSearchBar() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoods(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterFoods(String keyword) {
        List<FoodItem> filteredList = new ArrayList<>();

        if (keyword == null || keyword.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String query = keyword.toLowerCase().trim();
            for (FoodItem item : originalList) {
                if (item.getName().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
        }
        foodAdapter.setFoodList(filteredList);
    }
}