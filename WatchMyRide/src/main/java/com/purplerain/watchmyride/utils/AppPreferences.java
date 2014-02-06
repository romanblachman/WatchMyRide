package com.purplerain.watchmyride.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by roman on 2/6/14.
 */
public class AppPreferences {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public AppPreferences(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public void putString(String key,String Value){
        editor.putString(key,Value);
        editor.commit();
    }

    public String getString(String key){
        return sp.getString(key,null);
    }

}