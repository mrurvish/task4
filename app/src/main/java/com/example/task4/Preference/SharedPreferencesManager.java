package com.example.task4.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USERNAME = "email";
    private static final String KEY_USER_ID = "_id";
    private  static  final  String KEY_TOKEN = "token";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setEmail(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getEmail() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public void setId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getId() {
        return preferences.getString(KEY_USER_ID, "");
    }
    public String getToken(){
        return preferences.getString(KEY_TOKEN,"");
    }
    public  void setToken(String token){
        editor.putString(KEY_TOKEN,token);
        editor.apply();
    }

}

