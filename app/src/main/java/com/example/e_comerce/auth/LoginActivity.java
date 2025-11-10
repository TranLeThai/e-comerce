package com.example.e_comerce.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_comerce.R;
import com.example.e_comerce.auth.RegisterActivity;
import com.example.e_comerce.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    long backPressedTime; // Biến dùng cho double back exit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginBtn);
        btnRegister = findViewById(R.id.registerBtn);

        // --- Sự kiện đăng nhập ---
        btnLogin.setOnClickListener(v -> {
            String user = edtUsername.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            // 1️⃣ Kiểm tra ô trống
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2️⃣ Kiểm tra tài khoản mẫu
            if (user.equals("admin") && pass.equals("123456")) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // (Tùy chọn) Lưu trạng thái đăng nhập
                getSharedPreferences("LOGIN_PREF", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .apply();

                // 3️⃣ Chuyển sang trang Home
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish(); // Đóng màn hình login để không quay lại
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Sự kiện mở trang đăng ký ---
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
