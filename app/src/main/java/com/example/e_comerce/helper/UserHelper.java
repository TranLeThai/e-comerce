package com.example.e_comerce.helper;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserHelper {

    private static final String PREF_NAME = "UserData";
    private static final String KEY_USERS = "users";

    // Lấy danh sách user
    public static JSONArray getUsers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_USERS, "[]"); // nếu chưa có -> mảng rỗng
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
    // Lưu danh sách user
    public static void saveUsers(Context context, JSONArray users) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USERS, users.toString()).apply();
    }

    // Thêm user mới
    public static boolean addUser(Context context, String username, String password) {
        JSONArray users = getUsers(context);

        // kiểm tra trùng username
        for (int i = 0; i < users.length(); i++) {
            try {
                if (users.getJSONObject(i).getString("username").equals(username)) {
                    return false; // username đã tồn tại
                }
            } catch (JSONException ignored) {}
        }

        // Thêm user mới
        JSONObject newUser = new JSONObject();
        try {
            newUser.put("username", username);
            newUser.put("password", password);
            users.put(newUser);
            saveUsers(context, users);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
