package com.example.e_comerce.module_customer.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.e_comerce.module_customer.main.ProfileFragment;
import com.example.e_comerce.R;
import com.example.e_comerce.module_customer.cart.CartActivity;
import com.example.e_comerce.module_admin.fragment.RevenueFragment; // Ví dụ Favorite
import com.example.e_comerce.module_customer.cart.CartFragment;
import com.example.e_comerce.module_customer.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Mặc định load HomeFragment khi mở app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();
        }

        // Bắt sự kiện chọn tab
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }
            else if (id == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            }
            else if (id == R.id.nav_favorite) {
                // Ví dụ Favorite Fragment
                selectedFragment = new RevenueFragment(); // Tạm thời
            }
            else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}