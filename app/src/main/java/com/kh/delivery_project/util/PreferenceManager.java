package com.kh.delivery_project.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kh.delivery_project.domain.DeliverVo;

import java.sql.Date;
import java.sql.Timestamp;

public class PreferenceManager {

    public static final String PREFERENCE_NAME = "rebuild_preference";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void setDeliverVo(Context context, DeliverVo deliverVo) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("dlvr_no", deliverVo.getDlvr_no());
        editor.putString("dlvr_id", deliverVo.getDlvr_id());
        editor.putString("dlvr_pw", deliverVo.getDlvr_pw());
        editor.putString("dlvr_name", deliverVo.getDlvr_name());
        editor.putString("dlvr_phone", deliverVo.getDlvr_phone());
        editor.putString("dlvr_email", deliverVo.getDlvr_email());
        editor.putString("dlvr_addr", deliverVo.getDlvr_addr());
        editor.putString("dlvr_img", deliverVo.getDlvr_img());
        editor.putString("dlvr_idcard", deliverVo.getDlvr_idcard());
        editor.putString("dlvr_birth", String.valueOf(deliverVo.getDlvr_birth()));
        editor.putString("dlvr_date", String.valueOf(deliverVo.getDlvr_date()));
        editor.putString("dlvr_state", deliverVo.getDlvr_state());
        editor.putInt("dlvr_point", deliverVo.getDlvr_point());
        editor.putString("dlvr_rank", deliverVo.getDlvr_rank());
        editor.putString("account_state", deliverVo.getAccount_state());
        editor.commit();
    }

    public static DeliverVo getDeliverVo(Context context) {
        DeliverVo deliverVo = new DeliverVo();
        SharedPreferences pref = getPreferences(context);
        deliverVo.setDlvr_no(pref.getInt("dlvr_no", 0));
        deliverVo.setDlvr_id(pref.getString("dlvr_id", ""));
        deliverVo.setDlvr_pw(pref.getString("dlvr_pw", ""));
        deliverVo.setDlvr_name(pref.getString("dlvr_name", ""));
        deliverVo.setDlvr_phone(pref.getString("dlvr_phone", ""));
        deliverVo.setDlvr_email(pref.getString("dlvr_email", ""));
        deliverVo.setDlvr_addr(pref.getString("dlvr_addr", ""));
        deliverVo.setDlvr_img(pref.getString("dlvr_img", ""));
        deliverVo.setDlvr_idcard(pref.getString("dlvr_idcard", ""));
        try {
            Date dlvr_birth = ConvertUtil.getBirth(pref.getString("dlvr_birth", ""));
            Timestamp dlvr_date = ConvertUtil.getDate(pref.getString("dlvr_date", ""));
            deliverVo.setDlvr_birth(dlvr_birth);
            deliverVo.setDlvr_date(dlvr_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliverVo.setDlvr_state(pref.getString("dlvr_state", ""));
        deliverVo.setDlvr_point(pref.getInt("dlvr_point", 0));
        deliverVo.setDlvr_rank(pref.getString("dlvr_rank", ""));
        deliverVo.setAccount_state(pref.getString("account_state", ""));
        return deliverVo;
    }

    public static void removeDeliverVo(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("dlvr_no");
        editor.remove("dlvr_id");
        editor.remove("dlvr_pw");
        editor.remove("dlvr_name");
        editor.remove("dlvr_phone");
        editor.remove("dlvr_email");
        editor.remove("dlvr_addr");
        editor.remove("dlvr_img");
        editor.remove("dlvr_idcard");
        editor.remove("dlvr_birth");
        editor.remove("dlvr_date");
        editor.remove("dlvr_state");
        editor.remove("dlvr_point");
        editor.remove("dlvr_rank");
        editor.remove("account_state");
        editor.commit();
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
