package com.example.e_comerce.module_customer;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.local.database.CartViewModelFactory;
import com.example.e_comerce.core.data.local.entity.CartItem;
import com.example.e_comerce.core.data.model.FoodItem;
import com.example.e_comerce.core.viewmodel.CartViewModel;

import java.io.Serializable;

public class FoodDetailFragment extends Fragment {

    private FoodItem food;

    // Hàm static để nhận dữ liệu từ MainActivity gửi sang
    public static FoodDetailFragment newInstance(FoodItem food) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        // FoodItem đã implements Serializable nên không cần ép kiểu thủ công
        args.putSerializable("food_object", food);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            food = (FoodItem) getArguments().getSerializable("food_object");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);

        // Ánh xạ View
        ImageView img = view.findViewById(R.id.imgDetail);
        TextView tvName = view.findViewById(R.id.tvDetailName);
        TextView tvPrice = view.findViewById(R.id.tvDetailPrice);
        Button btnAdd = view.findViewById(R.id.btnAddDetail);

        // Hiển thị dữ liệu
        if (food != null) {
            tvName.setText(food.getName());
            tvPrice.setText(String.format("%,.0f đ", food.getPrice()));

            // --- SỬA LOGIC HIỂN THỊ ẢNH ---
            try {
                // 1. Thử coi nó là số (Resource ID - Ảnh có sẵn)
                int resId = Integer.parseInt(food.getImage());
                img.setImageResource(resId);
            } catch (NumberFormatException e) {
                // 2. Nếu lỗi (không phải số) -> Dùng URI (Ảnh Admin chọn)
                try {
                    img.setImageURI(Uri.parse(food.getImage()));
                } catch (Exception ex) {
                    img.setImageResource(R.drawable.ic_launcher_background); // Ảnh fallback
                }
            }
        }

        // Setup ViewModel
        CartViewModel cartViewModel = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(requireActivity().getApplication())).get(CartViewModel.class);

        // Click thêm vào giỏ
        btnAdd.setOnClickListener(v -> {
            if (food != null) {
                CartItem item = new CartItem(
                        food.getId(),
                        food.getName(),
                        food.getPrice(),
                        1,
                        food.getImage() // --- SỬA: Truyền thẳng String ảnh
                );
                cartViewModel.addToCart(item);
                Toast.makeText(getContext(), "Đã thêm vào giỏ!", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình trước
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}