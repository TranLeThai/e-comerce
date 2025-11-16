// module_customer/auth/LoginActivity.java (SIMPLE - FOR MID-TERM PROJECT)
package com.example.e_comerce.module_customer.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.R;
import com.example.e_comerce.module_admin.AdminMainActivity;
import com.example.e_comerce.module_customer.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_login);

        // Khởi tạo views
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Click login
        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String username = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Kiểm tra rỗng
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        // Check admin
        if (username.equals("admin") && password.equals("123456")) {
            saveLoginState(true);
            startActivity(new Intent(this, AdminMainActivity.class));
            finish();
            return; // ← QUAN TRỌNG! Thoát khỏi method
        }

        // Check customer
        if (UserHelper.verifyUser(this, username, password)) {
            // customer login
            return;

        // Nếu không match → sai mật khẩu
        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
    }

    private void saveLoginState(boolean isLoggedIn) {
        getSharedPreferences("LOGIN_PREF", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", isLoggedIn) // ← SỬA: putBoolean thay vì isBoolean
                .apply();
    }
}