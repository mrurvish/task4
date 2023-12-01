package com.example.task4.Preference;



import android.content.Context;
import android.content.SharedPreferences;

import com.example.task4.DataModels.Settings;
import com.google.gson.Gson;

public class CreatePref {
    private  SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public CreatePref(Context context,String prefname){
        preferences = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }



    public void clearpref(){
        editor.clear();
        editor.apply();
    }
    public void setString(String Key,String Data) {
        editor.putString(Key, Data);
        editor.apply();
    }

    public String getString(String Key) {
        return preferences.getString(Key, "");
    }
    public void setString(String Key,Settings Data) {
        Gson gson = new Gson();
        String json = gson.toJson(Data);
        editor.putString(Key,json);
        editor.apply();
    }
public Settings getobject(String Key){
    Gson gson = new Gson();
    Settings settings =gson.fromJson(preferences.getString(Key, ""), Settings.class);
        return settings;
}
}
