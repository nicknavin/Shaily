package com.app.session.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.session.data.model.Brief_CV;
import com.app.session.data.model.Language;
import com.app.session.data.model.UserLangauges;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;




public class DataPrefrence {

    public static final String PREFRENCE_NAME = "Data_Prefs";

    public static void setPref(Context c, String pref, String val) {

        SharedPreferences.Editor e = c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPref(Context c, String pref, String val) {
        return c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).getString(pref, val);
    }

    public static void setPref(Context c, String pref, Integer val) {
        SharedPreferences.Editor e = c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).edit();
        e.putInt(pref, val);
        e.commit();
    }

    public static Integer getPref(Context c, String pref, Integer val) {
        return c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).getInt(pref, val);
    }

    public static void setPref(Context c, String pref, Boolean val) {
        SharedPreferences.Editor e = c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    public static boolean getPref(Context c, String pref, Boolean val) {
        return c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).getBoolean(pref, val);
    }

    public static void deletePrefs(Context c) {
        c.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static void deleteKey(Context activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().remove(key).commit();
    }



//    public static void setUserData(Context context, String key, UserData value) {
//
//        try {
//
//            SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME, 0);
//            SharedPreferences.Editor editor = settings.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(value);
//            editor.putString(key, json);
//            editor.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public static UserData getUserData(Context context, String key)
//    {
//        UserData userData = new UserData();
//        SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, 0);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, "");
//        userData = gson.fromJson(json,
//                new TypeToken<UserData>() {
//                }.getType());
//        return userData;
//    }


//    public static void setHobbies(Context context, String key, LinkedList<Hobbies> value) {
//
//        try {
//            SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME, 0);
//            SharedPreferences.Editor editor = settings.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(value);
//            editor.putString(key, json);
//            editor.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public static LinkedList<Hobbies> getHobbies(Context context, String key) {
//        LinkedList<Hobbies> hobies = new LinkedList<>();
//        SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, 0);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, "");
//        hobies = gson.fromJson(json,
//                new TypeToken<LinkedList<Hobbies>>() {
//                }.getType());
//        return hobies;
//    }

    public static void setLanguage(Context context, String key, ArrayList<Language> value) {

    try {
        SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    } catch (Exception e) {
        e.printStackTrace();
    }

}
    public static ArrayList<Language> getLanguage(Context context, String key) {
        ArrayList<Language> hobies = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        hobies = gson.fromJson(json,
                new TypeToken<ArrayList<Language>>() {
                }.getType());
        return hobies;
    }

    public static void setBriefLanguage(Context context, String key, ArrayList<Brief_CV> value) {

        try {
            SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            Gson gson = new Gson();
            String json = gson.toJson(value);
            editor.putString(key, json);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<Brief_CV> getBriefLanguage(Context context, String key) {
        ArrayList<Brief_CV> brief_cvs = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        brief_cvs = gson.fromJson(json,
                new TypeToken<ArrayList<Brief_CV>>() {
                }.getType());
        return brief_cvs;
    }


    public static void setLanguageDb(Context context, String key, ArrayList<UserLangauges> value) {

        try {
            SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            Gson gson = new Gson();
            String json = gson.toJson(value);
            editor.putString(key, json);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<UserLangauges> getLanguageDb(Context context, String key) {
        ArrayList<UserLangauges> brief_cvs = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, 0);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        brief_cvs = gson.fromJson(json,
                new TypeToken<ArrayList<UserLangauges>>() {
                }.getType());
        return brief_cvs;
    }


}
