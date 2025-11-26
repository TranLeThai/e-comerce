package com.example.e_comerce.module_customer.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.prefs.PrefManager;
import com.example.e_comerce.module_customer.auth.LoginActivity;
import com.example.e_comerce.module_customer.history.OrderHistoryActivity;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvEmail = view.findViewById(R.id.tvEmail);
        LinearLayout btnHistory = view.findViewById(R.id.btnHistory);
        LinearLayout btnLogout = view.findViewById(R.id.btnLogout);

        // 1. Hiển thị Email
        if (getContext() != null) {
            PrefManager prefManager = new PrefManager(getContext());
            // tvEmail.setText(prefManager.getEmail()); // Nếu có lưu email
            tvEmail.setText("Xin chào, Khách hàng!");
        }

        // 2. Chuyển sang màn hình Lịch sử (Vẫn giữ Activity cho lịch sử cũng được)
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        // 3. Xử lý Đăng xuất
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn thoát?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    PrefManager prefManager = new PrefManager(getContext());
                    prefManager.logout();

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}