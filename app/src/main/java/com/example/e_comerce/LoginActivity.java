package com.example.e_comerce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, registerBtn;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Liên kết các component
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        titleText = findViewById(R.id.titleText);

        // Xử lý sự kiện nút
        loginBtn.setOnClickListener(v -> {
            String userInput = username.getText().toString();
            String passInput = password.getText().toString();

            JSONArray users = UserHelper.getUsers(this);

            boolean isValid = false;

            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("username").equals(userInput) &&
                            user.getString("password").equals(passInput)) {
                        isValid = true;
                        break;
                    }
                } catch (JSONException ignored) {}
            }

            if (isValid) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sai username hoặc password!", Toast.LENGTH_SHORT).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
