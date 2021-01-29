package com.app.session.openlive.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.session.openlive.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
