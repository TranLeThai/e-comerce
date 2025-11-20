package com.example.e_comerce.module_customer.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_comerce.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtUsername;
    private TextView tvResult;
    private Button btnFindPassword, btnBackToLogin;

    private static final String PREF_NAME = "UserData";
    private static final String KEY_USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tvResult = findViewById(R.id.tvResult);
        btnFindPassword = findViewById(R.id.btnFindPassword);
        edtUsername = findViewById(R.id.edtUsername);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnFindPassword.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            if(username.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
                tvResult.setText("");
                return;
            }

            String password = findPasswordByUsername(username);

            if (password != null) {
                tvResult.setText("Mật khẩu của bạn là:\n" + password);
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvResult.setText("Không tìm thấy tài khoản \"" + username + "\"");
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });

    }

    private String findPasswordByUsername(String username) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USERS, null);

        if (userJson == null || userJson.isEmpty()) {
            return null;
        }

        try {
            JSONArray usersArray = new JSONArray(userJson);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);

                String savedUsername = user.getString("username");
                String savedPassword = user.getString("password");

                if (username.equals(savedUsername)) {
                    return savedPassword;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}