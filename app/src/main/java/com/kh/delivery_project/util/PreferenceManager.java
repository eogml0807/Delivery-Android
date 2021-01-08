package com.kh.delivery_project.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String PREFERENCE_NAME = "rebuild_preference";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // 값 저장
    // @param context, @param key, @param value
    public static void setString(Context context, String key, String value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    // 값 호출
    public static String getString(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        String value = pref.getString(key, "");
        return value;
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        boolean value = pref.getBoolean(key, false);
        return value;
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        int value = pref.getInt(key, -1);
        return value;
    }

    public static Long getLong(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        Long value = pref.getLong(key, -1);
        return value;
    }

    public static float getFloat(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        float value = pref.getFloat(key, -1);
        return value;
    }

    // 키 값 삭제
    public static void removeKey(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    // 모든 데이터 삭제
    public static void clear(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
