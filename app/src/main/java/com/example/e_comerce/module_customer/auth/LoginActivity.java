package com.example.e_comerce.module_customer.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.R;
import com.example.e_comerce.core.utils.UserHelper;
import com.example.e_comerce.module_admin.AdminMainActivity;
import com.example.e_comerce.module_customer.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, editPassword;
    private TextView tvForgotPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_login);

        edtUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginBtn);
        btnRegister = findViewById(R.id.registerBtn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

        btnLogin.setOnClickListener(v -> performLogin());
        btnRegister.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
    }
    private void saveLoginState(boolean isLoggedIn, String username, boolean isAdmin) {
        SharedPreferences prefs = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("isLoggedIn", isLoggedIn)
                .putString("username", username)
                .putBoolean("isAdmin", isAdmin)
                .apply();
    }

    private void performLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();


        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        // Check admin
        if (username.equals("admin") && password.equals("123456")) {
            saveLoginState(true, username, false);
            startActivity(new Intent(this, AdminMainActivity.class));
            finish();
            return;
        }

        // Check customer
        if (UserHelper.verifyUser(this, username, password)) {
            // customer login
            saveLoginState(true, username, false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        // Nếu không match → sai mật khẩu
        Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
    }
}