package com.example.e_comerce.core.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "EcommercePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role"; // "admin" hoặc "customer"

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Hàm lưu thông tin khi đăng nhập thành công
    public void saveLogin(String email, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "customer");
    }

    // --- HÀM QUAN TRỌNG: ĐĂNG XUẤT ---
    public void logout() {
        editor.clear(); // Xóa sạch dữ liệu
        editor.apply();
    }
}