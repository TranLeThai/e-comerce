// module_admin/AdminMainActivity.java
package com.example.e_comerce.module_admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.e_comerce.R;
import com.example.e_comerce.databinding.ActivityAdminMainBinding;
import com.example.fooddelivery.module_admin.fragment.ManageFoodFragment;
import com.example.e_comerce.module_admin.fragment.ManageOrderFragment;
import com.example.e_comerce.module_admin.fragment.RevenueFragment;

public class AdminMainActivity extends AppCompatActivity {

    private ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigation();
        if (savedInstanceState == null) {
            replaceFragment(new ManageFoodFragment());
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_manage_food) {
                replaceFragment(new ManageFoodFragment());
            } else if (id == R.id.nav_manage_order) {
                replaceFragment(new ManageOrderFragment());
            } else if (id == R.id.nav_revenue) {
                replaceFragment(new RevenueFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}