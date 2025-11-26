// module_admin/AdminMainActivity.java
package com.example.e_comerce.module_admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.prefs.PrefManager;
import com.example.e_comerce.databinding.ActivityAdminMainBinding;
import com.example.e_comerce.module_admin.fragment.ManageFoodFragment;
import com.example.e_comerce.module_admin.fragment.ManageOrderFragment;
import com.example.e_comerce.module_admin.fragment.RevenueFragment;
import com.example.e_comerce.module_customer.auth.LoginActivity;

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

        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
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

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất Admin")
                .setMessage("Thoát phiên làm việc?")
                .setPositiveButton("Thoát", (dialog, which) -> {
                    PrefManager prefManager = new PrefManager(this);
                    prefManager.logout();

                    Intent intent = new Intent(this, LoginActivity.class);
                    // Cờ này giúp xóa sạch các màn hình cũ, bấm Back sẽ thoát app luôn chứ không quay lại Admin
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}