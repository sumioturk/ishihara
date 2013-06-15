package com.sumioturk.satomi.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Account setting
 */
public class AccountSetting {

    private Context context;

    private SharedPreferences sharedPreferences;

    private static final String ID = AccountSetting.class.getCanonicalName();

    private final static String PREF_KEY_USER_ID = getKey("user_id");

    private static final String getKey(String suffix){
        return ID + "." + suffix;
    }

   AccountSetting(Context context){
       this.context = context;
       this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
   }

    public void saveUserId(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY_USER_ID, id);
        editor.commit();
    }

    public String getUserId(){
        return sharedPreferences.getString(PREF_KEY_USER_ID, null);
    }


}
