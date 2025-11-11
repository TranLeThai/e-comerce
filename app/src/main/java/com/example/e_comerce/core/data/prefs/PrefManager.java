// core/data/prefs/PrefManager.java
package com.example.e_comerce.core.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "FoodDeliveryPref";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_ROLE = "user_role"; // "customer" or "admin"

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public void saveRole(String role) {
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "customer");
    }

    public boolean isAdmin() {
        return "admin".equals(getRole());
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}