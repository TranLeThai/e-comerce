package com.example.e_comerce.module_customer.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.R;
import com.example.e_comerce.core.data.prefs.PrefManager;
import com.example.e_comerce.module_admin.AdminMainActivity;
import com.example.e_comerce.module_customer.cart.CartActivity;
import com.example.e_comerce.module_customer.main.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword;
    private MaterialButton btnLogin;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        prefManager = new PrefManager(this);
        setupClickListeners();
    }

    private void initViews() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Demo accounts
        if ("admin@gmail.com".equals(email) && "123".equals(password)) {
            prefManager.saveToken("admin_token");
            prefManager.saveRole("admin");
            startActivity(new Intent(this, AdminMainActivity.class));
        } else if ("user@gmail.com".equals(email) && "123".equals(password)) {
            prefManager.saveToken("user_token");
            prefManager.saveRole("customer");
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
}