package com.example.e_comerce.module_customer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout; // Import LinearLayout
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

    // Các nút danh mục
    private LinearLayout btnCatAll, btnCatChicken, btnCatBurger, btnCatPizza, btnCatNoodles;

    private CustomerFoodViewModel foodViewModel;
    private CartViewModel cartViewModel;

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


        initViews(view);
        setupViewModels();
        setupRecyclerView();
        setupCategoryClicks();
        setupSearchBar();
    }

    private void initViews(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        rvFoodList = view.findViewById(R.id.rvFoodList);
        ImageView restaurantImage = view.findViewById(R.id.ivRestaurantImage);

        // Ánh xạ các nút danh mục
        btnCatAll = view.findViewById(R.id.btnCatAll);
        btnCatChicken = view.findViewById(R.id.btnCatChicken);
        btnCatBurger = view.findViewById(R.id.btnCatBurger);
        btnCatPizza = view.findViewById(R.id.btnCatPizza);
        btnCatNoodles = view.findViewById(R.id.btnCatNoodles);

        if (restaurantImage != null) {
            restaurantImage.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Khuyến mãi Pizza mua 1 tặng 1!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void setupViewModels() {
        cartViewModel = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(requireActivity().getApplication())).get(CartViewModel.class);

        foodViewModel = new ViewModelProvider(this).get(CustomerFoodViewModel.class);

        // Quan sát dữ liệu
        foodViewModel.getAllFoods().observe(getViewLifecycleOwner(), foodEntities -> {
            if (foodEntities != null) {
                originalList.clear();
                for (FoodEntity entity : foodEntities) {
                    originalList.add(FoodMapper.toModel(entity));
                }
                // Mặc định hiển thị tất cả
                foodAdapter.setFoodList(new ArrayList<>(originalList));
            }
        });
    }

    private void setupRecyclerView() {
        rvFoodList.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter = new HomeFoodAdapter(new HomeFoodAdapter.OnFoodClickListener() {
            @Override
            public void onAddToCartClick(FoodItem food) {
                CartItem item = new CartItem(food.getId(), food.getName(), food.getPrice(), 1, food.getImage());
                cartViewModel.addToCart(item);
                Toast.makeText(getContext(), "Đã thêm " + food.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(FoodItem food) {
                FoodDetailFragment fragment = FoodDetailFragment.newInstance(food);
                getParentFragmentManager().beginTransaction()
                        .add(R.id.nav_host_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        rvFoodList.setAdapter(foodAdapter);
    }

    private void setupCategoryClicks() {
        btnCatAll.setOnClickListener(v -> filterByCategory("ALL"));
        btnCatChicken.setOnClickListener(v -> filterByCategory("Chicken"));
        btnCatBurger.setOnClickListener(v -> filterByCategory("Burger"));
        btnCatPizza.setOnClickListener(v -> filterByCategory("Pizza"));
        btnCatNoodles.setOnClickListener(v -> filterByCategory("Noodles"));
    }

    private void filterByCategory(String category) {
        List<FoodItem> filteredList = new ArrayList<>();

        if (category.equals("ALL")) {
            filteredList.addAll(originalList);
            Toast.makeText(getContext(), "Tất cả món ăn", Toast.LENGTH_SHORT).show();
        } else {
            for (FoodItem item : originalList) {
                if (item.getCategory() != null && item.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(item);
                }
            }
            Toast.makeText(getContext(), "Đang xem: " + category, Toast.LENGTH_SHORT).show();
        }

        foodAdapter.setFoodList(filteredList);
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

    private void showThongBao() {
        if (getContext() == null) return;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_khuyenmai);
        dialog.setCancelable(false);

        Button btnClose = dialog.findViewById(R.id.btnClose);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}