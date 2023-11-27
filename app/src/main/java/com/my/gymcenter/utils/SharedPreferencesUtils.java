package com.my.gymcenter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.my.gymcenter.entity.User;

public class SharedPreferencesUtils {

    public static boolean saveUserInfo(Context context, User user) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putString("permission", user.get_permission());
            editor.putInt("user_id", user.get_id());
            editor.putString("username", user.get_username());
            editor.putString("phone_num", user.get_phone_num());
            editor.putString("password", user.get_password());
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserInfo(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            int user_id = sharedPreferences.getInt("user_id", 0);
            String password = sharedPreferences.getString("password", "");
            String username = sharedPreferences.getString("username", "");
            String permission = sharedPreferences.getString("permission", "user");
            String phone_num = sharedPreferences.getString("phone_num", "");
            User user = new User(username,password);
            user.set_id(user_id);
            user.set_permission(permission);
            user.set_phone_num(phone_num);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return new User();
        }
    }

    public static String getServerUrl(Context context) {
        try {
            return "http://192.168.2.225:9097";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
