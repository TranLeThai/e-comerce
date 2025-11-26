package com.example.e_comerce.module_customer;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailFragment extends Fragment {

    private FoodItem food;
    private int quantity = 1;
    private String selectedSize = "Vừa";
    private double basePrice = 0; // Giá gốc của món ăn
    private double currentPrice = 0; // Giá sau khi cộng trừ Size

    private TextView tvQuantity, tvDetailPrice;
    private Button btnAdd;

    public static FoodDetailFragment newInstance(FoodItem food) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_food_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView img = view.findViewById(R.id.imgDetail);
        TextView tvName = view.findViewById(R.id.tvDetailName);
        tvDetailPrice = view.findViewById(R.id.tvDetailPrice);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnAdd = view.findViewById(R.id.btnAddDetail);
        ImageButton btnMinus = view.findViewById(R.id.btnMinus);
        ImageButton btnPlus = view.findViewById(R.id.btnPlus);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupSize);
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbarDetail);

        if (food != null) {
            tvName.setText(food.getName());

            // Lưu giá gốc
            basePrice = food.getPrice();
            currentPrice = basePrice;
            updatePriceButton();

            try {
                int resId = Integer.parseInt(food.getImage());
                img.setImageResource(resId);
            } catch (Exception e) {
                try {
                    img.setImageURI(Uri.parse(food.getImage()));
                } catch (Exception ex) {
                    img.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updatePriceButton();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updatePriceButton();
            }
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSmall) {
                selectedSize = "Nhỏ";
                currentPrice = basePrice - 20000;
            } else if (checkedId == R.id.rbMedium) {
                selectedSize = "Vừa";
                currentPrice = basePrice; // Giữ nguyên
            } else if (checkedId == R.id.rbLarge) {
                selectedSize = "Lớn";
                currentPrice = basePrice + 10000;
            }
            updatePriceButton();
        });

        CartViewModel cartViewModel = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(requireActivity().getApplication())).get(CartViewModel.class);

        btnAdd.setOnClickListener(v -> {
            if (food != null) {
                String nameWithSize = food.getName() + " (" + selectedSize + ")";

                CartItem item = new CartItem(
                        food.getId(),
                        nameWithSize,
                        currentPrice,
                        quantity,
                        food.getImage()
                );
                cartViewModel.addToCart(item);
                Toast.makeText(getContext(), "Đã thêm: " + nameWithSize, Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void updatePriceButton() {
        double total = currentPrice * quantity;
        String priceStr = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(currentPrice);
        String totalStr = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);

        tvDetailPrice.setText(priceStr);
        btnAdd.setText("Thêm vào giỏ - " + totalStr);
    }
}